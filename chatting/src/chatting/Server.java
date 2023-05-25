package chatting;

import java.awt.Color;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Server extends JFrame implements ActionListener {

	// GUI 자원
	private JPanel mainPanel;
	private JTextArea textArea;
	private ScrollPane serverInfoScroll;
	private JLabel portLabel;
	private JTextField portTextField;
	private JButton serverStartButton;
	private JButton serverStopButton;

	// 네트워크 자원
	private ServerSocket serverSocket;
	private Socket socket;
	private int port;

	private Vector<UserInformation> userVectorList = new Vector<UserInformation>();
	private Vector<RoomInformation> roomVectorList = new Vector<RoomInformation>();

	public Server() {
		init();
		addListener();
	}

	private void init() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 410);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(null);
		setContentPane(mainPanel);
		serverInfoScroll = new ScrollPane();
		serverInfoScroll.setBounds(10, 10, 309, 229);
		mainPanel.add(serverInfoScroll);
		textArea = new JTextArea();
		textArea.setBounds(12, 11, 310, 230);
		textArea.setBackground(Color.WHITE);
		textArea.setEditable(false);
		serverInfoScroll.add(textArea);

		portLabel = new JLabel("포트번호 :");
		portLabel.setBounds(12, 273, 82, 15);
		mainPanel.add(portLabel);

		portTextField = new JTextField();
		portTextField.setBounds(98, 270, 224, 21);
		portTextField.setColumns(10);
		mainPanel.add(portTextField);

		serverStartButton = new JButton("서버실행");
		serverStartButton.setBounds(12, 315, 154, 23);
		mainPanel.add(serverStartButton);

		serverStopButton = new JButton("서버중지");
		serverStopButton.setBounds(168, 315, 154, 23);
		serverStopButton.setEnabled(false);
		mainPanel.add(serverStopButton);

		setVisible(true);

	}

	private void addListener() {
		portTextField.requestFocus();
		serverStartButton.addActionListener(this);
		serverStopButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == serverStartButton) {
			startNetwork();
			System.out.println("serverStartButton Click");
		} else if (e.getSource() == serverStopButton) {
			System.out.println("serverStopButton");
		}
	}

	private void startNetwork() {
		if (portTextField.getText().length() == 0) {
			System.out.println("값을 입력 하세요");
		} else if (portTextField.getText().length() != 0) {
			port = Integer.parseInt(portTextField.getText());
		}

		try {
			serverSocket = new ServerSocket(port);
			textArea.append("서버를 시작합니다.\n");
			connect();
			portTextField.setEditable(false);
			serverStartButton.setEnabled(false);
			serverStopButton.setEnabled(true);
		} catch (Exception e) {

		}
	}

	private void connect() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						textArea.append("사용자의 접속을 기다립니다\n");
						socket = serverSocket.accept();
						textArea.append("클라이언트 접속 성공!\n");

						UserInformation userInfo = new UserInformation(socket);
						userInfo.start();
					} catch (IOException e) {
						textArea.append("서버가 중지됨! 다시 스타트 버튼을 눌러주세요\n");
						break;
					}
				}

			}
		});
		thread.start();
	}

	class UserInformation extends Thread {
		private InputStream inputStream;
		private OutputStream outputStream;
		private DataInputStream dataInputStream;
		private DataOutputStream dataOutputStream;
		private String userId;
		private String currentRoomName;
		private Socket userSocket;

		private boolean roomCheck = true;

		public UserInformation(Socket socket) {
			this.userSocket = socket;
			network();
		}

		private void network() {
			try {
				inputStream = userSocket.getInputStream();
				dataInputStream = new DataInputStream(inputStream);
				outputStream = userSocket.getOutputStream();
				dataOutputStream = new DataOutputStream(outputStream);

				userId = dataInputStream.readUTF();
				textArea.append("[" + userId + "] 입장\n");

				broadcast("NewUser/" + userId);

				for (int i = 0; i < userVectorList.size(); i++) {
					UserInformation userInfo = userVectorList.elementAt(i);
					sendMessage("OldUser/" + userInfo.userId);
				}

				for (int i = 0; i < roomVectorList.size(); i++) {
					RoomInformation room = roomVectorList.elementAt(i);
					sendMessage("OldRoom/" + room.roomName);
				}

				userVectorList.add(this);

			} catch (IOException e) {
				System.out.println(e);
			}
		}

		@Override
		public void run() {
			while (true) {
				try {
					String message = dataInputStream.readUTF();
					textArea.append("[[" + userId + "]]" + message + "\n");
					inMessage(message);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}

		private void inMessage(String str) {
			StringTokenizer stringTokenizer = new StringTokenizer(str, "/");

			String protocol = stringTokenizer.nextToken();
			String message = stringTokenizer.nextToken();
			if (protocol.equals("Note")) {
				stringTokenizer = new StringTokenizer(message, "@");
				String user = stringTokenizer.nextToken();
				String note = stringTokenizer.nextToken();

				for (int i = 0; i < userVectorList.size(); i++) {
					UserInformation userInfo = userVectorList.elementAt(i);
					if (userInfo.userId.equals(user)) {
						userInfo.sendMessage("Note/" + userId + "@" + note);
					}
				}
			} else if (protocol.equals("CreateRoom")) {
				for (int i = 0; i < roomVectorList.size(); i++) {
					RoomInformation room = roomVectorList.elementAt(i);
					if (room.roomName.equals(message)) {
						sendMessage("CreateRoomFail/ok");
						roomCheck = false;
						break;
					} else {
						roomCheck = true;
					}
				}
				if (roomCheck == true) {
					RoomInformation newRoom = new RoomInformation(message, this);
					roomVectorList.add(newRoom);
					sendMessage("CreateRoom/" + message);
					broadcast("NewRoom/" + message);
				}

			}
		}

		private void sendMessage(String message) {
			try {
				dataOutputStream.writeUTF(message);
				dataOutputStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	class RoomInformation {

		private String roomName;
		private Vector<UserInformation> roomUserVectorList = new Vector<UserInformation>();

		public RoomInformation(String roomName, UserInformation userInfo) {
			this.roomName = roomName;
			this.roomUserVectorList.add(userInfo);
			userInfo.currentRoomName = roomName;
		}

//		private void addUser(UserInformation userInfo) {
//			roomUserVectorList.add(userInfo);
//		}

		// private void roomBroadcast(String str) {
//			for (int i = 0; i < roomUserVectorList.size(); i++) {
//				UserInformation userInfo = roomUserVectorList.elementAt(i);
//				userInfo.sendMessage(str);
//			}
//		}

	}

	public void broadcast(String string) {
		for (int i = 0; i < userVectorList.size(); i++) {
			UserInformation userInfo = userVectorList.elementAt(i);
			userInfo.sendMessage(string);
		}
	}

	public static void main(String[] args) {
		new Server();
	}

}

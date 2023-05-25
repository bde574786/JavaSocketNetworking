package chatting;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.NetworkChannel;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Client extends JFrame implements ActionListener {

	// 메인 패널
	private JPanel mainPanel;

	// 메인 탭
	private JTabbedPane mainTab;

	// 로그인 탭
	private JPanel loginPanel;
	private JLabel hostIPLabel;
	private JLabel serverPortLabel;
	private JLabel userIDLabel;
	private JTextField hostIPTextField;
	private JTextField serverPortTextField;
	private JTextField userIDTextField;
	private JButton connectButton;

	// 대기실 탭
	private JPanel waitingRoomPanel;
	private JLabel totalUserLabel;
	private JLabel totalRoomLabel;
	private JList totalUserList; // 전체접속자 리스트
	private JList totalRoomList; // 방 리스트
	private JButton sendNoteButton;
	private JButton joinRoomButton;

	// 채팅 탭
	private JPanel chattingPanel;
	private JScrollPane chattingScroll;
	private JTextArea viewChatTextArea;
	private JTextField chattingTextField;
	private JButton sendMessageButton;

	// 탭 외 버튼
	private JButton makeRoomButton;
	private JButton leaveRoomButton;
	private JButton endButton;

	// 네트워크
	private Socket socket;
	private String ip;
	private int port;
	private String userId;
	private InputStream inputStream;
	private OutputStream outputStream;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;

	private Vector<String> userVectorList = new Vector<String>();
	private Vector<String> roomVectorList = new Vector<String>();
	private String myRoomName;

	public Client() {
		init();
		serverPortTextField.setText("1");
		userIDTextField.setText("user1");
		addListener();
	}

	private void init() {

		// 메인 패널
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 474, 483);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(null);
		setContentPane(mainPanel);

		mainTab = new JTabbedPane(JTabbedPane.TOP);
		mainTab.setBounds(12, 27, 328, 407);
		mainPanel.add(mainTab);

		loginPanel = new JPanel();
		loginPanel.setLayout(null);
		mainTab.addTab("로그인", null, loginPanel, null);

		// 로그인 탭
		hostIPLabel = new JLabel("Host_IP ");
		hostIPLabel.setFont(new Font("휴먼모음T", Font.BOLD, 13));
		hostIPLabel.setBounds(12, 25, 91, 15);
		loginPanel.add(hostIPLabel);

		hostIPTextField = new JTextField();
		hostIPTextField.setFont(new Font("휴먼모음T", Font.BOLD, 13));
		hostIPTextField.setBounds(112, 21, 199, 21);
		hostIPTextField.setColumns(10);
		loginPanel.add(hostIPTextField);

		serverPortLabel = new JLabel("Server_Port");
		serverPortLabel.setFont(new Font("휴먼모음T", Font.BOLD, 13));
		serverPortLabel.setBounds(12, 72, 91, 15);
		loginPanel.add(serverPortLabel);

		serverPortTextField = new JTextField();
		serverPortTextField.setFont(new Font("휴먼모음T", Font.BOLD, 13));
		serverPortTextField.setBounds(112, 69, 199, 21);
		serverPortTextField.setColumns(10);
		loginPanel.add(serverPortTextField);

		userIDLabel = new JLabel("User_ID");
		userIDLabel.setFont(new Font("휴먼모음T", Font.BOLD, 13));
		userIDLabel.setBounds(12, 122, 91, 15);
		loginPanel.add(userIDLabel);

		userIDTextField = new JTextField();
		userIDTextField.setBounds(112, 119, 199, 21);
		userIDTextField.setColumns(10);
		loginPanel.add(userIDTextField);

		connectButton = new JButton("connect");
		connectButton.setFont(new Font("휴먼모음T", Font.BOLD, 12));
		connectButton.setBounds(214, 162, 97, 23);
		loginPanel.add(connectButton);

//			JLabel img_lbl = new JLabel("input the image");
//			img_lbl.setIcon(new ImageIcon());
//			img_lbl.setBounds(12, 213, 299, 155);
//			panel_1.add(img_lbl);

////////////////////////////////////////////////////////////////			

		// 대기실 탭
		waitingRoomPanel = new JPanel();
		waitingRoomPanel.setLayout(null);
		mainTab.addTab("대기실", null, waitingRoomPanel, null);

		totalUserLabel = new JLabel("전체접속자");
		totalUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalUserLabel.setFont(new Font("휴먼모음T", Font.BOLD, 13));
		totalUserLabel.setBounds(12, 28, 102, 15);
		waitingRoomPanel.add(totalUserLabel);

		totalRoomLabel = new JLabel("방 리스트");
		totalRoomLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalRoomLabel.setFont(new Font("휴먼모음T", Font.BOLD, 13));
		totalRoomLabel.setBounds(209, 27, 102, 15);
		waitingRoomPanel.add(totalRoomLabel);

		totalUserList = new JList();
		totalUserList.setBounds(12, 69, 102, 257);
		waitingRoomPanel.add(totalUserList);

		totalRoomList = new JList();
		totalRoomList.setBounds(209, 69, 102, 257);
		waitingRoomPanel.add(totalRoomList);

		sendNoteButton = new JButton("쪽지보내기");
		sendNoteButton.setFont(new Font("휴먼모음T", Font.BOLD, 12));
		sendNoteButton.setBounds(12, 345, 102, 23);
		waitingRoomPanel.add(sendNoteButton);

		joinRoomButton = new JButton("채팅방참여");
		joinRoomButton.setFont(new Font("휴먼모음T", Font.BOLD, 12));
		joinRoomButton.setBounds(209, 345, 102, 23);
		hostIPTextField.setText("127.0.0.1");
		waitingRoomPanel.add(joinRoomButton);

//////////////////////////////////////////////////////////////////////			

		// 채팅 탭
		chattingPanel = new JPanel();
		chattingPanel.setLayout(null);
		mainTab.addTab("채팅", null, chattingPanel, null);

		viewChatTextArea = new JTextArea("viewChatTextArea");
		viewChatTextArea.setEnabled(false);
		viewChatTextArea.setEditable(false);
		viewChatTextArea.setFont(new Font("휴먼모음T", Font.BOLD, 12));
		viewChatTextArea.setBounds(0, 0, 323, 337);
		chattingPanel.add(viewChatTextArea);

		chattingTextField = new JTextField("chattingTextField");
		chattingTextField.setFont(new Font("휴먼모음T", Font.BOLD, 11));
		chattingTextField.setBounds(0, 347, 214, 21);
		chattingTextField.setColumns(10);
		chattingPanel.add(chattingTextField);

		sendMessageButton = new JButton("전 송");
		sendMessageButton.setFont(new Font("휴먼모음T", Font.BOLD, 12));
		sendMessageButton.setBounds(226, 346, 97, 23);
		chattingPanel.add(sendMessageButton);

		chattingScroll = new JScrollPane();
		chattingScroll.setEnabled(false);
		chattingScroll.setBounds(0, 0, 323, 337);
		chattingPanel.add(chattingScroll);

//////////////////////////////////////////////////////////////////////////	

		// 탭 외 버튼
		makeRoomButton = new JButton("방 만들기");
		makeRoomButton.setFont(new Font("휴먼모음T", Font.BOLD, 11));
		makeRoomButton.setBounds(352, 93, 97, 23);
		mainPanel.add(makeRoomButton);

		leaveRoomButton = new JButton("방 나가기");
		leaveRoomButton.setFont(new Font("휴먼모음T", Font.BOLD, 12));
		leaveRoomButton.setBounds(352, 150, 97, 23);
		leaveRoomButton.setEnabled(false);
		mainPanel.add(leaveRoomButton);

		endButton = new JButton("종료");
		endButton.setFont(new Font("휴먼모음T", Font.BOLD, 12));
		endButton.setBounds(352, 398, 97, 23);
		mainPanel.add(endButton);

		setVisible(true);

	}

	private void addListener() {
		connectButton.addActionListener(this);
		sendMessageButton.addActionListener(this);
		sendNoteButton.addActionListener(this);
		joinRoomButton.addActionListener(this);
		chattingTextField.addActionListener(this);
		endButton.addActionListener(this);
		makeRoomButton.addActionListener(this);
		leaveRoomButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == connectButton) {
			System.out.println("connectButton Click");
			if (hostIPTextField.getText().length() == 0) {
				hostIPTextField.setText("IP를 입력하세요");
				hostIPTextField.requestFocus();
			} else if (serverPortTextField.getText().length() == 0) {
				serverPortTextField.setText("포트번호를 입력하세요");
				serverPortTextField.requestFocus();
			} else if (userIDTextField.getText().length() == 0) {
				userIDTextField.setText("ID를 입력하세요");
				userIDTextField.requestFocus();
			} else {
				ip = hostIPTextField.getText();
				try {
					port = Integer.parseInt(serverPortTextField.getText().trim());
				} catch (Exception e2) {
					serverPortTextField.setText("잘못 입력하였습니다.");
				}
				userId = userIDTextField.getText().trim();
				connectServer();
				setTitle("[" + userId + " ] 님 밍스톡에 오신걸 환경합니다.");
			}

		} else if (e.getSource() == sendNoteButton) {
			System.out.println("쪽지보내기버튼 Click");
			String user = (String) totalUserList.getSelectedValue();
			if (user == null) {
				JOptionPane.showMessageDialog(null, "대상을 선택하세요", "알림", JOptionPane.ERROR_MESSAGE);
			}
			String note = JOptionPane.showInputDialog("보낼메시지");
			if (note != null) {
				System.out.println("note : " + note);
				sendMessage("Note/" + user + "@" + note);
			}

		} else if (e.getSource() == sendMessageButton) {
			System.out.println("sendMessageButton Click");
		} else if (e.getSource() == joinRoomButton) {
			System.out.println("joinRoomButton Click");
		} else if (e.getSource() == makeRoomButton) {
			String roomName = JOptionPane.showInputDialog("방 이름을 입력하세요");
			if (roomName != null) {
				sendMessage("CreateRoom/" + roomName);
			}
			System.out.println("makeRoomButton Click");
		} else if (e.getSource() == leaveRoomButton) {
			System.out.println("leaveRoomButton Click");
		} else if (e.getSource() == endButton) {
			System.exit(0);
		}

	}

	private void connectServer() {
		try {
			socket = new Socket(ip, port);
			network();
		} catch (Exception e) {
		}
	}

	private void network() {
		try {
			inputStream = socket.getInputStream();
			dataInputStream = new DataInputStream(inputStream);
			outputStream = socket.getOutputStream();
			dataOutputStream = new DataOutputStream(outputStream);

			sendMessage(userId);

			userVectorList.add(userId);
			totalUserList.setListData(userVectorList);
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						try {
							String message = dataInputStream.readUTF();
							inMessage(message);
						} catch (Exception e) {
						}
					}

				}
			});
			thread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void inMessage(String str) {
		StringTokenizer stringTokenizer = new StringTokenizer(str, "/");
		String protocol = stringTokenizer.nextToken();
		String message = stringTokenizer.nextToken();

		if (protocol.equals("NewUser")) {
			userVectorList.add(message);
			totalUserList.setListData(userVectorList);

		} else if (protocol.equals("OldUser")) {
			userVectorList.add(message);
			totalUserList.setListData(userVectorList);
		} else if (protocol.equals("Note")) {
			stringTokenizer = new StringTokenizer(message, "@");
			String user = stringTokenizer.nextToken();
			String note = stringTokenizer.nextToken();
			JOptionPane.showMessageDialog(null, note, user + "로 부터 온 메시지", JOptionPane.CLOSED_OPTION);
		} else if (protocol.equals("CreateRoomFail")) {
			JOptionPane.showMessageDialog(null, "같은 방 이름이 존재합니다.", "알림", JOptionPane.ERROR_MESSAGE);
		} else if (protocol.equals("CreateRoom")) {
			myRoomName = message;
			joinRoomButton.setEnabled(false);
			leaveRoomButton.setEnabled(true);
			makeRoomButton.setEnabled(false);
		} else if (protocol.equals("NewRoom")) {
			roomVectorList.add(message);
			totalRoomList.setListData(roomVectorList);
		} else if (protocol.equals("OldRoom")) {
			roomVectorList.add(message);
			totalRoomList.setListData(roomVectorList);
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

	public static void main(String[] args) {
		new Client();
	}

}

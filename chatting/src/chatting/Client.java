package chatting;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Client extends JFrame implements ActionListener {

	// 메인 패널
	private JPanel mainPanel;

	// 로그인 탭
	private JPanel loginPanel;
	private JLabel iconLabel;
	private JLabel hostIPLabel;
	private JLabel serverPortLabel;
	private JLabel userIDLabel;
	private JTextField hostIPTextField;
	private JTextField serverPortTextField;
	private JTextField userIDTextField;
	private JButton connectButton;

	private JPanel waitingRoomPanel;
	private JLabel totalUserLabel;
	private JLabel totalRoomLabel;
	private JList totalUserList; // 전체접속자 리스트
	private JList totalRoomList; // 방 리스트
	private JButton sendNoteButton;
	private JButton joinRoomButton;
	private JButton createRoomButton;
	private JButton enterRoomButton;

	private JPanel chattingPanel;
	private JScrollPane chattingScroll;
	private JTextArea viewChatTextArea;
	private JTextField chattingTextField;
	private JButton sendMessageButton;
	private JButton leaveRoomButton;
	private JButton backButton;

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
		// mainPanel.add(waitingRoomPanel);
		addListener();
	}

	private void init() {

		// 메인 패널
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 400, 520);
		// setUndecorated(true);
		// 아이콘 설정 및 크기 조정
		ImageIcon icon = new ImageIcon("images/icon_bee.png");
		Image img = icon.getImage();
		Image scaledImg = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		icon = new ImageIcon(scaledImg);
		setIconImage(icon.getImage());

		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(null);
		mainPanel.setBackground(Color.white);
		setContentPane(mainPanel);
		setResizable(false);

		loginPanel = new JPanel();
		loginPanel.setLayout(null);
		loginPanel.setBackground(Color.white);
		loginPanel.setBounds(0, 0, 400, 482);
		mainPanel.add(loginPanel);

		iconLabel = new JLabel();
		iconLabel.setIcon(new ImageIcon("images/Icon_YouchaeTalk.png"));
		iconLabel.setLayout(null);
		iconLabel.setBounds(84, 40, 232, 232);
		loginPanel.add(iconLabel);

		hostIPLabel = new JLabel("SERVER_IP ");
		hostIPLabel.setFont(new Font("Dongle", Font.BOLD, 13));
		hostIPLabel.setForeground(new Color(15, 64, 41));
		hostIPLabel.setBounds(70, 305, 110, 15);
		loginPanel.add(hostIPLabel);

		hostIPTextField = new JTextField();
		hostIPTextField.setFont(new Font("Dongle", Font.PLAIN, 13));
		hostIPTextField.setBounds(180, 305, 150, 20);
		hostIPTextField.setColumns(10);
		loginPanel.add(hostIPTextField);

		serverPortLabel = new JLabel("SERVER_PORT");
		serverPortLabel.setFont(new Font("Dongle", Font.BOLD, 13));
		serverPortLabel.setBounds(70, 340, 110, 15);
		serverPortLabel.setForeground(new Color(15, 64, 41));
		loginPanel.add(serverPortLabel);

		serverPortTextField = new JTextField();
		serverPortTextField.setFont(new Font("Dongle", Font.PLAIN, 13));
		serverPortTextField.setBounds(180, 340, 150, 20);
		serverPortTextField.setColumns(10);
		loginPanel.add(serverPortTextField);

		userIDLabel = new JLabel("USER_ID");
		userIDLabel.setFont(new Font("Dongle", Font.BOLD, 13));
		userIDLabel.setBounds(70, 375, 110, 15);
		userIDLabel.setForeground(new Color(15, 64, 41));
		loginPanel.add(userIDLabel);

		userIDTextField = new JTextField();
		userIDTextField.setFont(new Font("Dongle", Font.PLAIN, 13));
		userIDTextField.setBounds(180, 375, 150, 20);
		userIDTextField.setColumns(10);
		loginPanel.add(userIDTextField);

		connectButton = new JButton("CONNECT");
		connectButton.setFont(new Font("Dongle", Font.BOLD, 12));
		connectButton.setBackground(new Color(251, 225, 61));
		connectButton.setForeground(new Color(15, 64, 41));
		connectButton.setBounds(140, 420, 120, 25);
		loginPanel.add(connectButton);

//			JLabel img_lbl = new JLabel("input the image");
//			img_lbl.setIcon(new ImageIcon());
//			img_lbl.setBounds(12, 213, 299, 155);
//			panel_1.add(img_lbl);

////////////////////////////////////////////////////////////////			

		waitingRoomPanel = new JPanel();
		waitingRoomPanel.setLayout(null);
		waitingRoomPanel.setBackground(new Color(247, 203, 76));
		waitingRoomPanel.setBounds(0, 0, 400, 482);

		totalUserLabel = new JLabel("전체접속자");
		totalUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalUserLabel.setFont(new Font("Dongle", Font.BOLD, 13));
		totalUserLabel.setBounds(29, 95, 102, 15);
		waitingRoomPanel.add(totalUserLabel);

		totalRoomLabel = new JLabel("방 리스트");
		totalRoomLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalRoomLabel.setFont(new Font("Dongle", Font.BOLD, 13));
		totalRoomLabel.setBounds(224, 95, 102, 15);
		waitingRoomPanel.add(totalRoomLabel);

		totalUserList = new JList();
		totalUserList.setBounds(20, 125, 120, 257);
		waitingRoomPanel.add(totalUserList);

		totalRoomList = new JList();
		totalRoomList.setBounds(180, 125, 188, 257);
		waitingRoomPanel.add(totalRoomList);

		sendNoteButton = new JButton("쪽지보내기");
		sendNoteButton.setFont(new Font("Dongle", Font.BOLD, 12));
		sendNoteButton.setBounds(29, 395, 102, 23);
		sendNoteButton.setBackground(new Color(255, 223, 136));
		sendNoteButton.setForeground(new Color(15, 64, 41));
		waitingRoomPanel.add(sendNoteButton);

		joinRoomButton = new JButton("채팅방참여");
		joinRoomButton.setFont(new Font("Dongle", Font.BOLD, 12));
		joinRoomButton.setBounds(180, 395, 102, 23);
		joinRoomButton.setBackground(new Color(255, 223, 136));
		joinRoomButton.setForeground(new Color(15, 64, 41));
		hostIPTextField.setText("127.0.0.1");
		waitingRoomPanel.add(joinRoomButton);

		enterRoomButton = new JButton("입장");
		enterRoomButton.setFont(new Font("Dongle", Font.BOLD, 12));
		enterRoomButton.setBounds(290, 395, 70, 23);
		enterRoomButton.setBackground(new Color(255, 223, 136));
		enterRoomButton.setForeground(new Color(15, 64, 41));
		waitingRoomPanel.add(enterRoomButton);

		createRoomButton = new JButton("+ 방 만들기");
		createRoomButton.setFont(new Font("Dongle", Font.BOLD, 11));
		createRoomButton.setBounds(266, 45, 102, 23);
		createRoomButton.setBackground(new Color(255, 223, 136));
		createRoomButton.setForeground(new Color(15, 64, 41));
		createRoomButton.setEnabled(false);
		waitingRoomPanel.add(createRoomButton);

//////////////////////////////////////////////////////////////////////			

		// 채팅 탭
		chattingPanel = new JPanel();
		chattingPanel.setLayout(null);
		chattingPanel.setBackground(Color.gray);
		chattingPanel.setBounds(0, 0, 400, 482);

		viewChatTextArea = new JTextArea();
		viewChatTextArea.setEnabled(false);
		viewChatTextArea.setEditable(false);
		viewChatTextArea.setFont(new Font("Dongle", Font.BOLD, 12));
		viewChatTextArea.setBounds(10, 60, 362, 380);
		chattingPanel.add(viewChatTextArea);

		chattingTextField = new JTextField();
		chattingTextField.setFont(new Font("Dongle", Font.BOLD, 11));
		chattingTextField.setBounds(10, 450, 280, 21);
		chattingTextField.setColumns(10);
		chattingPanel.add(chattingTextField);

		sendMessageButton = new JButton("전 송");
		sendMessageButton.setFont(new Font("Dongle", Font.BOLD, 12));
		sendMessageButton.setBounds(300, 450, 72, 23);
		sendMessageButton.setBackground(new Color(255, 223, 136));
		sendMessageButton.setForeground(new Color(15, 64, 41));
		chattingPanel.add(sendMessageButton);

		chattingScroll = new JScrollPane();
		chattingScroll.setEnabled(false);
		chattingPanel.add(chattingScroll);

		leaveRoomButton = new JButton("방 나가기");
		leaveRoomButton.setFont(new Font("Dongle", Font.BOLD, 11));
		leaveRoomButton.setBounds(275, 22, 97, 27);
		leaveRoomButton.setEnabled(false);
		leaveRoomButton.setBackground(new Color(255, 223, 136));
		leaveRoomButton.setForeground(new Color(15, 64, 41));
		chattingPanel.add(leaveRoomButton);

		backButton = new JButton(new ImageIcon("images/arrow.png"));
		backButton.setBounds(10, 22, 40, 40);
		// backButton.setOpaque(false);
		backButton.setBorderPainted(false);
		backButton.setContentAreaFilled(false);

		// backButton.setFocusPainted(false);
		// backButton.setBackground(new Color(1, 1, 1, 0));
		chattingPanel.add(backButton);

		setVisible(true);

	}

	private void addListener() {
		connectButton.addActionListener(this);
		sendMessageButton.addActionListener(this);
		sendNoteButton.addActionListener(this);
		joinRoomButton.addActionListener(this);
		enterRoomButton.addActionListener(this);
		chattingTextField.addActionListener(this);
		createRoomButton.addActionListener(this);
		leaveRoomButton.addActionListener(this);
		backButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				backButton.setContentAreaFilled(false);

			}

			@Override
			public void mousePressed(MouseEvent e) {
				backButton.setContentAreaFilled(true);
				backButton.setBackground(Color.red);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				backButton.setContentAreaFilled(false);

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				backButton.setContentAreaFilled(true);
				backButton.setBackground(Color.red);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				backButton.setContentAreaFilled(false);
			}

		});
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
					loginPanel.setVisible(false);
					mainPanel.add(waitingRoomPanel);
				} catch (Exception e2) {
					serverPortTextField.setText("잘못 입력하였습니다.");
				}
				userId = userIDTextField.getText().trim();
				connectServer();
				setTitle(" Welcome! [ " + userId + " ] in YouChaeTalk!!");
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
			if (chattingTextField.getText().length() == 0) {
			} else {
				sendMessage("Chatting/" + myRoomName + "/" + chattingTextField.getText());
				chattingTextField.setText("");
			}
		} else if (e.getSource() == joinRoomButton) {
			String joinRoom = (String) totalRoomList.getSelectedValue();
			leaveRoomButton.setEnabled(true);
			createRoomButton.setEnabled(false);
			sendMessage("JoinRoom/" + joinRoom);
		} else if (e.getSource() == enterRoomButton) {
			waitingRoomPanel.setVisible(false);
			mainPanel.add(chattingPanel);
		}

		else if (e.getSource() == createRoomButton) {
			String roomName = JOptionPane.showInputDialog("방 이름을 입력하세요");
			if (roomName != null) {
				sendMessage("CreateRoom/" + roomName);
			}
			System.out.println("makeRoomButton Click");
		} else if (e.getSource() == leaveRoomButton) {
			sendMessage("LeaveRoom/" + myRoomName);
			if (roomVectorList.size() != 0) {
				joinRoomButton.setEnabled(true);
			} else {
				joinRoomButton.setEnabled(false);
			}

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
		connectButton.setEnabled(false);
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
		} else if (protocol.equals("NetworkConnected")) {
			createRoomButton.setEnabled(socket.isConnected());
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
			createRoomButton.setEnabled(false);
		} else if (protocol.equals("NewRoom")) {
			roomVectorList.add(message);
			totalRoomList.setListData(roomVectorList);
		} else if (protocol.equals("OldRoom")) {
			roomVectorList.add(message);
			totalRoomList.setListData(roomVectorList);

		} else if (protocol.equals("JoinRoom")) {
			myRoomName = message;
			JOptionPane.showMessageDialog(null, "채팅방 (  " + myRoomName + " ) 에 입장완료", "알림",
					JOptionPane.INFORMATION_MESSAGE);
			joinRoomButton.setEnabled(false);
			viewChatTextArea.setText("");
		} else if (protocol.equals("Chatting")) {
			String msg = stringTokenizer.nextToken();
			viewChatTextArea.append(message + " : " + msg + "\n");
		} else if (protocol.equals("LeaveRoom")) {
			viewChatTextArea.append("*** (( " + myRoomName + "에서 퇴장 ))***\n");
			// myRoomName = null;
			createRoomButton.setEnabled(true);
			leaveRoomButton.setEnabled(false);
		} else if (protocol.equals("EmptyRoom")) {
			roomVectorList.remove(message);
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

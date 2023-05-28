package chatting;

import java.awt.Font;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;

import java.io.IOException;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.NetworkChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
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

public class Client extends JFrame implements ActionListener, KeyListener {

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
	private JPanel userListPanel;
	private JLabel totalUserLabel;
	private JLabel totalRoomLabel;
	private JScrollPane roomListScroll;
	private JList totalUserList; // 전체접속자 리스트
	private JList totalRoomList; // 방 리스트
	private JButton sendNoteButton;
	private JButton joinRoomButton;
	private JButton createRoomButton;

	private JPanel chattingPanel;
	private JScrollPane chattingScroll;
	private JTextArea viewChatTextArea;
	private JTextField chattingTextField;
	private JButton sendMessageButton;
	private JButton leaveRoomButton;
	private JButton backButton;
	private ImageIcon backbuttonImageIcon;
	private ImageIcon coloredIcon;
	
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
	private List<String> myRoomNameList = new ArrayList<String>();
	private String myCurrentRoomName = "";
	private boolean isUserIdvalidationOK = false;

	private Stack<JPanel> panelStack = new Stack<JPanel>();

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
		switchToTopPanel(loginPanel);
		// switchPanel(loginPanel);

		iconLabel = new JLabel();
		iconLabel.setIcon(new ImageIcon("images/Icon_YouchaeTalk.png"));
		iconLabel.setLayout(null);
		iconLabel.setBounds(84, 40, 232, 232);
		loginPanel.add(iconLabel);

		hostIPLabel = new JLabel("SERVER_IP ");
		hostIPLabel.setForeground(new Color(15, 64, 41));
		hostIPLabel.setBounds(70, 305, 110, 15);
		loginPanel.add(hostIPLabel);

		hostIPTextField = new JTextField();
		hostIPTextField.setBounds(180, 305, 150, 20);
		hostIPTextField.setColumns(10);
		hostIPTextField.setText("127.0.0.1");
		loginPanel.add(hostIPTextField);

		serverPortLabel = new JLabel("SERVER_PORT");
		serverPortLabel.setBounds(70, 340, 110, 15);
		serverPortLabel.setForeground(new Color(15, 64, 41));
		loginPanel.add(serverPortLabel);

		serverPortTextField = new JTextField();
		serverPortTextField.setBounds(180, 340, 150, 20);
		serverPortTextField.setColumns(10);
		loginPanel.add(serverPortTextField);

		userIDLabel = new JLabel("USER_ID");
		userIDLabel.setBounds(70, 375, 110, 15);
		userIDLabel.setForeground(new Color(15, 64, 41));
		loginPanel.add(userIDLabel);

		userIDTextField = new JTextField();
		userIDTextField.setBounds(180, 375, 150, 20);
		userIDTextField.setColumns(10);
		loginPanel.add(userIDTextField);

		connectButton = new JButton("CONNECT");
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
		waitingRoomPanel.setBackground(new Color(255, 255, 255));
		waitingRoomPanel.setBounds(0, 0, 400, 482);

		userListPanel = new JPanel();
		userListPanel.setBackground(new Color(236, 236, 236));
		userListPanel.setBounds(0, 0, 127, 480);
		waitingRoomPanel.add(userListPanel);

		totalUserLabel = new JLabel("전체접속자");
		totalUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalUserLabel.setBounds(29, 95, 102, 15);
		// waitingRoomPanel.add(totalUserLabel);

		try {
			InputStream inputStream = new BufferedInputStream(new FileInputStream("fonts/배스킨라빈스 R.ttf"));
			Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			totalUserLabel.setFont(font.deriveFont(Font.BOLD, 16f));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		totalRoomLabel = new JLabel("채팅");
		totalRoomLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalRoomLabel.setBounds(148, 63, 40, 17);
		waitingRoomPanel.add(totalRoomLabel);
		try {
			InputStream inputStream = new BufferedInputStream(new FileInputStream("fonts/배스킨라빈스 R.ttf"));
			Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			totalRoomLabel.setFont(font.deriveFont(Font.BOLD, 18f));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		totalUserList = new JList();
		totalUserList.setBounds(20, 125, 120, 257);
		// waitingRoomPanel.add(totalUserList);
		try {
			InputStream inputStream = new BufferedInputStream(new FileInputStream("fonts/배스킨라빈스 R.ttf"));
			Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			totalUserList.setFont(font.deriveFont(Font.BOLD, 14f));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		roomListScroll = new JScrollPane();
		roomListScroll.setBounds(150, 100, 230, 30);
		//roomListScroll.setBackground(new Color(0, 0, 0, 0));
		//roomListScroll.setPreferredSize(roomListScroll.getPreferredSize());
		waitingRoomPanel.add(roomListScroll);
		
		totalRoomList = new JList();
		totalRoomList.setBounds(0, 0, 200, 257);
		try {
			InputStream inputStream = new BufferedInputStream(new FileInputStream("fonts/배스킨라빈스 R.ttf"));
			Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			totalRoomList.setFont(font.deriveFont(Font.BOLD, 14f));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//totalRoomList.setLayoutOrientation(JList.VERTICAL);
		roomListScroll.add(totalRoomList);
		

		
		sendNoteButton = new JButton("쪽지보내기");
		sendNoteButton.setBounds(29, 395, 102, 23);
		sendNoteButton.setBackground(new Color(255, 223, 136));
		sendNoteButton.setForeground(new Color(15, 64, 41));
		// waitingRoomPanel.add(sendNoteButton);

		joinRoomButton = new JButton("채팅방참여");
		joinRoomButton.setBounds(180, 395, 102, 23);
		joinRoomButton.setBackground(new Color(255, 223, 136));
		joinRoomButton.setForeground(new Color(15, 64, 41));

		joinRoomButton.setEnabled(false);
		// waitingRoomPanel.add(joinRoomButton);

		createRoomButton = new JButton("+");
		createRoomButton.setBounds(330, 20, 35, 35);
		createRoomButton.setFont(new Font("Dongle", Font.BOLD, 25));
		createRoomButton.setBackground(new Color(255, 255, 255));
		createRoomButton.setForeground(new Color(69, 69, 69));
		createRoomButton.setBorder(null);
		createRoomButton.setFocusPainted(false);
		createRoomButton.setEnabled(false);
		createRoomButton.setRolloverEnabled(false);
		createRoomButton.setContentAreaFilled(false);
		waitingRoomPanel.add(createRoomButton);

//////////////////////////////////////////////////////////////////////			

		// 채팅 탭
		chattingPanel = new JPanel();
		chattingPanel.setLayout(null);
		chattingPanel.setBackground(new Color(74, 43, 0));
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
		chattingPanel.add(chattingScroll);

		leaveRoomButton = new JButton("방 나가기");
		leaveRoomButton.setFont(new Font("Dongle", Font.BOLD, 11));
		leaveRoomButton.setBounds(275, 22, 97, 27);
		leaveRoomButton.setEnabled(false);
		leaveRoomButton.setBackground(new Color(255, 223, 136));
		leaveRoomButton.setForeground(new Color(15, 64, 41));
		chattingPanel.add(leaveRoomButton);

		backbuttonImageIcon = new ImageIcon("images/arrow.png");
		coloredIcon = new ImageIcon("images/coloredArrow.png");

		backButton = new JButton(backbuttonImageIcon);

		backButton.setBounds(10, 21, 30, 30);
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
		chattingTextField.addActionListener(this);
		chattingTextField.addKeyListener(this);
		createRoomButton.addActionListener(this);
		leaveRoomButton.addActionListener(this);
		backButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				backButton.setIcon(coloredIcon);
				removeTopPanel();
				backButton.setIcon(backbuttonImageIcon);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				backButton.setIcon(coloredIcon);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				backButton.setIcon(backbuttonImageIcon);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				backButton.setIcon(coloredIcon);

			}

			@Override
			public void mouseExited(MouseEvent e) {
				backButton.setIcon(backbuttonImageIcon);
			}

		});
		totalRoomList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					if (totalRoomList.getSelectedIndex() != -1) {
						joinRoomButton.setEnabled(true); // 선택된 요소가 있으면 버튼 활성화
					} else {
						joinRoomButton.setEnabled(false); // 선택된 요소가 없으면 버튼 비활성화
					}
				}
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
				setTitle("ID를 입력하세요.");
				userIDTextField.requestFocus();
			} else {
				ip = hostIPTextField.getText();
				try {
					port = Integer.parseInt(serverPortTextField.getText().trim());
					userId = userIDTextField.getText().trim();
					try {
						connectServer();

					} catch (Exception e2) {
						setTitle("서버와의 연결이 필요합니다.");
					}
				} catch (Exception e2) {
					serverPortTextField.setText("잘못 입력하였습니다.");
				}

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
				JOptionPane.showMessageDialog(null, "메세지를 입력하세요", "알림", JOptionPane.INFORMATION_MESSAGE);
			} else {
				sendMessage("Chatting/" + myCurrentRoomName + "/" + chattingTextField.getText());
				chattingTextField.setText("");
			}
		} else if (e.getSource() == joinRoomButton) {
			String joinRoom = (String) totalRoomList.getSelectedValue();
			Boolean isAlreadyIn = false;
			switchToTopPanel(chattingPanel);
			leaveRoomButton.setEnabled(true);
			createRoomButton.setEnabled(false);
			for (String myRoom : myRoomNameList) {
				if (myRoom.equals(joinRoom)) {
					isAlreadyIn = true;
					break;
				}
			}
			if (isAlreadyIn) // 이미 들어가있는 room 이라면 EnterRoom
			{
				sendMessage("EnterRoom/" + joinRoom);
			} else { // 들어와있지 않던 room이라면 JoinRoom
				sendMessage("JoinRoom/" + joinRoom);
			}

		}

		else if (e.getSource() == createRoomButton) {
			String roomName = JOptionPane.showInputDialog("방 이름을 입력하세요");
			if (roomName != null) {
				sendMessage("CreateRoom/" + roomName);
			}
			System.out.println("makeRoomButton Click");

		} else if (e.getSource() == leaveRoomButton) {
			checkLeaveRoom();
			// sendMessage("LeaveRoom/" + myCurrentRoomName);
			if (roomVectorList.size() != 0) {
				joinRoomButton.setEnabled(true);
			} else {
				joinRoomButton.setEnabled(false);
			}

		}

	}

	private void checkLeaveRoom() {
		int result = JOptionPane.showOptionDialog(null, "정말 나가시겠습니까?", "Warning", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE, null, new String[] { "확인", "취소" }, "확인");

		if (result == JOptionPane.OK_OPTION) {
			System.out.println("okoption");
			sendMessage("LeaveRoomOK/" + myCurrentRoomName);
			myRoomNameList.remove(myCurrentRoomName);
			removeTopPanel();
			viewChatTextArea.setText("");
			createRoomButton.setEnabled(true);
			leaveRoomButton.setEnabled(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER && chattingTextField.hasFocus()) {
			if (chattingTextField.getText().length() == 0) {
			} else {
				sendMessage("Chatting/" + myCurrentRoomName + "/" + chattingTextField.getText());
				chattingTextField.setText("");
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

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
		// connectButton.setEnabled(false);
	}

	private void inMessage(String str) {
		StringTokenizer stringTokenizer = new StringTokenizer(str, "/");
		String protocol = stringTokenizer.nextToken();
		String message = stringTokenizer.nextToken();
		if (protocol.equals("UserIdValidationFailed")) {
			userIDTextField.setText("");
			isUserIdvalidationOK = false;
			connectButton.setEnabled(true);
			setTitle("이미 사용중인 USER_ID 입니다.");

		} else if (protocol.equals("NewUser")) {
			userVectorList.add(message);
			totalUserList.setListData(userVectorList);

		} else if (protocol.equals("OldUser")) {
			userVectorList.add(message);
			totalUserList.setListData(userVectorList);
		} else if (protocol.equals("NetworkConnected")) {
			isUserIdvalidationOK = true;
			if (isUserIdvalidationOK) {
				setTitle(" Welcome! [ " + userId + " ] in YouChaeTalk!!");
				switchToTopPanel(waitingRoomPanel);
				connectButton.setEnabled(false);
				userVectorList.add(userId);
				totalUserList.setListData(userVectorList);
			}
			createRoomButton.setEnabled(socket.isConnected());

		} else if (protocol.equals("Note")) {
			stringTokenizer = new StringTokenizer(message, "@");
			String user = stringTokenizer.nextToken();
			String note = stringTokenizer.nextToken();
			JOptionPane.showMessageDialog(null, note, user + "로 부터 온 메시지", JOptionPane.CLOSED_OPTION);
		} else if (protocol.equals("CreateRoomFail")) {
			JOptionPane.showMessageDialog(null, "같은 방 이름이 존재합니다.", "알림", JOptionPane.ERROR_MESSAGE);
		} else if (protocol.equals("CreateRoom")) {
			sendMessage("JoinRoom/" + message);
			switchToTopPanel(chattingPanel);
			viewChatTextArea.setText("");
			myRoomNameList.add(message);
			myCurrentRoomName = message;
			leaveRoomButton.setEnabled(true);
			createRoomButton.setEnabled(false);
		} else if (protocol.equals("NewRoom")) {
			roomVectorList.add(message);
			totalRoomList.setListData(roomVectorList);
		} else if (protocol.equals("OldRoom")) {
			roomVectorList.add(message);
			totalRoomList.setListData(roomVectorList);

		} else if (protocol.equals("JoinRoom")) {
			myCurrentRoomName = message;
			myRoomNameList.add(message);
			JOptionPane.showMessageDialog(null, "채팅방 (  " + myCurrentRoomName + " ) 에 입장완료", "알림",
					JOptionPane.INFORMATION_MESSAGE);
			// joinRoomButton.setEnabled(false);
			// viewChatTextArea.setText("");
		} else if (protocol.equals("EnterRoom")) {
			// TODO
			myCurrentRoomName = message;

		} else if (protocol.equals("Chatting")) {
			String msg = stringTokenizer.nextToken();

			viewChatTextArea.append(message + " : " + msg + "\n");
		} else if (protocol.equals("LeaveRoom")) {

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

	public void switchToTopPanel(JPanel currentPanel) {
		panelStack.push(currentPanel);
		currentPanel = panelStack.peek();
		currentPanel.setVisible(true);
		getContentPane().removeAll();
		getContentPane().add(currentPanel);
		getContentPane().validate();
		getContentPane().repaint();
	}

	private void removeTopPanel() {
		if (!panelStack.isEmpty()) {
			panelStack.pop();
		}

		getContentPane().removeAll();
		panelStack.peek().setVisible(true);
		getContentPane().add(panelStack.peek());
		getContentPane().validate();
		getContentPane().repaint();
	}

	public static void main(String[] args) {
		new Client();
	}

}

package chatting;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


public class Client extends JFrame{

	
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
		private JTextField serverportTextField;
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
		private JButton sendMessage;

		
		// 탭 외 버튼
		private JButton makeRoomButton;
		private JButton leaveRoomButton;
		private JButton endButton;
		
		
		
		public Client() {
			init();
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
			
			serverportTextField = new JTextField();
			serverportTextField.setFont(new Font("휴먼모음T", Font.BOLD, 13));
			serverportTextField.setBounds(112, 69, 199, 21);
			serverportTextField.setColumns(10);
			loginPanel.add(serverportTextField);

			
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

			totalRoomList = new JList();
			totalRoomList.setBounds(12, 69, 102, 257);
			waitingRoomPanel.add(totalRoomList);

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

			sendMessage = new JButton("전 송");
			sendMessage.setFont(new Font("휴먼모음T", Font.BOLD, 12));
			sendMessage.setBounds(226, 346, 97, 23);
			chattingPanel.add(sendMessage);

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
		
		public static void main(String[] args) {
			new Client();
		}
		
}

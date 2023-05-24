package chatting;

import java.awt.Color;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Server extends JFrame implements ActionListener  {

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
		if(portTextField.getText().length() == 0) {
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
				while(true) {
					try {
						textArea.append("사용자의 접속을 기다립니다\n");
						socket = serverSocket.accept();
						textArea.append("클라이언트 접속 성공!\n");
						System.out.println("사용자 소켓 접속" + socket);
					} catch (Exception e) {
						System.out.println("server client socket exception");
						textArea.append("서버가 중지됨! 다시 스타트 버틀을 눌러주세요\n");
						break;
					}
				}
					
			}
		});
		thread.start();
	}
	
		
	public static void main(String[] args) {
		new Server();
	}

	
}

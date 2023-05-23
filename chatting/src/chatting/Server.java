package chatting;

import java.awt.Color;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
			System.out.println("serverStartButton Click");
			} else if (e.getSource() == serverStopButton) {
				System.out.println("serverStopButton");
			}
		}
		
	
	
	public static void main(String[] args) {
		new Server();
	}

	
}

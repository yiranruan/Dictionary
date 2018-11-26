package DictionaryServer;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;

public class ServerManager {

	private JFrame frame;
	private JTextField textField;
	private ServerDictionary server = null;
	static JTextArea textArea;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerManager window = new ServerManager(args);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerManager(String[] args) {
		initialize(args);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String[] args) {
		
		frame = new JFrame();
		frame.setBounds(200, 200, 500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(10, 111, 464, 224);
		frame.getContentPane().add(textArea);
		textArea.setEditable(false);
		
		textField = new JTextField();
		textField.setBounds(182, 24, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		try {
			textField.setText(args[0]);
		} catch (Exception e) {
			textField.setText("4321");
			textArea.append("default port number used: 4321\n");
		}
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(156, 27, 46, 14);
		frame.getContentPane().add(lblPort);
		
		
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					if (btnEnter.getText().equals("Enter")) {
						server = new ServerDictionary(Integer.parseInt(textField.getText()));
						server.start();
						btnEnter.setText("Exit");
					} else if (btnEnter.getText().equals("Exit")) {
						server.saveJson();
						//server.interrupt();
						server.stopServer();
						btnEnter.setText("Enter");
					}
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, nfe.getMessage() +", Invalid", "Empty Port Error", 
							JOptionPane.ERROR_MESSAGE);
				} catch (NullPointerException npe) {
					textArea.append(npe.getMessage());
				}
				
			}
		});
		btnEnter.setBounds(302, 23, 89, 23);
		frame.getContentPane().add(btnEnter);
		
		JLabel lblLog = new JLabel("Connection Log");
		lblLog.setBounds(10, 84, 107, 14);
		frame.getContentPane().add(lblLog);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 111, 464, 224);
		frame.getContentPane().add(scrollPane);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				int count = 0;
				JLabel lblConnectionNumber = new JLabel();
				lblConnectionNumber.setBounds(319, 86, 133, 14);
				frame.getContentPane().add(lblConnectionNumber);
				while(true) {
					if (Thread.activeCount()-4<0) {
						count = 0;
					}else {
						count = Thread.activeCount()-4;
					}
					
					lblConnectionNumber.setText("Client Number: "+count);
				}
			}
		}).start();
	}
}

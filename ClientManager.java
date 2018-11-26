package DictionaryClient;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.net.*;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class ClientManager {
	
	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JLabel lblNewLabel_1;
	private ClientDictionary client;
	private JTextArea textArea;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientManager window = new ClientManager(args);
					window.frame.setVisible(true);
				} catch (Exception e) {
					System.out.println("client manager main");
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientManager(String[] args) {
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
		
		JLabel lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblSearch.setBounds(6, 55, 74, 29);
		frame.getContentPane().add(lblSearch);
		
		textField = new JTextField();
		textField.setBounds(77, 55, 130, 26);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		
		JLabel lblNewLabel = new JLabel("Result");
		lblNewLabel.setBounds(6, 93, 61, 16);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblHostip = new JLabel("HostIP");
		lblHostip.setBounds(6, 28, 46, 16);
		frame.getContentPane().add(lblHostip);
		
		textField_1 = new JTextField();
		try {
			textField_1.setText(args[0]);
		} catch (Exception e) {
			textField_1.setText("4321");
		}
		textField_1.setBounds(51, 23, 130, 26);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(191, 28, 25, 16);
		frame.getContentPane().add(lblPort);
		
		textField_2 = new JTextField();
		try {
			textField_2.setText(args[0]);
		} catch (Exception e) {
			textField_2.setText("127.0.0.1");
		}
		textField_2.setBounds(226, 23, 130, 26);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		
		
		JButton btnConnect = new JButton("connect");
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if(btnConnect.getText().equals("connect")) {
						String hostIP = textField_1.getText();
						int port = Integer.parseInt(textField_2.getText());
						client = new ClientDictionary(hostIP,port);
						textField_1.setEditable(false);
						textField_2.setEditable(false);
						btnConnect.setText("disconnect");
						lblNewLabel_1.setText("Connection established");
					}else if (btnConnect.getText().equals("disconnect")) {
						btnConnect.setText("connect");
						textField_1.setEditable(true);
						textField_2.setEditable(true);
						lblNewLabel_1.setText("Connection abrupted...");
						client.clientClose();
					}
				} catch (ConnectException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Connect Error", 
							JOptionPane.ERROR_MESSAGE);
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage()+"; invalid", "Port Error", 
							JOptionPane.ERROR_MESSAGE);
				} catch (NullPointerException e3) {
					JOptionPane.showMessageDialog(null, "Server is not accessible, please try again later!",
							"Access Error",JOptionPane.ERROR_MESSAGE);
				} catch (Exception e4) {
					System.out.println("connect button");
					e4.printStackTrace();
				}
				
			}
		});
		btnConnect.setBounds(366, 22, 117, 29);
		frame.getContentPane().add(btnConnect);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setAutoscrolls(true);
		textArea.setBounds(7, 120, 467, 209);
		textArea.setEditable(false);
		frame.getContentPane().add(textArea);
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { 
				switch (btnEnter.getText()) {
				case "Search":
					try {
						String searchWord = textField.getText();
						textArea.setText(client.search(searchWord));
					} catch (NullPointerException se1) {
						JOptionPane.showMessageDialog(null, "Please input valid word",
								"Error",JOptionPane.ERROR_MESSAGE);
					} catch (ConnectException se3) {
						btnConnect.setText("connect");
						textField_1.setEditable(true);
						textField_2.setEditable(true);
						lblNewLabel_1.setText("Connection abrupted...");
						client.clientClose();
					} catch (Exception se2) {
						JOptionPane.showMessageDialog(null, se2.getMessage(), "Error", 
								JOptionPane.ERROR_MESSAGE);//?????????????????????????
					}
					break;
				
				case "Add":
					try {
						String addWord = textField.getText();
						String wordDefinition = textArea.getText();
						if (addWord.equals("")) throw new ArrayIndexOutOfBoundsException("Empty Input For word!");
						if (wordDefinition.equals("")) throw new ArrayIndexOutOfBoundsException("Empty Input For Definition!");
						String info = client.add(addWord, wordDefinition);
						lblNewLabel_1.setText("Add "+info);
					} catch (ConnectException ae1) {
						btnConnect.setText("connect");
						textField_1.setEditable(true);
						textField_2.setEditable(true);
						lblNewLabel_1.setText("Connection abrupted...");
						client.clientClose();
					} catch (ArrayIndexOutOfBoundsException ae2){
						JOptionPane.showMessageDialog(null, ae2.getMessage(), "Input Error", 
								JOptionPane.ERROR_MESSAGE);
					} catch (Exception ae3) {
						JOptionPane.showMessageDialog(null, "IO Error!", "Error", 
								JOptionPane.ERROR_MESSAGE);
					}
					break;
					
				case "Remove":
					try {
						String removeWord = textField.getText();
						if (removeWord.equals("")) throw new Exception("Empty Input For word!");
						String wordDefinition = client.search(removeWord);
						if (wordDefinition.equals("Sorry, no definition found."))
							throw new Exception("No Exist Word");
						int option = JOptionPane.showConfirmDialog(null, 
								"Are you sure to remove this word?","sure?",
								JOptionPane.YES_NO_OPTION);
						if (option == JOptionPane.YES_OPTION) {
							String info = client.remove(removeWord, wordDefinition);
							lblNewLabel_1.setText("Remove "+info);
						}
					} catch (ConnectException re) {
						btnConnect.setText("connect");
						textField_1.setEditable(true);
						textField_2.setEditable(true);
						lblNewLabel_1.setText("Connection abrupted...");
						client.clientClose();
					}catch (Exception re2) {
						JOptionPane.showMessageDialog(null, re2.getMessage(),
								"Error",JOptionPane.ERROR_MESSAGE);
					}
					break;
				
				case "Edit":
					try {
						String editWord = textField.getText();
						if (editWord.equals("")) throw new ArrayIndexOutOfBoundsException("Empty Input For word!");
						String wordDefinition = client.search(editWord);
						if (wordDefinition.equals("Sorry, no definition found."))
							throw new Exception("No Exist Word");
						String userDefinition = textArea.getText();
						if (wordDefinition.equals("")) throw new ArrayIndexOutOfBoundsException("Empty Input For Definition!");
						String info = client.remove(editWord, wordDefinition);
						info = client.add(editWord, userDefinition);
						lblNewLabel_1.setText("Edit "+info);
					} catch (ArrayIndexOutOfBoundsException ee1){
						JOptionPane.showMessageDialog(null, ee1.getMessage(), "Input Error", 
								JOptionPane.ERROR_MESSAGE);
					} catch (Exception ee2) {
						JOptionPane.showMessageDialog(null, "Invalid Input",
								"Error",JOptionPane.ERROR_MESSAGE);
					} 
					break;
					
				case "Enter":
					JOptionPane.showMessageDialog(null, "Please choose one model", "Model Error", 
							JOptionPane.ERROR_MESSAGE);
					break;
				}
			}
		});
		btnEnter.setBounds(219, 55, 117, 29);
		frame.getContentPane().add(btnEnter);
		
		lblNewLabel_1 = new JLabel("Silence");
		lblNewLabel_1.setBounds(6, 340, 346, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 132, 22);
		frame.getContentPane().add(menuBar);
		
		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		
		JMenuItem mntmConnect = new JMenuItem("Connect");
		mntmConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String hostIP = JOptionPane.showInputDialog("Please input HostIP Address");
				String port = JOptionPane.showInputDialog("Please input port Address");
				textField_1.setText(hostIP);
				textField_2.setText(port);
			}
		});
		mnMenu.add(mntmConnect);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int option = JOptionPane.showConfirmDialog(mntmExit, "Confirm to Exit?");
				if (option == 0) {
					System.exit(0);
				}
			}
		});
		mnMenu.add(mntmExit);
		
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					switch ((String) arg0.getItem()) {
					case "Search Word":
						textArea.setEditable(false);
						btnEnter.setText("Search");
						break;
					case "Add New Word":
						if (textArea.getText().equals("Sorry, no definition found.")) {
							int option = JOptionPane.showConfirmDialog(mntmExit, "Define this word?","Define?",
									JOptionPane.YES_NO_OPTION);
							if (option == JOptionPane.NO_OPTION) {
								textField.setText("");
							}
						} else {
							textField.setText("");
						}
						textArea.setText("");
						textArea.setEditable(true);
						btnEnter.setText("Add");
						textArea.setEditable(true);
						break;
					case "Edit Word":
						btnEnter.setText("Edit");
						if (!textField.getText().equals("")) {
							int option = JOptionPane.showConfirmDialog(mntmExit, 
									"Edit this word?","Edit?",
									JOptionPane.YES_NO_OPTION);
							if (option == JOptionPane.NO_OPTION) {
								textField.setText("");
								textArea.setText("");
							}
						} else {
							textArea.setText("");
						}
						textArea.setEditable(true);
						break;
					case "Remove Word":
						textArea.setEditable(false);
						btnEnter.setText("Remove");
						break;
					default:
						textArea.setEditable(false);
						btnEnter.setText("Enter");
						break;
						
					}
				}
			}
		});
		comboBox.setBounds(351, 62, 110, 22);
		frame.getContentPane().add(comboBox);
		comboBox.addItem("---");
		comboBox.addItem("Search Word");
		comboBox.addItem("Add New Word");
		comboBox.addItem("Edit Word");
		comboBox.addItem("Remove Word");
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 111, 464, 224);
		frame.getContentPane().add(scrollPane);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}
}

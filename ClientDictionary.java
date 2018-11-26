package DictionaryClient;

import java.net.*;

import javax.swing.JOptionPane;

import java.io.*;

public class ClientDictionary {
	private final String hostIP;
	private final int port;
	private Socket client;
	BufferedReader heartReader;
	BufferedWriter heartWriter;
	
	public ClientDictionary(String hostIP, int port) throws ConnectException {
		this.hostIP = hostIP;
		this.port = port;
		try {
			this.client = new Socket(this.hostIP, this.port);
			System.out.println("Connection with server established");
		}catch (Exception e) {
			System.out.println(e.getMessage());
			throw new ConnectException(e.getMessage());
		}
		
	}

    public String search(String word) throws IOException, ConnectException {
    	String msg = "Sorry, no result.";
        try {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
        	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
        	if (word.equals("")) throw new IOException("Empty Input!");
        	writer.write("search,"+word+"\n");
        	writer.flush();
        	msg = reader.readLine();
        	return msg;
        }catch (IOException e) {
        	JOptionPane.showMessageDialog(null, e.getMessage(), "Search Error", 
					JOptionPane.ERROR_MESSAGE);
        	System.out.println("socket end");
        	this.client.close();
        	throw new ConnectException("Connect broken, refuse");
        }
    }
    
    public String add(String word, String definition) throws IOException, ConnectException {
    	String msg;
        try {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
        	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
        	if (word.equals("")) throw new IOException("Empty Input!");
        	writer.write("add,"+word+","+definition+"\n");
        	writer.flush();
        	msg = reader.readLine();
        	return msg;
        }catch (IOException e) {
        	JOptionPane.showMessageDialog(null, e.getMessage(), "Add Error", 
					JOptionPane.ERROR_MESSAGE);
        	System.out.println("socket end");
        	this.client.close();
        	throw new ConnectException("Connect broken, refuse");
        }
    }
    
    public String remove(String word, String definition) throws IOException {
    	String msg;
        try {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
        	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
        	if (word.equals("")) throw new IOException("Empty Input!");
        	writer.write("remove,"+word+","+definition+"\n");
        	writer.flush();
        	msg = reader.readLine();
        	return msg;
        }catch (IOException e) {
        	JOptionPane.showMessageDialog(null, e.getMessage(), "Remove Error", 
					JOptionPane.ERROR_MESSAGE);
        	System.out.println("socket end");
        	this.client.close();
        	throw new ConnectException("Connect broken, refuse");
        }
    }
    
    public void heartBeats() throws NullPointerException{
    	try {
    		heartReader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
    		heartWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
    		heartReader.readLine();
    		heartWriter.write("connection test");
    	} catch (Exception e) {
    		throw new NullPointerException("Scoket is terminated. Please Contact Server Administrator.");
    	}
    }
    
    public void clientClose() {
    	try {
    		this.client.close();
    		System.out.println("abrupt");
		}catch(IOException e) {
			e.printStackTrace();
		}
    }

}
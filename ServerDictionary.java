package DictionaryServer;
import java.io.*;
import java.net.*;

public class ServerDictionary extends Thread{
	private Dictionary dict = new Dictionary();
	private int port;
	private ServerSocket server = null;
	private int clientNum = 0;
	public ServerDictionary (int port) {
		this.port = port;
		
	}
	
    public void run() throws NullPointerException{
    	
        try {
        	dict.readJson();
        	server = new ServerSocket(port);
            ServerManager.textArea.append(Thread.currentThread().getName() + 
					" - Server listening on port "+port+" for a connection\n");

            while (!isInterrupted())
            {
                Socket client = server.accept();
                ServerManager.textArea.append(Thread.currentThread().getName() 
						+ " - Client conection accepted\n");
                clientNum++;
                ServerManager.textArea.append("one client is connecting\n");
                ServerManager.textArea.append(clientNum+" is connecting\n");
                ServerThreads thread = new ServerThreads(client, dict);
                thread.setName("Thread" + clientNum);
                thread.start();
            }
        } catch (Exception ex2){
        		ServerManager.textArea.append(ex2.getMessage()+"\n");
        } finally {
			try {
				server.close();
			} catch (IOException e) {
				ServerManager.textArea.append(e.getMessage()+"\n");
			} catch (NullPointerException ex) {
				throw new NullPointerException(ex.getMessage());
			}
		}
    }
    
    public void saveJson() {
    	dict.writeInFile();
    }
    
    public void stopServer() {
    	try {
			server.close();
			ServerManager.textArea.append("Server is closed. See you next time\n");
			clientNum = 0;
		} catch (IOException e) {
			ServerManager.textArea.append(e.getMessage()+"\n");
		}
    }

	public int getClientNum() {
		return clientNum;
	}

}
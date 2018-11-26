package DictionaryServer;
import java.io.*;
import java.net.*;

class ServerThreads extends Thread {
    private Dictionary dict;
    private Socket client;
    BufferedReader reader;
    BufferedWriter writer;

    public ServerThreads(Socket client, Dictionary dict) {
        try{
            this.client = client;
            this.dict = dict;
            
        } catch (Exception e){
        	System.out.println("client socket");
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String msg = null;
            String dictMsg = "Pipe Complete";
            reader = new BufferedReader(new InputStreamReader(client.getInputStream(),"UTF-8"));
            writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"UTF-8"));
            while ((msg = reader.readLine()) != null) {
        		String[] command = msg.split(",");
        		switch (command[0]) {
        			case "search":
        				dictMsg = dict.search(command[1]);
        				break;
        			case "add":
        				dictMsg = dict.add(command[1],command[2]);
        				break;
        			case "remove":
        				dictMsg = dict.remove(command[1],command[2]);
        			default:
        				break;
        		}
        		writer.write(dictMsg+"\n");
				writer.flush();
            }
        } catch (SocketException ex) {
        	try {
				reader.close();
				writer.close();
	        	client.close();
			} catch (IOException e) {
				
			} 
        } catch (IOException ioe) {
        	
        } catch (Exception er) {
        	
        }
        
    }
}
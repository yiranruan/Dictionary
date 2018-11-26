package DictionaryServer;
import org.json.*;
import java.io.*;
class Dictionary {
	private JSONArray json;
	public void readJson() {
		StringBuilder jsonStr = new StringBuilder();
		try{
 			BufferedReader reader = new BufferedReader(new FileReader(new File("dicts.json")));
 			String temp = "";
			while((temp = reader.readLine())!= null){
				jsonStr.append(temp);
			}
			reader.close();
			json = new JSONArray(jsonStr.toString());
				
		}catch (Exception ex){
			ServerManager.textArea.append("DictionaryReadJson: "+ex.getMessage()+"\n");
		}
		
	}
	
	public String search(String aimWord){
		String definition = "Sorry, no definition found.";
		String word;
		try{
			for(int i=0;i<json.length();i++){
				JSONObject wordObj = (JSONObject) json.get(i);
				word = (String) wordObj.get("word");
				if(word.equals(aimWord.toUpperCase())) {
					definition = (String)wordObj.get("definition");
					break;
				}
			}
		}catch(Exception ex){
			ServerManager.textArea.append("DictionarySearch: "+ex.getMessage()+"\n");
		}
		return definition;
	}
	
	public synchronized String add(String word, String text){
		try{
			JSONObject member = new JSONObject();
			member.put("definition",text);
			member.put("word",word.toUpperCase());
			json.put(member);
			return "Successed";
		}catch(Exception ex){
			ServerManager.textArea.append("DictionaryAdd: "+ex.getMessage()+"\n");
			return "Failed";
		}
	}
	
	public synchronized String remove(String word, String text) {
		try{
			int index;
			for(index=0;index<json.length();index++){
				JSONObject wordObj = (JSONObject) json.get(index);
				if (wordObj.get("word").equals(word.toUpperCase())||
						wordObj.get("definition").equals(text)) break;
			}
			json.remove(index);
			return "Successed";
		}catch(Exception ex){
			ServerManager.textArea.append("DictionaryRemove: "+ex.getMessage()+"\n");
			return "Failed";
		}
	}
	
	public void writeInFile() {
		String jsonStr = json.toString();
		try{
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File("dicts.json"))));
			writer.write(jsonStr);
			writer.close();
			ServerManager.textArea.append("DictionaryWriteInFile: write in json file successed\n");
		} catch(Exception ex) {
			ServerManager.textArea.append("DictionaryWriteInFile: "+ex.getMessage()+"\n");
		}
	}
	
}
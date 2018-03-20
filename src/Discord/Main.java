package Discord;

import java.util.Vector;

import Discord.API.BotListener;
import Discord.API.DiscordTemp;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

public class Main {

	public static void main(String[] args) {
		try {
			loadConfigFromFile(Constants.CONFIG_PATH);
			
			DataManager dm = DataManager.Instance();
			dm.dbconnector = new DB_Connector();
			dm.dbconnector.connect();
			
			DataManager.Instance().getDataFromDatabase();
		    
			IDiscordClient client = DiscordTemp.createClient(DataManager.Instance().bot_token, false);
			EventDispatcher dispatcher = client.getDispatcher();
			dispatcher.registerListener(new BotListener());
			
			client.login();
		
		} catch (Exception e) {
			IO.printToConsole("\nCRITICAL EXCEPTION! TERMINATING PROGRAM\n");
			IO.printToConsole(e.getMessage());
			if(e.getCause() != null)
				IO.printToConsole(e.getCause().getMessage());
		}
	}

	@SuppressWarnings("unused")
	private static void loadConfigFromFile(String path) {
		Vector<String> lines = IO.readFile(path);
		for(String line:lines) {
			String parName = line.substring(0,line.indexOf(":"));
			String parVal = line.substring(line.indexOf(":")+1,line.length()).replaceAll(" ", "");
			
			if(parName.equals(Constants.CFG_db_string)) DataManager.Instance().database_string = parVal;
			else if(parName.equals(Constants.CFG_live_token) && !Constants.TEST) DataManager.Instance().bot_token = parVal;	
			else if(parName.equals(Constants.CFG_live_id) && !Constants.TEST) DataManager.Instance().bot_id = Long.parseLong(parVal);
			else if(parName.equals(Constants.CFG_test_token) && Constants.TEST) DataManager.Instance().bot_token = parVal;	
			else if(parName.equals(Constants.CFG_test_id) && Constants.TEST) DataManager.Instance().bot_id = Long.parseLong(parVal);
		}
	}
	
}

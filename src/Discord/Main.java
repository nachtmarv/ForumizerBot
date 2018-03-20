package Discord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
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
		
		try {
			File file = new File("bot.properties");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();
			
			if (properties.containsKey(Constants.CFG_db_string)) DataManager.Instance().database_string = properties.getProperty(Constants.CFG_db_string);
			if(!Constants.TEST) {
				if (properties.containsKey(Constants.CFG_live_token)) DataManager.Instance().bot_token = properties.getProperty(Constants.CFG_live_token);
				if (properties.containsKey(Constants.CFG_live_id)) DataManager.Instance().bot_id = Long.parseLong(properties.getProperty(Constants.CFG_live_id));
			} else {
				if (properties.containsKey(Constants.CFG_test_token)) DataManager.Instance().bot_token = properties.getProperty(Constants.CFG_test_token);
				if (properties.containsKey(Constants.CFG_test_id)) DataManager.Instance().bot_id = Long.parseLong(properties.getProperty(Constants.CFG_test_id));
			}
/*
			Enumeration enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);
				if(key.equals(Constants.CFG_db_string)) DataManager.Instance().database_string = value;
				else if(key.equals(Constants.CFG_live_token) && !Constants.TEST) DataManager.Instance().bot_token = value;	
				else if(key.equals(Constants.CFG_live_id) && !Constants.TEST) DataManager.Instance().bot_id = Long.parseLong(value);
				else if(key.equals(Constants.CFG_test_token) && Constants.TEST) DataManager.Instance().bot_token = value;	
				else if(key.equals(Constants.CFG_test_id) && Constants.TEST) DataManager.Instance().bot_id = Long.parseLong(value);
			}
			*/
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

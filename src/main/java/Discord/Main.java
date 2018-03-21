package Discord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import Discord.API.BotListener;
import Discord.API.DiscordTemp;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;

public class Main {

	public static void main(String[] args) {
		try {
			loadConfigFromFile(Constants.CONFIG_PATH);
			
			DataManager dm = DataManager.Instance();
			dm.dbconnector = new DB_Connector();
			dm.dbconnector.connect();
			
			boolean result = DataManager.Instance().getDataFromDatabase();
		    if(!result) {
		    	IO.printToConsole("Data could not be fetched from database. \nTerminating program.");
		    	System.exit(2);
		    }
		    	
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

	/**
	 * Loads all variables from the config file and inserts them into the DataManager instance
	 * @param config path
	 */
	@SuppressWarnings("unused")
	private static void loadConfigFromFile(String path) {
		
		try {
			File file = new File(path);
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();
			
			if (properties.containsKey(Constants.CFG_db_string)) 
				DataManager.Instance().database_string = properties.getProperty(Constants.CFG_db_string);
			if (properties.containsKey(Constants.CFG_token)) 
				DataManager.Instance().bot_token = properties.getProperty(Constants.CFG_token);
			if (properties.containsKey(Constants.CFG_id)) 
				DataManager.Instance().bot_id = Long.parseLong(properties.getProperty(Constants.CFG_id));
			if (properties.containsKey(Constants.CFG_prefix)) 
				DataManager.Instance().bot_prefix = properties.getProperty(Constants.CFG_prefix);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

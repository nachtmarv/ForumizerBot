package Discord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import Discord.API.BotListener;
import Discord.API.DiscordTemp;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import util.Pair;

public class ForumizerMain {

	public static void main(String[] args) {
		try {
			loadConfigFromFile(Constants.CONFIG_PATH);
		    	
			DataManager.Instance().client = DiscordTemp.createClient(DataManager.Instance().bot_token, false);
			EventDispatcher dispatcher = DataManager.Instance().client.getDispatcher();
			dispatcher.registerListener(new BotListener());
			
			DataManager.Instance().client.login();
		
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
	private static void loadConfigFromFile(String path) {
		
		try {
			File file = new File(path);
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();
			
			//Load token & prefix
			if (properties.containsKey(Constants.CFG_token)) 
				DataManager.Instance().bot_token = properties.getProperty(Constants.CFG_token);
			if (properties.containsKey(Constants.CFG_prefix)) 
				DataManager.Instance().bot_prefix = properties.getProperty(Constants.CFG_prefix);
			
			//Load message mover configuration
			int mmCounter = 1;
			Map<Long,Long> mmConnections = new HashMap<Long,Long>();
			while(properties.containsKey(Constants.CFG_mm_prefix+mmCounter+Constants.CFG_mm_emojiId)) {
				long emojiId = Long.parseLong(properties.getProperty(Constants.CFG_mm_prefix+mmCounter+Constants.CFG_mm_emojiId));
				long channelId = Long.parseLong(properties.getProperty(Constants.CFG_mm_prefix+mmCounter+Constants.CFG_mm_channelId));
				mmConnections.put(emojiId, channelId);
				mmCounter ++;
			}
			DataManager.Instance().messageMoveConfigConnections = mmConnections;
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void convertConfig(IDiscordClient client) {
		Map<Long, Pair<IChannel,IGuild>> mmResolvedConnections = new HashMap<Long, Pair<IChannel,IGuild>>();
		for(Map.Entry<Long, Long> entry : DataManager.Instance().messageMoveConfigConnections.entrySet()) {
			IChannel channel = client.getChannelByID(entry.getValue());
			IGuild guild = channel.getGuild();
			mmResolvedConnections.put(entry.getKey(), new Pair<IChannel,IGuild>(channel,guild));
		}
		DataManager.Instance().messageMoveConnections = mmResolvedConnections;
	}
	
}

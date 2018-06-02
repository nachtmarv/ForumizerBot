package Discord;
import java.util.Map;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import util.Pair;

public class DataManager {
	private static DataManager instance = null;
	/**
	 * Returns the DataManager instance.
	 * @return DataManager instance
	 */
	public static DataManager Instance() {
		if(instance == null) instance = new DataManager();
		return instance;
	}
	
	public IDiscordClient client;
	public int debugLevel = 3;
	public String bot_prefix = "%";
	public String bot_token = "";
	public Map<Long, Long> messageMoveConfigConnections;
	public Map<Long, Pair<IChannel, IGuild>> messageMoveConnections;
	
	
	
	
}

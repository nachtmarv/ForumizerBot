package Discord;

public class Constants {
	
	public static final String CONFIG_PATH = "bot.properties";
	
	public static final String CFG_db_string = "db_string";
	public static final String CFG_token = "bot_token";
	public static final String CFG_id = "bot_id";
	public static final String CFG_prefix = "bot_prefix";
	
	public static final int CONN_TRIES = 10;
	public static final int CONN_TRY_SLEEP = 2000; 
	public static final int CONN_CHECK_TIMEOUT = 5000; 
	public static final int MAX_EMOJIS_PER_CHANNEL = 30;
	public static final String DELETE_EMOJI = "\u2702";
	public static final String OKAY_EMOJI = "\u2705";
	public static final String NOTOKAY_EMOJI = "\u274E";
	public static final String LOGFILE_NAME = "log.txt";
	public static final String POLL_ADDITION = "'s poll";
	public static final String REACTION_X_NAME = "sentinel_x";
	public static final long REACTION_X_ID = 423974983862190080L;
	public static final String REACTION_CHECK_NAME = "sentinel_check";
	public static final long REACTION_CHECK_ID = 423974674607767572L;
	public static final String REACTION_CHECK = "<:sentinel_check:423974674607767572>";
	public static final String REACTION_X = "<:sentinel_x:423974983862190080>";
	
}

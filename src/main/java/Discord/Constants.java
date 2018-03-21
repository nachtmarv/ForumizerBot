package Discord;

public class Constants {
	
	// Path for the config file
	public static final String CONFIG_PATH = "bot.properties";
	// Path of the log file
	public static final String LOGFILE_NAME = "log.txt";
		
	// Database string with address, port and credentials
	public static final String CFG_db_string = "db_string";
	// Token for the discord bot
	public static final String CFG_token = "bot_token";
	// Id of the discord bot
	public static final String CFG_id = "bot_id";
	// Prefix to be used for all bot commands
	public static final String CFG_prefix = "bot_prefix";
	
	// Number of connection retries attempted when connection to database is lost
	public static final int CONN_TRIES = 10;
	// Duration to wait between connection retries when connection to database is lost
	public static final int CONN_TRY_SLEEP = 2000; 
	// Timeout of each connection retry
	public static final int CONN_CHECK_TIMEOUT = 5000; 
	// Maximum allowed number of emoji-role binds per channel
	public static final int MAX_EMOJIS_PER_CHANNEL = 30;
	
	// String every poll message has as an identifier in the author name.
	public static final String POLL_ADDITION = "'s poll";
		
	// Unicode emoji used to delete all reactions on role management messages.
	public static final String DELETE_EMOJI = "\u2702";
	// Name of the X reaction used on messages
	public static final String REACTION_X_NAME = "sentinel_x";
	// Id of the X reaction used on messages
	public static final long REACTION_X_ID = 423974983862190080L;
	// Name of the checkmark reaction used on messages
	public static final String REACTION_CHECK_NAME = "sentinel_check";
	// Id of the checkmark reaction used on messages
	public static final long REACTION_CHECK_ID = 423974674607767572L;
	// Complete, pasteable string of the X reaction used by the bot
	public static final String REACTION_X = "<:sentinel_x:423974983862190080>";
	// Complete, pasteable string of the checkmark reaction used by the bot
	public static final String REACTION_CHECK = "<:sentinel_check:423974674607767572>";
	
	
}

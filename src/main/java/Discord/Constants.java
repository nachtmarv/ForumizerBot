package Discord;

import sx.blah.discord.handle.impl.obj.ReactionEmoji;

public class Constants {
	
	// Path for the config file
	public static final String CONFIG_PATH = "bot.properties";
	// Path of the log file
	public static final String LOGFILE_NAME = "log.txt";
		
	// Token for the discord bot
	public static final String CFG_token = "bot_token";
	// Prefix to be used for all bot commands
	public static final String CFG_prefix = "bot_prefix";
	
	// 
	public static final String CFG_mm_prefix = "mm_";
	// 
	public static final String CFG_mm_emojiId = "_emojiId";
	// 
	public static final String CFG_mm_channelId = "_channelId";
	
	// Number of connection retries attempted when connection to database is lost
	public static final int CONN_TRIES = 10;
	// Duration to wait between connection retries when connection to database is lost
	public static final int CONN_TRY_SLEEP = 2000; 
	// Timeout of each connection retry
	public static final int CONN_CHECK_TIMEOUT = 5000; 
	// Maximum allowed number of emoji-role binds per channel
	public static final int MAX_EMOJIS_PER_CHANNEL = 30;
	
	// String every poll message has as an identifier in the author name.
	public static final String POLL_ADDITION = " asks:";
		
	// Unicode emoji used to delete all reactions on role management messages.
	public static final String DELETE_EMOJI = "\u2702";
	// Unicode emoji used to initiate poll evaluation on poll messages.
	public static final String POLLEVAL_EMOJI = "\u23F0";
	// Unicode emoji used to delete polls.
	public static final String POLLDELETE_EMOJI = "\u2702";
	// Unicode checkmark emoji used
	public static final String REACTION_CHECK = "\u2705";
	// Unicode 'no' and 'X' emoji used
	public static final String REACTION_X = "\u274C";
	// Integer representation of the evaluation emoji. needs more than 16 bit, so we have to convert it at execution to string
	public static final int REACTION_POLLEVAL_INT = 0x1f501;
	// Emoji object of the evaluation emoji. is created at the start of the program.
	public static ReactionEmoji REACTION_POLLEVAL_EMOJI = create_POLLEVAL_EMOJI();
	// Color used in poll embeds
	public static final int[] POLL_EMBED_COLORS = {50,50,250};
	// URI used for metadata in embeds. Changing this invalidates all previous metadata.
	public static final String URI_FAKE = "http://fake.url/index?";
	
	
	private static ReactionEmoji create_POLLEVAL_EMOJI() {
		String s = new StringBuilder().appendCodePoint(Constants.REACTION_POLLEVAL_INT).toString();
		return ReactionEmoji.of(s);
	}
}

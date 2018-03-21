package Discord;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class DataManager {
	public int debugLevel = 3;
	public Set<Long> channelSet = null; 
	public DB_Connector dbconnector = null;
	Map<Long,Vector<BoundEmoji>> boundEmojis = null;
	public long sentinelId = 0;
	
	public String bot_prefix = "%";
	public String database_string;
	public String bot_token = "";
	public long bot_id = 0L;
	private static DataManager instance = null;
	
	
	/**
	 * Returns the DataManager instance.
	 * @return DataManager instance
	 */
	public static DataManager Instance() {
		if(instance == null) instance = new DataManager();
		return instance;
	}
	
	/**
	 * Fetches all data from the database.
	 * @return true if successful
	 */
	public boolean getDataFromDatabase() {
		try {
			channelSet = dbconnector.getChannelIDs();
			boundEmojis = dbconnector.getBoundEmojis();
		} catch (SQLException e) {
			IO.printToConsole("CRITICAL ERROR: Could not get data from database. terminating...");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if a channel is an rmc channel
	 * @param channelId
	 * @return true if it is an rmc channel, false if it isn't
	 */
	public boolean isValidChannel(Long id) {
		if(channelSet.contains(id)) return true;
		return false;
	}
	
	/**
	 * Adds a channel to the rmc channel list.
	 * First tries to insert into the database, then manipulates the local list.
	 * @param id
	 * @return
	 */
	public boolean addChannel(Long id) {
		boolean result = dbconnector.addChannelId(id);
		if(result) {
			channelSet.add(id);
			return true;
		}
		return false;
	}
	
	/**
	 * Removes a channel from list of rmc channels and deletes all their emoji-role bindings.
	 * First tries to delete from the database, then manipulates the local list.
	 * @param channelId
	 * @return true if successful
	 */
	public boolean removeChannel(Long channelId) {
		boolean result = dbconnector.deleteChannelId(channelId);
		if(result) {
			dbconnector.removeAllEmojisOfChannel(channelId);
			channelSet.remove(channelId);
			boundEmojis.remove(channelId);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Checks if an emoji has a role bound to it in a given channel
	 * @param channelId
	 * @param isUnicode true if the character is a unicode emoji, false if custom
	 * @param unicodeString The string of the unicode emoji. Ignored if isUnicode is false.
	 * @param emojiId The ID of the custom emoji. Ignored if isUnicode is true.
	 * @return ID of the bound role or 0 if no role was found
	 */
	public long checkBoundEmoji(long channelId, boolean isUnicode, String unicodeString, long emojiId) {
		if(!boundEmojis.containsKey(channelId)) return 0;
		Vector<BoundEmoji> emojiVec = boundEmojis.get(channelId);
		if(isUnicode) {
			for(BoundEmoji bm:emojiVec) {
				if(bm.unicodeString.equals(unicodeString)) return bm.roleId;
			}
		} else {
			for(BoundEmoji bm:emojiVec) {
				if(bm.emojiId == emojiId) return bm.roleId;
			}
		}
		return 0;
	}
	
	/**
	 * Adds an emoji entry to the dataset.
	 * First tries to edit the database, then edits the local representation.
	 * @param channelId
	 * @param isUnicode
	 * @param unicodeString
	 * @param emojiId
	 * @param roleId
	 * @return 0 if everything is ok, 1 if problem with db connection, 2 if max limit reached.
	 */
	public int addBoundEmoji(long channelId, boolean isUnicode, String unicodeString, long emojiId, long roleId) {
		// if it already exists, do nothing
		if(checkBoundEmoji(channelId, isUnicode, unicodeString, emojiId) != 0) return 0;
		
		Vector<BoundEmoji> emojiVec = boundEmojis.get(channelId);
		if(emojiVec != null)
			if(emojiVec.size() > Constants.MAX_EMOJIS_PER_CHANNEL)
				return 2;
		
		// Try adding to database, quit if it fails
		boolean result = dbconnector.addEmoji(channelId, isUnicode, unicodeString, emojiId, roleId);
		if(!result) {
			IO.printToConsole("ERROR: Could not add bound emoji to database");
			return 1;
		}
		
		
		boolean nullflag = false;
		if(emojiVec == null) {
			emojiVec = new Vector<BoundEmoji>();
			nullflag = true;
		}
		
		emojiVec.addElement(new BoundEmoji(channelId, isUnicode, unicodeString, emojiId, roleId));
		if(nullflag) boundEmojis.put(channelId, emojiVec);
		
		return 0;
	}
	
	/**
	 * Removes an emoji entry from the dataset.
	 * First tries to edit the database, then edits the local representation.
	 * @param channelId
	 * @param isUnicode
	 * @param unicodeString
	 * @param emojiId
	 * @return 0 if no problems, 1 if something went wrong.
	 */
	public int removeBoundEmoji(long channelId, boolean isUnicode, String unicodeString, long emojiId) {
		// if it doesn't exist, do nothing
		if(checkBoundEmoji(channelId, isUnicode, unicodeString, emojiId) == 0) return 0;
		
		// Try deleting from database, quit if it fails
		boolean result = dbconnector.removeEmoji(channelId, isUnicode, unicodeString, emojiId);
		if(!result) {
			IO.printToConsole("ERROR: Could not remove bound emoji to database");
			return 1;
		}
		
		Vector<BoundEmoji> emojiVec = boundEmojis.get(channelId);
		if(emojiVec == null) return 0;
		
		if(isUnicode) {
			for(int i=0;i<emojiVec.size();++i) {
				if(emojiVec.get(i).unicodeString.equals(unicodeString)) {
					emojiVec.remove(i);
					return 0;
				}
			}
		} else {
			for(int i=0;i<emojiVec.size();++i) {
				if(emojiVec.get(i).emojiId == emojiId) {
					emojiVec.remove(i);
					return 0;
				}
			}
		}
		
		return 0;
	}
}

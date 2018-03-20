package Discord;

public class BoundEmoji {
	public long channelId;
	public boolean isUnicode;
	public String unicodeString;
	public long emojiId;
	public long roleId;
	
	public BoundEmoji(long channelId, boolean isUnicode, String unicodeString, long emojiId, long roleId) {
		this.channelId = channelId;
		this.isUnicode = isUnicode;
		this.unicodeString = unicodeString;
		this.emojiId = emojiId;
		this.roleId = roleId;
	}
}

package Discord.API;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Discord.Constants;
import Discord.DataManager;
import Discord.IO;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IEmbed;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

public class ReactionProcessor {
	public static void processReaction(IUser user, IMessage message, IChannel channel, IReaction reaction, boolean added) {
		// Filter reactions from bots
		if(user.isBot()) return;
		
		// Filter messages from this bot for poll reactions
		if(message.getAuthor().getLongID() == DataManager.Instance().bot_id) {
			List<IEmbed> embeds = message.getEmbeds();
			if(embeds.isEmpty()) return;
			if(embeds.get(0).getAuthor().getName().contains(Constants.POLL_ADDITION)) {
				processPollReaction(user, message, channel, reaction, added, embeds.get(0));
			}
			return;
		}
		
		// Execute role management routine
		processRMC(user, message, channel, reaction, added);
		
	}
	
	public static void processPollReaction(IUser user, IMessage message, IChannel channel, IReaction reaction, boolean added, IEmbed embed) {
		// Filter for poll evaluation emoji
		if(!reaction.getEmoji().equals(Constants.REACTION_POLLEVAL_EMOJI)) return;
		
		if(added) {
			ServerInteractions.removeReactionFromMessage(message, user, Constants.REACTION_POLLEVAL_EMOJI);
			return;
		}
		
		// get all voters for both reactions (custom reactions, Id stored in Constants)
	    IReaction reactionCheck = message.getReactionByUnicode(Constants.REACTION_CHECK);
	    IReaction reactionX = message.getReactionByUnicode(Constants.REACTION_X);
	    
	    //Check whether there are no voters. returns sometimes null
	    if(reactionCheck == null || reactionX == null) {
	    	String s = "";
	    	if(reactionCheck == null) s += "c";
	    	if(reactionX == null) s += "x";
	    	IO.printToConsole("POLL_REACTION: REACTIONS NULL [" + s + "]");
	    	return;
	    }
	    
	    
	    
	    Map<Long,IUser> usersYes = new HashMap<Long,IUser>();
	    Map<Long,IUser> usersNo = new HashMap<Long,IUser>();
	    
	    //put the users in a map if they are not bots. 
	    //could be done another way but it remained after some experiments.
	    for(IUser u:reactionX.getUsers()) {
	    	if(!u.isBot())
	    		usersNo.put(u.getLongID(), u);
		}
	    for(IUser u:reactionCheck.getUsers()) {
	    	if(!u.isBot() && !usersNo.containsKey(u.getLongID()))
	    		usersYes.put(u.getLongID(), u);
		}
	    
	    
	    // Build strings for voters
	    String peopleYes = "";
	    String peopleNo = "";
	    
	    for(Map.Entry<Long, IUser> u:usersYes.entrySet())
	    	peopleYes = peopleYes + u.getValue().getDisplayName(message.getGuild()) + "\n";
	    for(Map.Entry<Long, IUser> u:usersNo.entrySet())
	    	peopleNo = peopleNo + u.getValue().getDisplayName(message.getGuild()) + "\n";
	    
	    if(usersYes.isEmpty()) peopleYes = "-----";
	    if(usersNo.isEmpty()) peopleNo = "-----";
	    
	    //Creating embed object
	    EmbedBuilder builder = new EmbedBuilder();
		builder.withAuthorName(embed.getAuthor().getName());
		
		builder.withColor(50, 50, 250);
	    builder.withDescription(embed.getDescription());
	    builder.appendField(Constants.REACTION_CHECK, 
	    		peopleYes, true);
	    builder.appendField(Constants.REACTION_X, 
	    		peopleNo, true);
	    //builder.withFooterText(embed.getFooter().getText());
	    
	    ServerInteractions.editEmbedInMessage(message, builder.build());
	}
	
	public static void processRMC(IUser user, IMessage message, IChannel channel, IReaction reaction, boolean added) {
		// Filter wrong channels
		if(!DataManager.Instance().isValidChannel(channel.getLongID())) return;
		
		long channelId = channel.getLongID();
		long emojiId = reaction.getEmoji().getLongID();
		boolean isUnicode = false;
		String unicodeString = reaction.getEmoji().getName();
		if(emojiId == 0)
			isUnicode = true;
		
		// check deletion emoji
		if(unicodeString.equals(Constants.DELETE_EMOJI)) {
			RequestBuffer.request(() -> {
				message.removeAllReactions();
			});
			return;
		}
		
		long roleId = DataManager.Instance().checkBoundEmoji(channelId, isUnicode, unicodeString, emojiId);
		
		// skip unbound emoji
		if(roleId == 0) return;
		
		// If first addition, in other words setup:
		if(added && reaction.getCount() <= 1) {
			ServerInteractions.addReactionToMessage(message, reaction);
			return;
		}
		
		if(added) {
			RequestBuffer.request(() -> {
				user.addRole(message.getGuild().getRoleByID(roleId));
			});
			return;
		} else {
			RequestBuffer.request(() -> {
				user.removeRole(message.getGuild().getRoleByID(roleId));
			});
			return;
		}
	}
}

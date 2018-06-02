package Discord.API;

import java.security.Permissions;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Discord.Constants;
import Discord.DataManager;
import Discord.IO;
import Wrappers.EmbedWrapper;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IEmbed;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;
import util.Pair;

public class ReactionProcessor {
	public static void processReaction(IUser user, IMessage message, IChannel channel, IReaction reaction, boolean added) {
		// Filter reactions from bots
		if(user.isBot()) return;
		
		processMessageMoving(user, message, channel, reaction, added);
	}
	
	public static void processMessageMoving(IUser user, IMessage message, IChannel channel, IReaction reaction, boolean added) {
		Pair<IChannel,IGuild> pair = DataManager.Instance().messageMoveConnections.get(reaction.getEmoji().getLongID());
		if(pair == null) return;
		if(channel.getGuild().getLongID() != pair.second.getLongID()) return;
		if(!user.getPermissionsForGuild(pair.second).contains(sx.blah.discord.handle.obj.Permissions.MANAGE_MESSAGES)) return;
		
		String answer = "__"+message.getAuthor().mention(true)+" sagte:__ (Verschoben von " + user.getDisplayName(pair.second) + ")\n"+message.getContent();
		ServerInteractions.sendMessageInChannel(pair.first, answer);
		
		/*
		EmbedBuilder builder = new EmbedBuilder();
		builder.withAuthorIcon(message.getAuthor().getAvatarURL());
		LocalDateTime mTime = LocalDateTime.ofInstant(message.getTimestamp(), ZoneOffset.UTC);
		builder.withAuthorName(message.getAuthor().getDisplayName(message.getGuild()));
		builder.withDescription(message.getContent());
		builder.withFooterText("Verschoben aus " + channel.getName() + " von " +user.getDisplayName(channel.getGuild()));
		ServerInteractions.sendEmbedInChannel(pair.first, builder.build());
		*/
		
		ServerInteractions.deleteMessage(message);
	}
	
	public static void processPollReaction(EmbedWrapper embed, IUser user, IMessage message, IChannel channel, IReaction reaction, boolean added) {
		/*
		// Filter for poll evaluation emoji
		if(!reaction.getEmoji().equals(Constants.REACTION_POLLEVAL_EMOJI) && !reaction.getEmoji().getName().equals(Constants.POLLDELETE_EMOJI)) return;
		
		// Delete message if poll creator reacted with the delete emoji.
		if(reaction.getEmoji().getName().equals(Constants.POLLDELETE_EMOJI)) {
			if(user.getLongID() == embed.getMetadata().uid) {
				ServerInteractions.deleteMessage(message);
				return;
			}
		}
		
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
	    
	    Map<Long,IUser> usersYes = new LinkedHashMap<Long,IUser>();
	    Map<Long,IUser> usersNo = new LinkedHashMap<Long,IUser>();
	    
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
	    
	    EmbedObject newEmbed = embed.createNewPollEmbedFromPrevious(usersYes, usersNo, message.getGuild());
	    
	    ServerInteractions.editEmbedInMessage(message, newEmbed);
	    */
	}
	
	public static void processRMC(IUser user, IMessage message, IChannel channel, IReaction reaction, boolean added) {
		/*
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
		*/
	}
}

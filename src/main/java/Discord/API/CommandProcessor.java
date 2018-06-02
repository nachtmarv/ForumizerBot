package Discord.API;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import Discord.Constants;
import Discord.DataManager;
import Discord.IO;
import Discord.Lang;
import Wrappers.EmbedWrapper;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;

public class CommandProcessor {
	
	public static void processCommand(IMessage message, String prefix) {
		if(message.getAuthor().isBot()) return;
		
		String[] command = message.getContent().toLowerCase().replaceFirst(prefix, "").split(" ");
		
		if(command[0].equals("ping")) {
			ServerInteractions.sendMessageInChannel(message.getChannel(),"pong");
		} else if(command[0].equals("help")) {
			handleHelp(message);
		}
	}
	
	private static void handleHelp(IMessage message) {
		ServerInteractions.sendEmbedsToUser(message.getAuthor(), Lang.help_embeds);
		ServerInteractions.addReactionToMessage(message, ReactionEmoji.of(Constants.REACTION_CHECK));
	}
	
	private static void handlePoll(IMessage message, String[] command) {
		/*
		String content = message.getContent().replaceFirst(DataManager.Instance().bot_prefix+command[0]+" ", "");
		String contentCopy = content;
		Vector<String> pars = new Vector<String>();
		while(contentCopy.indexOf("\"") != -1) {
			int firstParenth = contentCopy.indexOf("\"");
			int secondParenth = contentCopy.indexOf("\"",firstParenth+1);
			if(secondParenth != -1) {
				pars.addElement(contentCopy.substring(firstParenth+1,secondParenth));
				contentCopy = contentCopy.substring(secondParenth+1,contentCopy.length());
			} else {
				pars.addElement(contentCopy.substring(firstParenth+1));
				break;
			}
		}
		
		if(pars.isEmpty()) return;
		
		EmbedObject embed = null;
		if(pars.size()==1)
			embed = EmbedWrapper.CreateFirstPollMessage(message.getAuthor(), message.getGuild(), pars.elementAt(0), EmbedWrapper.POLLTYPE_default);
		if(pars.size()==2) {
			if(pars.elementAt(0).equals(""))
				Collections.swap(pars, 0, 1);
			embed = EmbedWrapper.CreateFirstPollMessage(message.getAuthor(), message.getGuild(), pars.elementAt(1), EmbedWrapper.POLLTYPE_default, pars.elementAt(0));
		}
		if(embed == null) return;
		IMessage answer = ServerInteractions.sendEmbedInChannel(message.getChannel(), embed);
		
		boolean result = ServerInteractions.addReactionToMessage(answer, ReactionEmoji.of(Constants.REACTION_CHECK));
		if(result) ServerInteractions.addReactionToMessage(answer, ReactionEmoji.of(Constants.REACTION_X));
		ServerInteractions.addReactionToMessage(answer, Constants.REACTION_POLLEVAL_EMOJI);
		
		ServerInteractions.deleteMessage(message);
		*/
	}
	
	private static void handleRMC(IMessage message, String[] command) {
		/*
		IUser sender = message.getAuthor();
		IChannel channel = message.getChannel();
		IGuild guild = message.getGuild();
		
		if(!sender.getPermissionsForGuild(guild).contains(sx.blah.discord.handle.obj.Permissions.ADMINISTRATOR)) {
			ServerInteractions.sendMessageInChannel(channel,"You need to be administrator to do that.");
			return;
		}
		if(command.length>1) {
			if(command[1].equals("remove")) { 
				// Remove case
				boolean result = DataManager.Instance().removeChannel(channel.getLongID());
				if(result) ServerInteractions.sendMessageInChannel(channel,"Unmarked channel as role management channel.");
				else ServerInteractions.sendMessageInChannel(channel,"Encountered an error. please inform bot developer.");
				return;
			} else if(command[1].equals("add")) { 
				// add case
				boolean result = DataManager.Instance().addChannel(channel.getLongID());
				if(result) ServerInteractions.sendMessageInChannel(channel,"Marked channel as role management channel.");
				else ServerInteractions.sendMessageInChannel(channel,"Encountered an error. please inform bot developer.");
				return;
			} else if(command[1].equals("bind") || command[1].equals("unbind")) {
				// bind / unbind case
				if(!DataManager.Instance().isValidChannel(channel.getLongID()))
					ServerInteractions.sendMessageInChannel(channel,"This channel is not marked as a role management channel. No emoji assignment possible.");
				else
					doBindUnbind(message, command);
				return;
			} else if(command[1].equals("check")) {
				// bind / unbind case
				boolean result = DataManager.Instance().isValidChannel(channel.getLongID());
				if(result) ServerInteractions.sendMessageInChannel(channel,"This channel IS marked as a role management channel.");
				else ServerInteractions.sendMessageInChannel(channel,"This channel is NOT marked as a role management channel.");
				return;
			} 
		}
		
		channel.sendMessage("please use rmc with parameters "
				+ "'add' 'remove' 'bind' 'unbind' 'check'"
				+ "\ntype "+DataManager.Instance().bot_prefix+"help for additional info.");
		*/
	}
	
	private static void doBindUnbind(IMessage message, String[] command) {
		/*
		IChannel channel = message.getChannel();
		
		List<IRole> roles = message.getRoleMentions();
		if(roles.size()!=1) {
			ServerInteractions.sendMessageInChannel(channel,"The message has to contain exactly 1 role.");
		}
		
		if(command.length != 4) {
			ServerInteractions.sendMessageInChannel(channel,"Paramter requirement not met. Please use the help command.");
			return;
		}
		String emoji = command[3];
		
		long channelId = channel.getLongID();
		boolean isUnicode = true;
		String unicodeString = "";
		long emojiId = 0;
		String emojiName = "";
		long roleId = 0;
		
		if(emoji.startsWith("<")) isUnicode = false;
		if(isUnicode) unicodeString = emoji;
		else {
			try {
				emojiId = getCustomEmojiId(emoji);
				emojiName = getCustomEmojiName(emoji);
			} catch(Exception e) {
				ServerInteractions.sendMessageInChannel(channel,"Something is wrong with the parameters, please check them.");
				return;
			}
		}
		roleId = roles.get(0).getLongID();
		
		boolean emojitest = false;
		if(isUnicode) {
			try {
				final String temp = new String(unicodeString);
				emojitest = RequestBuffer.request(() -> {
					message.addReaction(ReactionEmoji.of(temp));
					return true;
				}).get();
			} catch(Exception e) {
				IO.printToConsoleDebug(e.getMessage(), 3);
			}
		} else {
			try {
				final Long temp = new Long(emojiId);
				final String temp2 = new String(emojiName);
				emojitest = RequestBuffer.request(() -> {
					message.addReaction(ReactionEmoji.of(temp2,temp));
					return true;
				}).get();
			} catch(Exception e) {
				IO.printToConsoleDebug(e.getMessage(), 3);
			}
		}
		
		if(emojitest) {
			int result = 1;
			if(command[1].equals("bind")) {
				result = DataManager.Instance().addBoundEmoji(channelId, isUnicode, unicodeString, emojiId, roleId);
			} else {
				result = DataManager.Instance().removeBoundEmoji(channelId, isUnicode, unicodeString, emojiId);
			}
			if(result == 0) {
				ServerInteractions.addReactionToMessage(message, ReactionEmoji.of(Constants.REACTION_CHECK));
				return;
			}
			if(result == 2) {
				ServerInteractions.sendMessageInChannel(channel, "This channel already has the maximum number of emojis assigned. ("+Constants.MAX_EMOJIS_PER_CHANNEL+")");
				return;
			}
		}
		ServerInteractions.addReactionToMessage(message, ReactionEmoji.of(Constants.REACTION_X));
		*/
	}
	
	private static long getCustomEmojiId(String s) {
		String[] splitted = s.split(":");
		String idstring = splitted[splitted.length-1].replace(">", "");
		long result = Long.parseLong(idstring);
		return result;
	}
	
	private static String getCustomEmojiName(String s) {
		String[] splitted = s.split(":");
		String name = splitted[1];
		return name;
	}
}

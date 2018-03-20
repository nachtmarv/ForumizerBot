package Discord.API;

import Discord.Constants;
import Discord.IO;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionRemoveEvent;
import sx.blah.discord.handle.obj.IMessage;

public class BotListener {
	
	
	@EventSubscriber
	public void onMessageEvent(MessageReceivedEvent event) {
		if(event.getMessage().getContent().toLowerCase().startsWith(Constants.CMD_PREFIX)) {
			CommandProcessor.processCommand(event.getMessage(), Constants.CMD_PREFIX);
		}
	}
	
	@EventSubscriber
	public void onReactionAddEvent(ReactionAddEvent event) {
		ReactionProcessor.processReaction(event.getUser(),event.getMessage(), event.getChannel(), event.getReaction(), true);
	}
	
	@EventSubscriber
	public void onReactionRemoveEvent(ReactionRemoveEvent event) {
		ReactionProcessor.processReaction(event.getUser(),event.getMessage(), event.getChannel(), event.getReaction(), false);
	}
	/*
	public void test(ReactionAddEvent event) {
		IO.printToConsole(event.getReaction().getEmoji().getName());
		String temp = event.getReaction().getEmoji().getName();
		int unicode = temp.codePointAt(0);
		IO.printToConsole(unicode);
		String new1 = new String(Character.toChars(unicode));
		IO.printToConsole(new1);
		IO.printToConsole(event.getReaction().getEmoji().getStringID());
		IO.printToConsole(event.getReaction().getEmoji().isUnicode());
	}
	
	public void test(MessageReceivedEvent event) {
		IMessage message = event.getMessage();
		IO.printToConsole(message.getContent());
	}*/
}

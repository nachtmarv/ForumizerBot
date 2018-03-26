package Discord.API;

import Discord.DataManager;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionRemoveEvent;

/**
 * Handles every discord event subscription
 * 
 * @author Marvin Weisbrod
 */
public class BotListener {
	
	@EventSubscriber
	public void onMessageEvent(MessageReceivedEvent event) {
		if(event.getMessage().getContent().toLowerCase().startsWith(DataManager.Instance().bot_prefix)) {
			CommandProcessor.processCommand(event.getMessage(), DataManager.Instance().bot_prefix);
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
}

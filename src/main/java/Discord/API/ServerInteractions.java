package Discord.API;

import java.util.Vector;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.impl.obj.Webhook;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IWebhook;
import sx.blah.discord.util.RequestBuffer;

public class ServerInteractions {
	public static void sendMessageInChannel(IChannel channel, String message) {
		RequestBuffer.request(() -> {
			channel.sendMessage(message);
		});
	}
	
	public static IMessage sendEmbedInChannel(IChannel channel, EmbedObject embed) {
		IMessage result = RequestBuffer.request(() -> {
			IMessage res = channel.sendMessage(embed);
			return res;
		}).get();
		return result;
	}
	
	public static void removeReactionFromMessage(IMessage message, IUser user, ReactionEmoji r) {
		RequestBuffer.request(() -> {
			message.removeReaction(user, r);
		});
	}
	
	public static void sendEmbedsInChannel(IChannel channel, Vector<EmbedObject> embeds) {
		//IWebhook test = new Webhook(null, null, 0, channel, null, null, null);
		for(EmbedObject embed:embeds)
			sendEmbedInChannel(channel, embed);
	}
	
	public static void editEmbedInMessage(IMessage message,  EmbedObject embed) {
		RequestBuffer.request(() -> {
			message.edit(embed);
		});
	}
	
	public static boolean addReactionToMessage(IMessage message, ReactionEmoji reaction) {
		
		boolean result = RequestBuffer.request(() -> {
			message.addReaction(reaction);
			return true;
		}).get();
		return true;
	}
	
	public static void addReactionToMessage(IMessage message, IReaction reaction) {
		RequestBuffer.request(() -> {
			message.addReaction(reaction);
		});
	}
	
	public static void deleteMessage(IMessage message) {
		RequestBuffer.request(() -> {
			message.delete();
		});
	}
	
}

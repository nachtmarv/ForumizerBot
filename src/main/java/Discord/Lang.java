package Discord;

import java.util.Vector;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

public class Lang {
	public static Vector<EmbedObject> help_embeds = make_help_embeds();
	
	
	public static EmbedObject make_embed_rmc() {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withAuthorName("rmc <action>");
		
		builder.withColor(230, 20, 20);
		builder.withTitle("[administrator]");
	    //builder.withDesc("withDesc");
	    builder.withDescription("Command for everything regarding role management channels (rmc's)."
	    		+ "After binding a role to an emoji in a channel, you can react to a message in this channel with the emoji. "
	    		+ "\nThe bot will then also react with that emoji and from that point, if someone adds the reaction (clicking on it),"
	    		+ "\nthey gain the bound role. If they remove the reaction, they lose the role. "
	    		+ "\nIf it does not work, check if the bot has the rights to manage the specific role. "
	    		+ "\nIf you want to remove all reactions from a message, react with "+Constants.DELETE_EMOJI
	    		+ "\nWe *strongly suggest* only using role bindings in channels where *adding new reactions* is *restricted to admins*.");
	    
	    builder.appendField("rmc add", 
	    		"Marks channel as role management channel.", true);
	    builder.appendField("rmc remove", 
	    		"Unmarks channel as role management channel & deletes all bound emojis for that channel.", true);
	    builder.appendField("rmc bind <role> <emoji>", 
	    		"Binds a role to an emoji."
	    		+ "The bot will add a tick if everything went well."
	    		+ "You can only bind emojis the bot has access to."
	    		+ "We advise against using "+Constants.DELETE_EMOJI+", since it is used to clear all reations."
	    		+ "Every channel is limited to "+Constants.MAX_EMOJIS_PER_CHANNEL+" role bindings.", false);
	    builder.appendField("rmc unbind <role> <emoji>", 
	    		"Unbinds a role from an emoji."
	    		+ "The bot will add a tick if everything went well.", true);
		
	    return builder.build();
	}
	
	public static EmbedObject make_embed_ping() {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withAuthorName("ping");
		
		builder.withColor(20, 200, 20);
		builder.withTitle("[everyone]");
	    builder.withDescription("The bot will reply \"pong\"");
	    
	    return builder.build();
	}
	
	public static EmbedObject make_embed_poll() {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withAuthorName("poll <message>");
		
		builder.withColor(20, 200, 20);
		builder.withTitle("[everyone]");
	    builder.withDescription("The bot creates a poll with your message and adds reactions for 'yes' and 'no'."
	    		+ "\nUsers can add their answer like a normal poll. If someone then reacts with the third preadded emoji"
	    		+ ", The bot will edit the names of everyone who voted into the message for easier viewing."
	    		+ "\nIf somebody voted with yes and no, the bot will treat it as a no.");
	    
	    return builder.build();
	}
	
	public static Vector<EmbedObject> make_help_embeds() {
		Vector<EmbedObject> allEmbeds = new Vector<EmbedObject>();
		allEmbeds.add(make_embed_rmc());
		allEmbeds.add(make_embed_ping());
		allEmbeds.add(make_embed_poll());
		return allEmbeds;
	}
	
	
	
	
}

package Discord;

import Wrappers.EmbedWrapper;

public class URI_Handler {
	public static enum URI_Type {NONE, POLL};
	private static final String var_type = "type";
	private static final String var_uid = "uid";
	private static final String var_extraInt = "extraInt";
	private static final String type_poll = "poll";
	private static final String type_help = "help";
	
	
	
	public static String createFakeUri_Poll(Long uid, int polltype) {
		String fullUri = Constants.URI_FAKE + var_type + "=" + type_poll 
				+ "&" + var_uid + "=" + uid 
				+ "&" + var_extraInt + "=" + polltype;
		return fullUri;
	}
	
	public static String createFakeUri_Help() {
		String fullUri = Constants.URI_FAKE + var_type + "=" + type_help;
		return fullUri;
	}
	
	public static EmbedWrapper.Metadata getFakeUriContent(String uri) {
		EmbedWrapper.Metadata content = new EmbedWrapper.Metadata();
		if(uri == null) {
			content.type = EmbedWrapper.Metadata.Type.NONE;
			return content;
		}
		
		String pars = uri.replace(Constants.URI_FAKE, "");
		String[] parameters = pars.split("&");
		try {
			for(String par:parameters) {
				if(par.startsWith(var_type)) {
					String[] split = par.split("=");
					if(split.length != 2)
						content.type = EmbedWrapper.Metadata.Type.NONE;
					if(split[1].equals(type_poll))
						content.type = EmbedWrapper.Metadata.Type.POLL;
				}
				if(par.startsWith(var_uid)) {
					String[] split = par.split("=");
					if(split.length == 2)
						content.uid = Long.parseLong(split[1]);
				}
				if(par.startsWith(var_extraInt)) {
					String[] split = par.split("=");
					if(split.length == 2)
						content.extraInt = Integer.parseInt(split[1]);
				}
			}
		} catch(NumberFormatException e) {
			IO.printToConsole(e.getMessage());
			content.type = EmbedWrapper.Metadata.Type.NONE;
		}
		return content;
	}
}

package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.RequestContext;

public class BgColorWorker implements IWebWorker{

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String color;
		if((color=context.getParameter("bgcolor"))!=null) {
			if(color.trim().length()!=6) {
				context.setPersistentParameter("message", "Color was not set! Color should be formed as AB12CD ");
				context.removePersistentParameter("bgcolor");
			}else {
				context.setPersistentParameter("bgcolor", "#"+color.trim());
				context.setPersistentParameter("message", "Color succesfully set to #"+color.trim());
			}
		}else {
			context.setPersistentParameter("message", "Color was not set!"+color);
		}
		context.getDispatcher().dispatchRequest("\\private\\pages\\color.smscr");
		
	}

}

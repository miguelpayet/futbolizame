package pe.trazos.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

import javax.servlet.http.HttpServletRequest;

public class LogRequestListener implements IRequestCycleListener {

	public static Logger log = LogManager.getLogger(LogRequestListener.class);

	public LogRequestListener() {
	}

	@Override
	public void onBeginRequest(RequestCycle cycle) {
		Request req = cycle.getRequest();
		HttpServletRequest servletReq = (HttpServletRequest) req.getContainerRequest();

	}

	@Override
	public void onDetach(RequestCycle cycle) {
	}

	@Override
	public void onEndRequest(RequestCycle cycle) {

	}

	@Override
	public IRequestHandler onException(RequestCycle cycle, Exception ex) {
		return null;
	}

	@Override
	public void onExceptionRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler, Exception exception) {
	}

	@Override
	public void onRequestHandlerExecuted(RequestCycle cycle, IRequestHandler handler) {
	}

	@Override
	public void onRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler) {
	}

	@Override
	public void onRequestHandlerScheduled(RequestCycle cycle, IRequestHandler handler) {
	}

	@Override
	public void onUrlMapped(RequestCycle cycle, IRequestHandler handler, Url url) {
	}

}

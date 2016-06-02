package pe.trazos.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import pe.trazos.dao.HibernateUtil;

public class HibernateRequestListener implements IRequestCycleListener {

	public static Logger log = LogManager.getLogger(HibernateRequestListener.class);

	private SessionFactory factoria;

	public HibernateRequestListener() {
		factoria = HibernateUtil.getSessionFactory();
	}

	@Override
	public void onBeginRequest(RequestCycle cycle) {
		log.debug("onBeginRequest");
		Session sesion = factoria.getCurrentSession();
		sesion.beginTransaction();
	}

	@Override
	public void onDetach(RequestCycle cycle) {

	}

	@Override
	public void onEndRequest(RequestCycle cycle) {
		log.debug("onEndRequest");
		Session sesion = factoria.getCurrentSession();
		try {
			sesion.getTransaction().commit();
		} catch (Throwable ex) {
			try {
				if (sesion.getTransaction().getStatus().equals(TransactionStatus.ACTIVE)) {
					sesion.getTransaction().rollback();
				}
			} catch (Throwable rbEx) {
				log.error("could not rollback after exception!", rbEx);
				rbEx.printStackTrace();
			}
		}
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
		//log.debug("onUrlMapped {}", url.toString());
	}

}

package pe.trazos.web;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.dao.HibernateUtil;

import javax.servlet.*;
import java.io.IOException;

public class FutbolizameHibernateFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(FutbolizameHibernateFilter.class);

	@Override
	public void destroy() {
		logger.info("FutbolizameHibernateFilter.destroy");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session sesion = sf.getCurrentSession();
		try {
			logger.debug("FutbolizameHibernateFilter.doFilter.beginTransaction");
			sesion.beginTransaction();
			chain.doFilter(request, response);
			logger.debug("FutbolizameHibernateFilter.doFilter.commit");
			sesion.getTransaction().commit();
		} catch (Throwable ex) {
			try {
				if (sesion.getTransaction().getStatus().equals(TransactionStatus.ACTIVE)) {
					sesion.getTransaction().rollback();
				}
			} catch (Throwable rbEx) {
				logger.error("Could not rollback after exception!", rbEx);
				rbEx.printStackTrace();
			}
			throw new ServletException(ex);
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("FutbolizameHibernateFilter.init");
	}

}

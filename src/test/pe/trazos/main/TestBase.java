package pe.trazos.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import pe.trazos.dao.HibernateUtil;

public class TestBase {

	protected Session sesion;

	Session createSession() {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		return sf.getCurrentSession();
	}

}

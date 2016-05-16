package pe.trazos.wicket;

import org.apache.wicket.util.tester.WicketTester;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import pe.trazos.dao.HibernateUtil;
import pe.trazos.web.FutbolizameApplication;

abstract class AbstractHorasTest {

	private SessionFactory sf;
	WicketTester tester;

	@After
	public void cerrarSesionHibernate() {
		sf.getCurrentSession().getTransaction().commit();
	}

	@Before
	public void iniciarSesionHibernate() {
		sf = HibernateUtil.getSessionFactory();
		sf.getCurrentSession().beginTransaction();
	}

	@Before
	public void setUp() {
		tester = new WicketTester(new FutbolizameApplication());
	}

}

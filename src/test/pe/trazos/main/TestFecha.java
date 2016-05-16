package pe.trazos.main;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.dao.DaoFecha;
import pe.trazos.dao.HibernateUtil;
import pe.trazos.dominio.Fecha;

import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;

public class TestFecha extends TestBase {
	private final Logger logger = LoggerFactory.getLogger(TestPronostico.class);

	@After
	public void endTransaction() {
		sesion.getTransaction().commit();
	}

	@Test
	public void leerFechasFuturas() {
		DaoFecha df = new DaoFecha();
		List<Fecha> fechas = df.getFuturo(new Date());
		assertNotNull(fechas);
		Assert.assertNotEquals(fechas.size(), 0);
	}

	@Before
	public void startTransaction() {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		sesion = sf.getCurrentSession();
		sesion.beginTransaction();
	}
}

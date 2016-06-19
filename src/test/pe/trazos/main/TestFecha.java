package pe.trazos.main;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.dao.DaoCompetencia;
import pe.trazos.dao.DaoFecha;
import pe.trazos.dao.HibernateUtil;
import pe.trazos.dominio.Competencia;
import pe.trazos.dominio.Fecha;

import java.util.Date;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;

public class TestFecha extends TestBase {

	private static final Logger log = LoggerFactory.getLogger(TestPronostico.class);

	@After
	public void endTransaction() {
		sesion.getTransaction().commit();
	}

	@Test
	public void leerFechasAnteriores() {
		DaoFecha df = new DaoFecha();
		Fecha f = df.get(307);
		Assert.assertNotNull(f);
		if (f != null) {
			Set<Fecha> fechas = df.listarAnteriores(f);
			for (Fecha fl : fechas) {
				log.info(fl.toString());
			}
		}
	}

	@Test
	public void leerFechasFuturas() {
		DaoCompetencia dc = new DaoCompetencia();
		Competencia c = dc.get(1);
		Assert.assertNotNull(c);
		if (c != null) {
			DaoFecha df = new DaoFecha();
			Set<Fecha> fechas = df.listarFuturas(c, new Date());
			assertNotNull(fechas);
			Assert.assertNotEquals(fechas.size(), 0);
			for (Fecha fl : fechas) {
				log.info(fl.toString());
			}
		}
	}

	@Before
	public void startTransaction() {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		sesion = sf.getCurrentSession();
		sesion.beginTransaction();
	}

}

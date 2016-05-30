package pe.trazos.main;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.dao.DaoFecha;
import pe.trazos.dao.DaoPronostico;
import pe.trazos.dao.DaoVisitante;
import pe.trazos.dao.HibernateUtil;
import pe.trazos.dominio.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestPronostico extends TestBase {

	private final Logger logger = LoggerFactory.getLogger(TestPronostico.class);

	@After
	public void endTransaction() {
		sesion.getTransaction().commit();
	}

	@Test
	public void leerPronosticosFecha() {
		DaoVisitante dv = new DaoVisitante();
		Visitante v = dv.get("10208978070830655");
		assertNotNull(v);
		DaoFecha df = new DaoFecha();
		Fecha f = df.get(307);
		assertNotNull(f);
		DaoPronostico dp = new DaoPronostico();
		Map<Participacion, Pronostico> pronosticos = dp.obtenerPronosticosFecha(v, f);
		for (Pronostico p : pronosticos.values()) {
			logger.info(String.format("%s - %s", p.getParticipacion().getEquipo().getNombre(), p.getGoles()));
			Assert.assertNotNull(p.getGoles());
		}
	}

	@Test
	public void leerPronosticosId() {
		DaoPronostico dp = new DaoPronostico();
		Pronostico pronostico = dp.get(1);
		Assert.assertNotNull(pronostico);
		logger.info(String.format("%s - %s", pronostico.getVisitante().getNombre(), pronostico.getVisitante().getUserId()));
	}

	@Test
	public void leerPronosticosVisitante() {
		DaoVisitante dv = new DaoVisitante();
		Visitante v = dv.get("10208978070830655");
		DaoPronostico dp = new DaoPronostico();
		List<Pronostico> pronosticos = dp.get(v);
		Assert.assertNotEquals(pronosticos.size(), 0);
	}

	@Before
	public void startTransaction() {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		sesion = sf.getCurrentSession();
		sesion.beginTransaction();
	}

}

package pe.trazos.main;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.dao.*;
import pe.trazos.dominio.*;

import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;

public class TestPronostico extends TestBase {

	private final static int ID_COMPETENCIA = 1;
	private final static int ID_FECHA = 307;
	private final static String USER_ID = "58213393-6c5b-4252-99ea-d4dbcec282f4";
	private final static Logger log = LoggerFactory.getLogger(TestPronostico.class);

	@After
	public void endTransaction() {
		sesion.getTransaction().commit();
	}

	@Test
	public void leerPronosticosFecha() {
		DaoVisitante dv = new DaoVisitante();
		Visitante v = dv.get(USER_ID);
		assertNotNull(v);
		DaoFecha df = new DaoFecha();
		Fecha f = df.get(ID_FECHA);
		assertNotNull(f);
		DaoPronostico dp = new DaoPronostico();
		Map<Participacion, Pronostico> pronosticos = dp.obtenerPronosticosFecha(v, f);
		for (Pronostico p : pronosticos.values()) {
			log.info("{} - {}", p.getParticipacion().getEquipo().getNombre(), p.getGoles());
			Assert.assertNotNull(p.getGoles());
		}
	}

	@Test
	public void leerPronosticosVisitante() {
		DaoVisitante dv = new DaoVisitante();
		Visitante v = dv.get(USER_ID);
		Assert.assertNotNull(v);
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

	@Test
	public void validarFechasPronosticadas() {
		DaoVisitante dv = new DaoVisitante();
		Visitante v = dv.get(USER_ID);		DaoCompetencia dc = new DaoCompetencia();
		Competencia c = dc.get(ID_COMPETENCIA);
		c.setVisitante(v);
		for (Fecha f : c.getFechas()) {
			log.info("{}", f.toString());
			// si está pronosticada completamente por el visitante devuelve rtue, si no false
			log.info("tiene pronóstico {} ", f.tienePronostico());
			// si ya pasó la fecha y no aplica pronostico
			f.aplicaPronosticar();
			log.info("aplica pronosticar {} ", f.aplicaPronosticar());
		}
	}

}

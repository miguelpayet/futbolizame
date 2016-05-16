package pe.trazos.main;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.dominio.Equipo;
import pe.trazos.dominio.Participacion;
import pe.trazos.dominio.Partido;

import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestPartido extends TestBase {

	private final Logger logger = LoggerFactory.getLogger(TestPartido.class);

	@Test
	public void leerEmpate() {
		Session sesion = createSession();
		Transaction tran = sesion.beginTransaction();
		Partido partido = traerPartido(sesion, 2161);
		assertNotNull(partido);
		assertTrue(partido.esEmpate());
		tran.rollback();
	}

	@Test
	public void leerPartido() {
		Session sesion = createSession();
		Transaction tran = sesion.beginTransaction();
		Partido partido = traerPartido(sesion, 3065);
		assertNotNull(partido);
		int participantes = 0;
		for (Map.Entry<Boolean, Participacion> partic : partido.getParticipantes().entrySet()) {
			participantes++;
			Equipo eq = partic.getValue().getEquipo();
			assertThat(eq.getNombre(), either(is("paraguay")).or(is("brasil")));
		}
		assertThat(participantes, is(2));
		assertThat(partido.esValido(), is(Boolean.TRUE));
		tran.rollback();
	}

	private Partido traerPartido(Session unaSesion, int unId) {
		return (Partido) unaSesion.get(Partido.class, unId);
	}

}

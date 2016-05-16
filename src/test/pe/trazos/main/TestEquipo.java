package pe.trazos.main;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import pe.trazos.dao.DaoEquipo;
import pe.trazos.dominio.Equipo;
import pe.trazos.dominio.Participacion;
import pe.trazos.dominio.Partido;
import pe.trazos.dominio.Ratio;

import java.text.SimpleDateFormat;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestEquipo extends TestBase {

	@Test
	public void leerPartidosPeru() {
		Session sesion = createSession();
		Transaction tran = sesion.beginTransaction();
		Equipo equipo = traerEquipo(sesion, "peru");
		assertNotNull(equipo);
		List<Participacion> parts = equipo.getParticipaciones();
		assertNotNull(parts);
		int partidos = 0;
		for (Participacion part : parts) {
			partidos++;
			assertThat(part.getEquipo(), is(equipo));
		}
		tran.rollback();
		System.out.println(String.format("partidos: %s", partidos));
	}

	@Test
	public void leerPeru() {
		Session sesion = createSession();
		Transaction tran = sesion.beginTransaction();
		Equipo equipo = traerEquipo(sesion, "peru");
		assertNotNull(equipo);
		assertThat(equipo.getNombre(), is("perú"));
		tran.rollback();
	}

	@Test
	public void leerRatiosPeru() {
		Session sesion = createSession();
		Transaction tran = sesion.beginTransaction();
		Equipo peru = traerEquipo(sesion, "peru");
		Equipo argentina = traerEquipo(sesion, "argentina");
		Ratio ida = peru.compararCon(argentina);
		assertNotNull(ida);
		Ratio vuelta = argentina.compararCon(peru);
		assertNotNull(vuelta);
		System.out.println(String.format("ratios perú vs argentina - victorias: %f - empates: %f - derrotas: %f", ida.getRatioVictorias(), ida.getRatioEmpates(), ida.getRatioDerrotas()));
		System.out.println(String.format("partidos: %s", ida.getTotal()));
		System.out.println(String.format("ratios argentina vs perú - victorias: %f - empates: %f - derrotas: %f", vuelta.getRatioVictorias(), vuelta.getRatioEmpates(), vuelta.getRatioDerrotas()));
		System.out.println(String.format("partidos: %s", vuelta.getTotal()));
		tran.rollback();
	}

	private Equipo traerEquipo(Session unaSesion, String unId) {
		Equipo equipo = (Equipo) unaSesion.get(Equipo.class, unId);
		assertNotNull(equipo);
		return equipo;
	}

	@Test
	@SuppressWarnings("unchecked")
	public void validarPartidosComunes() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Session sesion = createSession();
		Transaction tran = sesion.beginTransaction();
		// primero obtengo los equipos
		Equipo miEquipo = traerEquipo(sesion, "peru");
		Equipo tuEquipo = traerEquipo(sesion, "argentina");
		// luego hago el query por mi cuenta
		String sql = "select * from partido where idpartido in (select idpartido from participacion p where idequipo='%s' or idequipo='%s' group by idpartido having count(1) = 2)";
		SQLQuery sqlQuery = sesion.createSQLQuery(String.format(sql, miEquipo.getId(), tuEquipo.getId())).addEntity(Partido.class);
		List<Partido> misPartidos = (List<Partido>) sqlQuery.list();
		assertNotNull(misPartidos);
		System.out.println("mi lista");
		for (Partido par : misPartidos) {
			System.out.println(String.format("%s vs %s", par.getLocal().getEquipo(), par.getVisita().getEquipo()));
			assertThat(par.getLocal().getEquipo(), either(is(miEquipo)).or(is(tuEquipo)));
			assertThat(par.getVisita().getEquipo(), either(is(miEquipo)).or(is(tuEquipo)));
		}
		// luego pruebo con el dao a ver si los resultados son los mismos
		DaoEquipo de = new DaoEquipo();
		List<Partido> partidosDao = de.getPartidosComunes(miEquipo, tuEquipo);
		// comparo las dos listas deben ser iguales
		assertThat(misPartidos.size(), is(partidosDao.size()));
		System.out.println("otra lista");
		for (Partido par : partidosDao) {
			System.out.println(String.format("%s - %s (%s) vs %s (%s)", dateFormat.format(par.getFechaPartido()),
					par.getLocal().getEquipo(), par.getLocal().getGoles(), par.getVisita().getEquipo(), par.getVisita().getGoles()));
			assertThat(par.getLocal().getEquipo(), either(is(miEquipo)).or(is(tuEquipo)));
			assertThat(par.getVisita().getEquipo(), either(is(miEquipo)).or(is(tuEquipo)));
		}
		tran.rollback();
	}
}

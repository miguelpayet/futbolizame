package pe.trazos.main;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import pe.trazos.dominio.Competencia;
import pe.trazos.dominio.Fecha;

import java.util.Date;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestConcurso extends TestBase {

	@Test
	public void crearTabla() {
		Session sesion = createSession();
		Transaction tran = sesion.beginTransaction();
		Competencia con = traerConcurso(sesion, 1);
		assertNotNull(con);
		con.crearPosiciones();
		con.setActualizado(new Date());
		sesion.saveOrUpdate(con);
		tran.commit();
	}

	@Test
	public void proximaFecha() {
		Session sesion = createSession();
		Transaction tran = sesion.beginTransaction();
		Competencia con = traerConcurso(sesion, 1);
		assertNotNull(con);
		Fecha fecha = con.getFechaSiguiente(new Date());
		assertNotNull(fecha);
		assertThat(fecha.getNumero(), is(7));
		tran.rollback();
	}

	private Competencia traerConcurso(Session unaSesion, int unId) {
		return (Competencia) unaSesion.get(Competencia.class, unId);
	}

}

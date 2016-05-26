package pe.trazos.main;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;
import pe.trazos.auth.SesionFacebook;
import pe.trazos.dao.DaoVisitante;
import pe.trazos.dominio.Visitante;

public class TestTabla extends TestBase {

	@Test
	public void testTabla() {
		Session sesion = createSession();
		Transaction tx = sesion.beginTransaction();
		DaoVisitante dv = new DaoVisitante();
		Visitante v = dv.get("10208978070830655");
		boolean l = SesionFacebook.get().signIn(v.getUserId(), v.getToken());
		Assert.assertThat(l, org.hamcrest.CoreMatchers.is(true));
		tx.rollback();
	}

}

package pe.trazos.wicket;

import org.junit.Test;
import pe.trazos.homepage.HomePageAnonimo;
import pe.trazos.homepage.HomePageAutentificado;
import pe.trazos.wicket.AbstractHorasTest;

public class TestHomePageAutentificado extends AbstractHorasTest {

	@Test
	public void rendersSuccessfully() {
		tester.startPage(HomePageAnonimo.class);
		tester.assertRenderedPage(HomePageAutentificado.class);
	}

}

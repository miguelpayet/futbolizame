package pe.trazos.wicket;

import org.junit.Test;
import pe.trazos.homepage.HomePageAnonimo;

public class TestHomePageAnonimo extends AbstractHorasTest {

	@Test
	public void rendersSuccessfully() {
		tester.startPage(HomePageAnonimo.class);
		tester.assertRenderedPage(HomePageAnonimo.class);
	}

}

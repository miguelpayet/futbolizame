package pe.trazos.wicket;

import org.junit.Test;
import pe.trazos.homepage.HomePage;

public class TestHomePageAnonimo extends AbstractHorasTest {

	@Test
	public void rendersSuccessfully() {
		tester.startPage(HomePage.class);
		tester.assertRenderedPage(HomePage.class);
	}

}

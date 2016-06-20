package pe.trazos.web;

import de.agilecoders.wicket.core.Bootstrap;
import org.apache.wicket.Application;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.auth.SesionWeb;
import pe.trazos.componentes.WebPageBase;
import pe.trazos.homepage.HomePage;

public class FutbolizameApplication extends WebApplication {

	public static FutbolizameApplication get() {
		return (FutbolizameApplication) Application.get();
	}

	public FutbolizameApplication() {
	}

	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void init() {
		super.init();
		Bootstrap.install(this);
		if (getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT) {
			getDebugSettings().setOutputComponentPath(true);
		}
		getRequestCycleListeners().add(new HibernateRequestListener());
	}

	@Override
	public Session newSession(final Request request, final Response response) {
		return new SesionWeb(request);
	}

}

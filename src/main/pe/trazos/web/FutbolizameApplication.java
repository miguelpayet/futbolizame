package pe.trazos.web;

import de.agilecoders.wicket.core.Bootstrap;
import org.apache.wicket.Application;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.auth.SesionFacebook;
import pe.trazos.homepage.HomePage;

public class FutbolizameApplication extends AuthenticatedWebApplication {

	public static FutbolizameApplication get() {
		return (FutbolizameApplication) Application.get();
	}

	private final Logger logger = LoggerFactory.getLogger(FutbolizameApplication.class);

	public FutbolizameApplication() {
	}

	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return null;
	}

	@Override
	protected Class<SesionFacebook> getWebSessionClass() {
		return SesionFacebook.class;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void init() {
		super.init();
		Bootstrap.install(this);
		if (getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT) {
			getDebugSettings().setOutputComponentPath(true);
		}
	}

	@Override
	public Session newSession(final Request request, final Response response) {
		return new SesionFacebook(request);
	}

}

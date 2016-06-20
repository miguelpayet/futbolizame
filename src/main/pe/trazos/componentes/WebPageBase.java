package pe.trazos.componentes;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.facebook.FacebookPermission;
import org.wicketstuff.facebook.FacebookSdk;
import org.wicketstuff.facebook.behaviors.AuthLoginEventBehavior;
import org.wicketstuff.facebook.behaviors.LogoutEventBehavior;
import org.wicketstuff.facebook.plugins.LikeButton;
import org.wicketstuff.facebook.plugins.LoginButton;
import pe.trazos.auth.SesionWeb;
import pe.trazos.homepage.HomePage;
import pe.trazos.homepage.LoginStatusEventBehavior;
import pe.trazos.web.FutbolizameApplication;

public abstract class WebPageBase extends GenericWebPage {

	private static final Logger log = LoggerFactory.getLogger(WebPageBase.class);

	public WebPageBase() {
		super();
	}

	public WebPageBase(final PageParameters parameters) {
		super(parameters);
		agregarFacebook();
	}

	private void agregarFacebook() {
		String facebookid = FutbolizameApplication.get().getInitParameter("facebook-app-id");
		log.debug("app id {}", facebookid);
		FacebookSdk fsdk = new FacebookSdk("fb-root", facebookid);
		fsdk.setOgProperty("url", getUrlBase());
		fsdk.setOgProperty("type", "website");
		fsdk.setOgProperty("description", "pronósitco de la eliminatoria a rusia 2018 en sudamérica");
		add(fsdk);
		final IModel<String> url = Model.of("http://futboliza.me"); // todo: no en duro
		final LikeButton likeButton = new LikeButton("likeButton", url);
		likeButton.setLayoutStyle(LikeButton.LikeButtonLayoutStyle.BUTTON_COUNT);
		likeButton.setAction(LikeButton.LikeButtonAction.RECOMMEND);
		add(likeButton);
		LoginButton loginButton = new LoginButton("loginButton", FacebookPermission.email);
		loginButton.add(new AttributeModifier("auto_logout_link", Model.of(true)));
		add(loginButton);
		add(new AuthLoginEventBehavior() {
			@Override
			protected void onSessionEvent(AjaxRequestTarget target, String status, String userId, String signedRequest, String expiresIn, String accessToken) {
				log.info("login event");
				doLogin(userId, accessToken, target);
			}
		});
		add(new LogoutEventBehavior() {
			@Override
			protected void onLogout(AjaxRequestTarget target, String status) {
				log.info("logout event");
				doLogout(target);
			}
		});
		add(new LoginStatusEventBehavior() {
			@Override
			protected void onLoginStatus(AjaxRequestTarget target, String status, String userId, String expiresIn, String accessToken) {
				log.info("login status event: " + status);
				if (status != null && status.equals("connected")) {
					doLogin(userId, accessToken, target);
				} else {
					log.info("status: " + status);
					doLogout(target); // esto pinta la tabla anónima
				}
			}
		});
	}

	protected abstract void doLogin(String unUsuario, String unToken, AjaxRequestTarget unTarget);

	protected abstract void doLogout(AjaxRequestTarget unTarget);

	protected SesionWeb getSesion() {
		return SesionWeb.get();
	}

	private String getUrlBase() {
		return RequestCycle.get().getUrlRenderer().renderFullUrl(Url.parse(urlFor(HomePage.class, null).toString()));
	}

}

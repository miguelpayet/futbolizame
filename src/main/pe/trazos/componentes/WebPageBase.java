package pe.trazos.componentes;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.facebook.FacebookPermission;
import org.wicketstuff.facebook.FacebookSdk;
import org.wicketstuff.facebook.behaviors.AuthLoginEventBehavior;
import org.wicketstuff.facebook.behaviors.AuthStatusChangeEventBehavior;
import org.wicketstuff.facebook.behaviors.LogoutEventBehavior;
import org.wicketstuff.facebook.plugins.LikeButton;
import org.wicketstuff.facebook.plugins.LoginButton;
import pe.trazos.auth.SesionFacebook;
import pe.trazos.web.FutbolizameApplication;

public abstract class WebPageBase extends GenericWebPage {

	private final Logger logger = LoggerFactory.getLogger(WebPageBase.class);

	public WebPageBase() {
		super();
	}

	public WebPageBase(final PageParameters parameters) {
		super(parameters);
		agregarFacebook();
	}

	private void agregarFacebook() {
		add(new FacebookSdk("fb-root", FutbolizameApplication.get().getInitParameter("facebook-app-id")));
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
				logger.info("login event");
				doLogin(userId, accessToken);
			}
		});
		add(new AuthStatusChangeEventBehavior() {
			@Override
			protected void onSessionEvent(AjaxRequestTarget target, String status, String userId, String signedRequest, String expiresIn, String accessToken) {
				logger.info(String.format("status change event: %s", status));
				if (status.equals("connected") && !FutbolizameApplication.get().getHomePageAutentificado().equals(getComponent().getClass())) {
					doLogin(userId, accessToken);
				} else if (status.equals("unknown")) {
					doLogout();
				}
			}
		});
		add(new LogoutEventBehavior() {
			@Override
			protected void onLogout(AjaxRequestTarget target, String status) {
				logger.info("logout event");
				doLogout();
			}
		});
	}

	private void doLogin(String unUsuario, String unToken) {
		SesionFacebook sf = getSesion();
		if (!sf.isSignedIn()) {
			sf.signIn(unUsuario, unToken);
			throw new RestartResponseException(FutbolizameApplication.get().getHomePageAutentificado());
		}
	}

	protected void doLogout() {
		getSesion().signOut();
		throw new RestartResponseException(FutbolizameApplication.get().getHomePage());
	}

	protected void encabezado(String intro) {
		add(new Label("intro", intro));
	}

	protected void feedback() {
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
	}

	protected SesionFacebook getSesion() {
		return (SesionFacebook) Session.get();
	}

}

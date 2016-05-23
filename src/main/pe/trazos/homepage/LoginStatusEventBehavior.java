package pe.trazos.homepage;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.IRequestParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.facebook.behaviors.AbstractSubscribeBehavior;

public abstract class LoginStatusEventBehavior extends AbstractSubscribeBehavior {

	private static final String ACCESS_TOKEN = "authResponse.accessToken";
	private static final String EXPIRES_IN = "authResponse.expiresIn";
	private static final String STATUS = "status";
	private static final String USER_ID = "authResponse.userID";
	private static final Logger log = LoggerFactory.getLogger(LoginStatusEventBehavior.class);

	public LoginStatusEventBehavior() {
		super("xfbml.render");
	}

	@Override
	protected void onEvent(final AjaxRequestTarget target, final IRequestParameters parameters, final String response) {
		final String status = parameters.getParameterValue(STATUS).toOptionalString();
		final String userId = parameters.getParameterValue(USER_ID).toOptionalString();
		final String expiresIn = parameters.getParameterValue(EXPIRES_IN).toOptionalString();
		final String accessToken = parameters.getParameterValue(ACCESS_TOKEN).toOptionalString();
		onLoginStatus(target, status, userId, expiresIn, accessToken);
	}

	protected abstract void onLoginStatus(AjaxRequestTarget target, String status, String userId, String expiresIn, String accessToken);

	public void renderHead(final Component component, final IHeaderResponse response) {
		log.debug("renderHead");
		final StringBuilder js = new StringBuilder();
		js.append("FB.Event.subscribe('").append("xfbml.render").append("', function(response) {");
		js.append("FB.getLoginStatus(function(response) {");
		js.append("var callback = '").append(getCallbackUrl()).append("';");
		js.append("if (response.authResponse) {");
		js.append("try{callback += '&status=' + Wicket.Form.encode(response.status);}catch(e){}");
		js.append("try{callback += '&authResponse.userID=' + Wicket.Form.encode(response.authResponse.userID);}catch(e){}");
		js.append("try{callback += '&authResponse.accessToken=' + Wicket.Form.encode(response.authResponse.accessToken);}catch(e){}");
		js.append("try{callback += '&authResponse.expiresIn=' + Wicket.Form.encode(response.authResponse.expiresIn);}catch(e){}");
		js.append("} else {");
		js.append("try{callback += '&status=' + Wicket.Form.encode('unknown');}catch(e){}");
		js.append("try{callback += '&authResponse.userID=' + Wicket.Form.encode('');}catch(e){}");
		js.append("try{callback += '&authResponse.accessToken=' + Wicket.Form.encode('');}catch(e){}");
		js.append("try{callback += '&authResponse.expiresIn=' + Wicket.Form.encode('');}catch(e){}");
		js.append("}");
		js.append("Wicket.Ajax.get({'u': callback + '&response=' + Wicket.Form.encode(response)});");
		js.append("});");
		js.append("});");
		log.debug(js.toString());
		response.render(JavaScriptHeaderItem.forScript(js.toString(), null));
	}

}

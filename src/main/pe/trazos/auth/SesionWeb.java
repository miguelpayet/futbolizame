package pe.trazos.auth;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.types.User;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.dao.DaoVisitante;
import pe.trazos.dominio.Visitante;

import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.UUID;

public class SesionWeb extends WebSession {

	private static final String NOMBRE_COOKIE = "id-anonimo";
	private static final Logger log = LoggerFactory.getLogger(SesionWeb.class);

	public static SesionWeb get() {
		return (SesionWeb) Session.get();
	}

	private String idAnonimo;
	private String idFacebook;
	private String nombreFacebook;
	TipoSesion tipo;
	private Visitante visitante;

	public SesionWeb(Request unRequest) {
		super(unRequest);
		tipo = TipoSesion.INDEFINIDO;
	}

	private void crearVisitante(TipoSesion tipo, String unUserId, String unNombre, String unToken) {
		DaoVisitante dv = new DaoVisitante();
		visitante = dv.get(unUserId);
		if (visitante == null) {
			visitante = new Visitante();
			visitante.setId(unUserId);
			visitante.setNombre(unNombre);
		}
		visitante.setFecha(new Date());
		visitante.setToken(unToken);
		dv.grabar(visitante);
	}

	public String getUserId() {
		if (tipo == TipoSesion.FACEBOOK) {
			return idFacebook;
		} else if (tipo == TipoSesion.ANONIMO) {
			return idAnonimo;
		} else {
			return "";
		}
	}

	public String getUserName() {
		if (tipo == TipoSesion.FACEBOOK) {
			return nombreFacebook;
		} else {
			return "";
		}
	}

	public Visitante getVisitante() {
		return visitante;
	}

	public boolean isLoginAnonimo() {
		return tipo == TipoSesion.ANONIMO && !idAnonimo.equals("");
	}

	public boolean isLoginFacebook() {
		return tipo == TipoSesion.FACEBOOK && !idFacebook.equals("");
	}

	public void loginAnonimo() {

		if (!isLoginAnonimo()) {
			tipo = TipoSesion.ANONIMO;
			WebRequest webRequest = (WebRequest) RequestCycle.get().getRequest();
			Cookie cookie = webRequest.getCookie(NOMBRE_COOKIE);
			if (cookie == null) {

				idAnonimo = UUID.randomUUID().toString();
				cookie = new Cookie(NOMBRE_COOKIE, idAnonimo);
				cookie.setPath("/");
			} else {

				idAnonimo = cookie.getValue();
			}
			cookie.setMaxAge(7 * 24 * 60 * 60);
			WebResponse webResponse = (WebResponse) RequestCycle.get().getResponse();
			webResponse.addCookie(cookie);
			crearVisitante(tipo, idAnonimo, "", "");

		}
	}

	public boolean loginFacebook(String unUserId, String unToken) {

		try {
			tipo = TipoSesion.FACEBOOK;
			if (unUserId == null || unToken == null) {
				this.logoutFacebook();
				return false;
			}
			if (!unUserId.equals(idFacebook)) {
				this.logoutFacebook();
			}
			String token = unToken;
			idFacebook = unUserId;
			// validar el token
			FacebookClient client = new DefaultFacebookClient(unToken, Version.LATEST);
			// conseguir el nombre
			User user = client.fetchObject("me", User.class);
			nombreFacebook = user.getName();
			// crear o actualizar el registro de bd
			crearVisitante(tipo, unUserId, nombreFacebook, unToken);

			return true;
		} catch (Exception e) {
			tipo = TipoSesion.INDEFINIDO;
			log.error(e.getMessage());
			return false;
		}
	}

	public void logoutAnonimo() {
		tipo = TipoSesion.INDEFINIDO;
		idAnonimo = null;
	}

	public void logoutFacebook() {
		tipo = TipoSesion.INDEFINIDO;
		idFacebook = null;
		nombreFacebook = null;
	}

}

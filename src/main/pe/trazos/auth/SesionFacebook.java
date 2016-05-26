package pe.trazos.auth;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.types.User;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.dao.DaoVisitante;
import pe.trazos.dominio.Visitante;

import java.util.Date;

public class SesionFacebook extends AuthenticatedWebSession {

	private static final Logger log = LoggerFactory.getLogger(SesionFacebook.class);

	public static SesionFacebook get() {
		return (SesionFacebook) Session.get();
	}

	private String userId;
	private String userName;
	private Visitante visitante;

	public SesionFacebook(Request unRequest) {
		super(unRequest);
	}

	@Override
	protected boolean authenticate(String unUserId, String unToken) {
		log.info("userid: " + unUserId);
		try {
			if (unUserId == null || unToken == null) {
				this.signOut();
				return false;
			}
			if (!unUserId.equals(userId)) {
				this.signOut();
			}
			String token = unToken;
			userId = unUserId;
			// validar el token
			FacebookClient client = new DefaultFacebookClient(unToken, Version.LATEST);
			// conseguir el nombre
			User user = client.fetchObject("me", User.class);
			userName = user.getName();
			// crear o actualizar el registro de bd
			DaoVisitante dv = new DaoVisitante();
			visitante = dv.get(unUserId);
			if (visitante == null) {
				visitante = new Visitante();
				visitante.setId(unUserId);
				visitante.setNombre(user.getName());
			}
			visitante.setFecha(new Date());
			visitante.setToken(token);
			dv.grabar(visitante);
			log.info("nombre: " + userName);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			//todo: algún tipo de feedback
			return false;
		}
	}

	@Override
	public Roles getRoles() {
		return null;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public Visitante getVisitante() {
		return visitante;
	}

}

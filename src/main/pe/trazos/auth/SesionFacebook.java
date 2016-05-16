package pe.trazos.auth;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.types.User;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.dao.DaoVisitante;
import pe.trazos.dominio.Visitante;

import java.util.Date;

public class SesionFacebook extends AuthenticatedWebSession {

	private final Logger logger = LoggerFactory.getLogger(SesionFacebook.class);
	private String token;
	private String userId;
	private String userName;

	public SesionFacebook(Request unRequest) {
		super(unRequest);
	}

	@Override
	protected boolean authenticate(String unUserId, String unToken) {
		logger.info("userid: " + unUserId);
		try {
			if (unUserId == null || unToken == null) {
				return false;
			}
			if (!unUserId.equals(userId)) {
				this.signOut();
			}
			token = unToken;
			userId = unUserId;
			// validar el token
			FacebookClient client = new DefaultFacebookClient(unToken, Version.LATEST);
			// conseguir el nombre
			User user = client.fetchObject("me", User.class);
			userName = user.getName();
			// crear o actualizar el registro de bd
			DaoVisitante dv = new DaoVisitante();
			Visitante visitaBd = dv.get(unUserId);
			if (visitaBd == null) {
				visitaBd = new Visitante();
				visitaBd.setId(unUserId);
				visitaBd.setNombre(user.getName());
			}
			visitaBd.setFecha(new Date());
			visitaBd.setToken(token);
			dv.grabar(visitaBd);
			logger.info("nombre: " + userName);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
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

}

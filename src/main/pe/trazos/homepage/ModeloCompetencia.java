package pe.trazos.homepage;

import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Session;
import org.slf4j.LoggerFactory;
import pe.trazos.auth.SesionFacebook;
import pe.trazos.dao.DaoCompetencia;
import pe.trazos.dao.HibernateUtil;
import pe.trazos.dominio.Competencia;
import pe.trazos.dominio.Posicion;
import pe.trazos.dominio.Posicionable;
import pe.trazos.web.FutbolizameApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModeloCompetencia extends LoadableDetachableModel<Competencia> {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(ModeloCompetencia.class);

	private Integer idCompetencia;
	private List<? extends Posicionable> participantes;

	public ModeloCompetencia() {
		init();
	}

	public void crearPosiciones() {
		if (SesionFacebook.get().isSignedIn()) {
			getObject().setVisitante(SesionFacebook.get().getVisitante());
		} else {
			getObject().clearVisitante();
		}
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		if (s.isOpen()) {
			log.info("todo bien");
		}
		getObject().crearPosiciones();
	}

	public String getNombreCompetencia() {
		return getObject() != null ? getObject().getNombre() : "";
	}

	public Map<Boolean, ? extends Posicionable> getParticipantes() {
		return null;
	}

	public List<HashMap<Boolean, Posicionable>> getPosicionables() {
		return null;
	}

	public Map<String, Posicion> getPosiciones() {
		return getObject().getPosiciones();
	}

	public String getTituloTabla() {
		String titulo = "tabla de posiciones ";
		if (SesionFacebook.get().isSignedIn()) {
			titulo += "(" + SesionFacebook.get().getUserName() + ")";
		} else {
			titulo += " (anónimo)";
		}
		return titulo;
	}

	private void init() {
		// identificar competencia configurada
		idCompetencia = Integer.valueOf(FutbolizameApplication.get().getInitParameter("competencia.id"));
		if (idCompetencia == null) {
			throw new RuntimeException("no está configurado id de competencia");
		}
		load();
	}

	@Override
	protected Competencia load() {
		log.info("load");
		DaoCompetencia dc = new DaoCompetencia();
		Competencia c = dc.get(idCompetencia);
		if (c == null) {
			throw new RuntimeException("no está creado el objeto competencia");
		}
		return c;
	}

	@Override
	protected void onAttach() {
		log.info("onAttach");
	}

	@Override
	protected void onDetach() {
		log.info("onDetach");
		idCompetencia = getObject().getId();
	}

}

package pe.trazos.homepage;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.LoggerFactory;
import pe.trazos.auth.SesionWeb;
import pe.trazos.dao.DaoCompetencia;
import pe.trazos.dao.DaoFecha;
import pe.trazos.dominio.Competencia;
import pe.trazos.dominio.Fecha;
import pe.trazos.dominio.Posicion;
import pe.trazos.dominio.Posicionable;
import pe.trazos.web.FutbolizameApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModeloCompetencia extends LoadableDetachableModel<Competencia> {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(ModeloCompetencia.class);

	private Integer idCompetencia;
	private Integer idFecha;
	private List<? extends Posicionable> participantes;

	public ModeloCompetencia() {
		init();
	}

	public void crearPosiciones() {
		getObject().setVisitante(SesionWeb.get().getVisitante());
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
		return "tabla de posiciones (" + SesionWeb.get().getUserName() + ")";
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
		log.debug("load");
		DaoCompetencia dc = new DaoCompetencia();
		Competencia c = dc.get(idCompetencia);
		if (c == null) {
			throw new RuntimeException("no está creado el objeto competencia");
		}
		if (idFecha != null) {
			DaoFecha df = new DaoFecha();
			Fecha f = df.get(idFecha);
			c.setFechaActual(f);
		}
		return c;
	}

	@Override
	protected void onAttach() {
		log.debug("onAttach");
	}

	@Override
	protected void onDetach() {
		log.debug("onDetach");
		idCompetencia = getObject().getId();
		if (getObject().getFechaActual() != null) {
			idFecha = getObject().getFechaActual().getId();
		}
	}

}

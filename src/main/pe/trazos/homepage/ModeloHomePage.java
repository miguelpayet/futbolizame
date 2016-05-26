package pe.trazos.homepage;

import pe.trazos.auth.SesionFacebook;
import pe.trazos.dao.DaoCompetencia;
import pe.trazos.dominio.*;
import pe.trazos.web.FutbolizameApplication;

import java.io.Serializable;
import java.util.*;

public class ModeloHomePage implements Serializable {

	private Competencia competencia;
	private Fecha fecha;
	private List<? extends Posicionable> participantes;

	public ModeloHomePage() {
		init();
	}

	public void crearPosiciones() {
		if (SesionFacebook.get().isSignedIn()) {
			competencia.setVisitante(SesionFacebook.get().getVisitante());
		} else {
			competencia.clearVisitante();
		}
		competencia.crearPosiciones();
	}

	public String getNombreCompetencia() {
		return competencia != null ? competencia.getNombre() : "";
	}

	public String getNombreFecha() {
		return fecha != null ? fecha.getNombre() : "";
	}

	public Map<Boolean, ? extends Posicionable> getParticipantes() {
		return null;
	}

	public Set<Partido> getPartidos() {
		return fecha != null ? fecha.getPartidos() : null;
	}

	public List<HashMap<Boolean, Posicionable>> getPosicionables() {
		return null;
	}

	public Map<String, Posicion> getPosiciones() {
		return competencia.getPosiciones();

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
		// identificar próxima fecha (primera fecha del futuro)
		Integer idCompetencia = Integer.valueOf(FutbolizameApplication.get().getInitParameter("competencia.id"));
		if (idCompetencia == null) {
			throw new RuntimeException("no está configurado id de competencia");
		}
		DaoCompetencia dc = new DaoCompetencia();
		competencia = dc.get(idCompetencia);
		if (competencia == null) {
			throw new RuntimeException("no está creado el objeto competencia");
		}
		fecha = competencia.getFechaSiguiente(new Date());
		if (fecha == null) {
			throw new RuntimeException("no existe próxima fecha");
		}
	}

}

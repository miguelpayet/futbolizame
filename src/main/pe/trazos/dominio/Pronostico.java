package pe.trazos.dominio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pronostico")
@IdClass(PronosticoPK.class)
public class Pronostico extends Posicionable implements Serializable {

	private static final Logger log = LoggerFactory.getLogger(Pronostico.class);

	@Id
	@ManyToOne()
	@JoinColumn(name = "idparticipacion")
	private Participacion participacion;
	@Id
	@ManyToOne()
	@JoinColumn(name = "userid")
	private Visitante visitante;

	@Override
	protected Equipo getEquipo() {
		return getParticipacion().getEquipo();
	}

	public Participacion getParticipacion() {
		return participacion;
	}

	@Override
	protected Partido getPartido() {
		return getParticipacion().getPartido();
	}

	public Visitante getVisitante() {
		return visitante;
	}

	public Pronostico setParticipacion(Participacion participacion) {
		this.participacion = participacion;
		return this;
	}

	public Pronostico setVisitante(Visitante visitante) {
		this.visitante = visitante;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		if (getPartido() != null) {
			sb.append("pronostico - ");
			sb.append(getPartido().getFecha());
		}
		if (getParticipacion() != null) {
			sb.append(" - ");
			sb.append(getParticipacion().getEquipo().getNombre());
		}
		return sb.toString();
	}

}

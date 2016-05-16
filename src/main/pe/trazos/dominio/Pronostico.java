package pe.trazos.dominio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
@Table(name = "pronostico")
public class Pronostico extends Posicionable {

	private static final Logger log = LoggerFactory.getLogger(Pronostico.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idpronostico")
	private Integer id;
	@ManyToOne()
	@JoinColumn(name = "idparticipacion")
	private Participacion participacion;
	@ManyToOne()
	@JoinColumn(name = "userid")
	private Visitante visitante;

	@Override
	protected Equipo getEquipo() {
		return getParticipacion().getEquipo();
	}

	@Override
	public Integer getId() {
		return id;
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

	@Override
	public void setId(Integer unId) {
		id = unId;
	}

	public void setParticipacion(Participacion participacion) {
		this.participacion = participacion;
	}

	public void setVisitante(Visitante visitante) {
		this.visitante = visitante;
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

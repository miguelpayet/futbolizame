package pe.trazos.dominio;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "partido")
public class Partido implements Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idfecha")
	private Fecha fecha;
	@Column(name = "fecha")
	private Date fechaPartido;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idpartido")
	private Integer id;
	@OneToMany(mappedBy = "partido", cascade = CascadeType.ALL) //, fetch = FetchType.EAGER)
	@MapKey(name = "local")
	private Map<Boolean, Participacion> participantes;

	public Boolean esEmpate() {
		Boolean empate = Boolean.FALSE;
		if (esValido() && tieneResultado()) {
			Participacion local = getLocal();
			Participacion visita = getVisita();
			empate = (local.getGoles().equals(visita.getGoles()));
		}
		return empate;
	}

	public Boolean esValido() {
		return (getParticipantes().containsKey(Boolean.FALSE) && getParticipantes().containsKey(Boolean.TRUE));
	}

	public Fecha getFecha() {
		return fecha;
	}

	public Date getFechaPartido() {
		return fechaPartido;
	}

	public Participacion getGanador() {
		Participacion ganador = null;
		if (esValido() && tieneResultado()) {
			Participacion local = getLocal();
			Participacion visita = getVisita();
			if (local.getGoles() > visita.getGoles()) {
				ganador = local;
			} else if (visita.getGoles() > local.getGoles()) {
				ganador = visita;
			}
		}
		return ganador;
	}

	public Integer getId() {
		return id;
	}

	public Participacion getLocal() {
		return getParticipante(Boolean.TRUE);
	}

	private Participacion getParticipante(Boolean unTipo) {
		Participacion partic = null;
		if (esValido()) {
			partic = getParticipantes().get(unTipo);
		}
		return partic;
	}

	public Map<Boolean, Participacion> getParticipantes() {
		return participantes;
	}

	public Participacion getPerdedor() {
		Participacion perdedor = null;
		if (esValido() && tieneResultado()) {
			Participacion local = getLocal();
			Participacion visita = getVisita();
			if (local.getGoles() < visita.getGoles()) {
				perdedor = local;
			} else if (visita.getGoles() < local.getGoles()) {
				perdedor = visita;
			}
		}
		return perdedor;
	}

	public Participacion getVisita() {
		return getParticipante(Boolean.FALSE);
	}

	public void setFecha(Fecha fecha) {
		this.fecha = fecha;
	}

	public void setFechaPartido(Date fechaPartido) {
		this.fechaPartido = fechaPartido;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setParticipantes(Map<Boolean, Participacion> participantes) {
		this.participantes = participantes;
	}

	public Boolean tieneResultado() {
		return getLocal().tieneGoles() && getVisita().tieneGoles();
	}

	public String toString() {
		return String.format("%s vs %s (%s)", getLocal().getEquipo().getNombre(), getVisita().getEquipo().getNombre(), getFecha().getFecha());
	}

}

package pe.trazos.dominio;

import javax.persistence.*;

@Entity
@Table(name = "participacion")
public class Participacion extends Posicionable {

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "idequipo")
	private Equipo equipo;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idparticipacion")
	private Integer id;
	@Column(name = "local")
	private Boolean local;
	@ManyToOne()
	@JoinColumn(name = "idpartido")
	private Partido partido;

	public Participacion() {
		reset();
	}

	@Override
	public Equipo getEquipo() {
		return equipo;
	}

	@Override
	public Integer getGolesContra() {
		return golesContra;
	}

	@Override
	public Integer getGolesFavor() {
		return golesFavor;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public Partido getPartido() {
		return partido;
	}

	@Override
	public Integer getPartidoEmpatado() {
		return partidoEmpatado;
	}

	@Override
	public Integer getPartidoGanado() {
		return partidoGanado;
	}

	@Override
	public Integer getPartidoPerdido() {
		return partidoPerdido;
	}

	@Override
	public Integer getPuntos() {
		return puntos;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		if (getPartido() != null) {
			sb.append("participación - ");
			sb.append(getPartido().getFecha());
		}
		if (getEquipo() != null) {
			sb.append(" - ");
			sb.append(getEquipo().getNombre());
		}
		return sb.toString();
	}

}

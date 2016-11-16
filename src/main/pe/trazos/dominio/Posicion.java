package pe.trazos.dominio;

import javax.persistence.*;
import java.io.Serializable;

public class Posicion implements Serializable, Comparable<Posicion> {

	public static final int MAYOR = -1;
	public static final int MENOR = 1;

	@ManyToOne()
	@JoinColumn(name = "idconcurso")
	private Competencia competencia;
	@ManyToOne()
	@JoinColumn(name = "idequipo")
	private Equipo equipo;
	@Transient
	protected Integer golesContra;
	@Transient
	protected Integer golesFavor;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idposicion")
	private Integer id;
	@Transient
	protected Integer partidosEmpatados;
	@Transient
	protected Integer partidosGanados;
	@Transient
	protected Integer partidosPerdidos;
	@Transient
	protected Integer puntos;

	public Posicion() {
		reset();
	}

	/**
	 * pendiente: comparar velocidad vs compararlos como string, concatenando los valores empacados con espacio o 0
	 * pendiente:  reglas fifa para tabla de posiciones eliminatoria
	 * @param unaPosicion -> representa una posicion en la tabla
	 * @return indica si este objeto es menor, igual o mayor que la posicion recibida
	 */
	public int compareTo(Posicion unaPosicion) {
		if (puntos > unaPosicion.getPuntos()) {
			return MAYOR;
		} else if (puntos < unaPosicion.getPuntos()) {
			return MENOR;
		} else {
			if (getDiferenciaGoles() > unaPosicion.getDiferenciaGoles()) {
				return MAYOR;
			} else if (getDiferenciaGoles() < unaPosicion.getDiferenciaGoles()) {
				return MENOR;
			} else {
				if (getGolesFavor() > unaPosicion.getGolesFavor()) {
					return MAYOR;
				} else if (getDiferenciaGoles() < unaPosicion.getDiferenciaGoles()) {
					return MENOR;
				} else {
					if (partidosGanados > unaPosicion.getPartidosGanados()) {
						return MAYOR;
					} else if (partidosGanados < unaPosicion.getPartidosGanados()) {
						return MENOR;
					} else {
						return -1; // aqui viene lo de head-to-head record
					}
				}
			}
		}
	}

	public Competencia getCompetencia() {
		return competencia;
	}

	public Integer getDiferenciaGoles() {
		return golesFavor - golesContra;
	}

	public Equipo getEquipo() {
		return equipo;
	}

	/**
	 * método para wicket
	 *
	 * @return goles en contra del equipo en la tabla
	 */
	@SuppressWarnings("unused")
	public Integer getGolesContra() {
		return golesContra;
	}

	public Integer getGolesFavor() {
		return golesFavor;
	}

	public Integer getId() {
		return id;
	}

	public String getLogo() {
		return getEquipo().getLogo();
	}

	/**
	 * método para wicket
	 *
	 * @return cantidad de partidos empatados del equipo en la tabla
	 */
	@SuppressWarnings("unused")
	public Integer getPartidosEmpatados() {
		return partidosEmpatados;
	}

	public Integer getPartidosGanados() {
		return partidosGanados;
	}

	/**
	 * método para wicket
	 *
	 * @return cantidad de partidos jugados del objeto
	 */
	@SuppressWarnings("unused")
	public Integer getPartidosJugados() {
		return partidosEmpatados + partidosPerdidos + partidosGanados;
	}

	/**
	 * método para wicket
	 *
	 * @return partidos perdidos del equipo en la tabla
	 */
	@SuppressWarnings("unused")
	public Integer getPartidosPerdidos() {
		return partidosPerdidos;
	}

	public Integer getPuntos() {
		return puntos;
	}

	public void reset() {
		golesFavor = 0;
		golesContra = 0;
		partidosEmpatados = 0;
		partidosGanados = 0;
		partidosPerdidos = 0;
		puntos = 0;
	}

	public void setCompetencia(Competencia competencia) {
		this.competencia = competencia;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void sumar(Posicionable unPosicionable, Posicionable otroPosicionable) {
		if (unPosicionable.actualizar(otroPosicionable)) {
			puntos += unPosicionable.getPuntos();
			golesFavor += unPosicionable.getGolesFavor();
			golesContra += unPosicionable.getGolesContra();
			partidosGanados += unPosicionable.getPartidoGanado();
			partidosEmpatados += unPosicionable.getPartidoEmpatado();
			partidosPerdidos += unPosicionable.getPartidoPerdido();
		}
	}

	/**
	 * @return cadena de representación del objeto
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (getEquipo() != null) {
			sb.append(getEquipo().getNombre());
		} else {
			sb.append("sin equipo");
		}
		sb.append(" - ");
		if (getPuntos() != null) {
			sb.append("puntos ").append(getPuntos());
		} else {
			sb.append("sin puntos");
		}
		return sb.toString();
	}

}

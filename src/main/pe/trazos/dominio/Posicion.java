package pe.trazos.dominio;

import javax.persistence.*;
import java.io.Serializable;

//@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "posicion")
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
	private Integer posicion;
	@Transient
	protected Integer puntos;

	public Posicion() {
		reset();
	}

	// comparar velocidad vs compararlos como string, concatenando los valores empacados con espacio o 0
	// reglas fifa para tabla de posiciones eliminatoria
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

	public Integer getPartidosEmpatados() {
		return partidosEmpatados;
	}

	public Integer getPartidosGanados() {
		return partidosGanados;
	}

	public Integer getPartidosJugados() {
		return partidosEmpatados + partidosPerdidos + partidosGanados;
	}

	public Integer getPartidosPerdidos() {
		return partidosPerdidos;
	}

	public Integer getPosicion() {
		return posicion;
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

	public void setGolesContra(Integer golesContra) {
		this.golesContra = golesContra;
	}

	public void setGolesFavor(Integer golesFavor) {
		this.golesFavor = golesFavor;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPartidosEmpatados(Integer partidosEmpatados) {
		this.partidosEmpatados = partidosEmpatados;
	}

	public void setPartidosGanados(Integer partidosGanados) {
		this.partidosGanados = partidosGanados;
	}

	public void setPartidosPerdidos(Integer partidosPerdidos) {
		this.partidosPerdidos = partidosPerdidos;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}

	public void setPuntos(Integer puntos) {
		this.puntos = puntos;
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

}

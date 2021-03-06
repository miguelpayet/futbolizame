package pe.trazos.dominio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;

@MappedSuperclass
public abstract class Posicionable implements Serializable {

	private static final Logger log = LoggerFactory.getLogger(Posicionable.class);

	@Column(name = "goles")
	Integer goles;
	@Transient
	protected Integer golesContra;
	@Transient
	protected Integer golesFavor;
	@Transient
	protected Integer partidoEmpatado;
	@Transient
	protected Integer partidoGanado;
	@Transient
	protected Integer partidoPerdido;
	@Transient
	protected Integer puntos;

	public boolean actualizar(Posicionable unPosicionable) {
		boolean tieneGoles = this.getGoles() != null && unPosicionable.getGoles() != null;
		if (tieneGoles) {
			golesFavor = this.getGoles();
			golesContra = unPosicionable.getGoles();
			if (this.getGoles().equals(unPosicionable.getGoles())) {
				// empate
				partidoEmpatado = 1;
				partidoGanado = 0;
				partidoPerdido = 0;
				puntos = Competencia.PUNTOS_EMPATE;
			} else if (this.getGoles() > unPosicionable.getGoles()) {
				// ganador
				partidoEmpatado = 0;
				partidoGanado = 1;
				partidoPerdido = 0;
				puntos = Competencia.PUNTOS_VICTORIA;
			} else if (this.getGoles() < unPosicionable.getGoles()) {
				// perdedor
				partidoEmpatado = 0;
				partidoGanado = 0;
				partidoPerdido = 1;
				puntos = Competencia.PUNTOS_DERROTA;
			}
		}
		return tieneGoles;
	}

	protected abstract Equipo getEquipo();

	public Integer getGoles() {
		return goles;
	}

	public Integer getGolesContra() {
		return golesContra;
	}

	public Integer getGolesFavor() {
		return golesFavor;
	}

	public abstract Partido getPartido();

	public Integer getPartidoEmpatado() {
		return partidoEmpatado;
	}

	public Integer getPartidoGanado() {
		return partidoGanado;
	}

	public Integer getPartidoPerdido() {
		return partidoPerdido;
	}

	public Integer getPuntos() {
		return puntos;
	}

	public void reset() {
		golesContra = null;
		golesFavor = null;
		partidoEmpatado = null;
		partidoGanado = null;
		partidoPerdido = null;
		puntos = null;
	}

	public void setGoles(Integer goles) {
		this.goles = goles;
	}

	public boolean tieneGoles() {
		return getGoles() != null;
	}

}

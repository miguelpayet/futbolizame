package pe.trazos.dominio;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class PronosticoPK implements Serializable {

	@ManyToOne()
	@JoinColumn(name = "idparticipacion", insertable = false, updatable = false)
	private Participacion participacion;
	@ManyToOne()
	@JoinColumn(name = "userid", insertable = false, updatable = false)
	private Visitante visitante;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || o instanceof Pronostico) {
			return false;
		}
		PronosticoPK that = (PronosticoPK) o;
		if (participacion != null ? !participacion.equals(that.participacion) : that.participacion != null) {
			return false;
		}
		return visitante != null ? visitante.equals(that.visitante) : that.visitante == null;
	}

	public Participacion getParticipacion() {
		return participacion;
	}

	public Visitante getVisitante() {

		return visitante;
	}

	@Override
	public int hashCode() {
		int result = participacion != null ? participacion.hashCode() : 0;
		result = 31 * result + (visitante != null ? visitante.hashCode() : 0);
		return result;
	}

	public void setParticipacion(Participacion participacion) {
		this.participacion = participacion;
	}

	public void setVisitante(Visitante visitante) {
		this.visitante = visitante;
	}

}

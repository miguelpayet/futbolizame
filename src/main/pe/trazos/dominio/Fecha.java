package pe.trazos.dominio;

import org.hibernate.annotations.SortComparator;
import pe.trazos.dao.DaoPronostico;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "fecha")
public class Fecha implements Serializable, Comparable<Fecha> {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idconcurso")
	private Competencia competencia;
	@Column(name = "fecha")
	private Date fecha;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idfecha")
	private Integer id;
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "numero")
	private Integer numero;
	@OneToMany(mappedBy = "fecha", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@SortComparator(ComparadorPartido.class)
	private SortedSet<Partido> partidos;

	public Fecha() {
	}

	/**
	 * retorna un indicador si aplican los pronósticos para esta fecha
	 * se puede pronosticar (y se consideran pronósticos) mientras la fecha es posterior a la fecha calendario
	 */
	public boolean aplicaPronostico() {
		return getFecha().after(new Date());
	}

	@Override
	public int compareTo(Fecha o) {
		return fecha.compareTo(o.getFecha());
	}

	public Competencia getCompetencia() {
		return competencia;
	}

	public Date getFecha() {
		return fecha;
	}

	public String getFechaDia() {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		return dateFormat.format(getFecha());
	}

	public Integer getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public Integer getNumero() {
		return numero;
	}

	public Set<Partido> getPartidos() {
		return partidos;
	}

	public void setCompetencia(Competencia competencia) {
		this.competencia = competencia;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	/**
	 * valida si el visitante actual de la competencia ha pronosticado completa la fecha
	 *
	 * @return true si el visitante ha pronosticado completa la fecha
	 */
	public boolean tienePronostico() {
		// ver todos los partidos de la fecha y buscar los pronosticos de cada uno
		// obtiene todos los pronosticos de la fecha y visitante
		DaoPronostico dp = new DaoPronostico();
		Map<Participacion, Pronostico> pronosticos = dp.obtenerValidos(this);
		// compara que cada participante tenga un pronóstico
		boolean completo = true;
		for (Partido part : getPartidos()) {
			for (Participacion parti : part.getParticipantes().values()) {
				completo = completo && pronosticos.containsKey(parti);
			}
		}
		return completo;
	}

	@Override
	public String toString() {
		String titulo;
		String id;
		if (getId() != null) {
			id = String.valueOf(getId());
		} else {
			id = "sin id";
		}
		if (getNombre() != null) {
			titulo = getNombre();
		} else {
			titulo = "sin nombre";
		}
		return String.format("fecha %s - %s", id, titulo);
	}

}

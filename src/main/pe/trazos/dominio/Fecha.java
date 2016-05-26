package pe.trazos.dominio;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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
	@OneToMany(mappedBy = "fecha", cascade = CascadeType.ALL)
	private Set<Partido> partidos;

	public Fecha() {
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

	public void setPartidos(Set<Partido> partidos) {
		this.partidos = partidos;
	}

	@Override
	public String toString() {
		String titulo;
		if (getNombre() != null) {
			titulo = getNombre();
		} else {
			titulo = "sin nombre";
		}
		return String.format("fecha %s", titulo);
	}

}

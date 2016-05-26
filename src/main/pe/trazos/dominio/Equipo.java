package pe.trazos.dominio;

import pe.trazos.dao.DaoEquipo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "equipo")
public class Equipo implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idequipo")
	private String id;
	@Column(name = "logo")
	private String logo;
	@Column(name = "nombre")
	private String nombre;

	public Ratio compararCon(Equipo unEquipo) {
		DaoEquipo de = new DaoEquipo();
		List<Partido> partidosComunes = de.getPartidosComunes(this, unEquipo);
		Ratio ratio = new Ratio();
		for (Partido partid : partidosComunes) {
			ratio.agregarPartido(partid, this);
		}
		return ratio;
	}

	public String getId() {
		return id;
	}

	public String getLogo() {
		return logo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String toString() {
		return (getId() == null) ? "nuevo equipo" : nombre;
	}

}

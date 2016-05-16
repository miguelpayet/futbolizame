package pe.trazos.dominio;

import javax.persistence.*;

@Entity
@Table(name = "rol")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Rol implements IObjetoDominio<Integer> {

	public static final int ADMIN = 1;
	public static final int USUARIO = 2;
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrol")
	private Integer id;
	@Column(name = "nombre")
	private String nombre;

	public Rol() {
	}

	public Integer getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}

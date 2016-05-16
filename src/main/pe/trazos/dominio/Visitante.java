package pe.trazos.dominio;

import javax.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "visitante")
public class Visitante implements IObjetoDominio<String> {

	@Column(name = "fecha")
	Date fecha;
	@Column(name = "nombre")
	String nombre;
	@Column(name = "token")
	String token;
	@Id
	@Column(name = "userid")
	String userId;

	public Date getFecha() {
		return fecha;
	}

	@Override
	public String getId() {
		return userId;
	}

	public String getNombre() {
		return nombre;
	}

	public String getToken() {
		return token;
	}

	public String getUserId() {
		return userId;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Override
	public void setId(String unId) {
		userId = unId;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}

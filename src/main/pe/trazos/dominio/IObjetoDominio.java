package pe.trazos.dominio;

import java.io.Serializable;

public interface IObjetoDominio<T> extends Serializable {

	T getId();

	void setId(T unId);

}

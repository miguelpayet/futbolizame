package pe.trazos.dominio;

public interface IPosicionable {

	void actualizar();

	Integer getGolesContra();

	Integer getGolesFavor();

	Integer getPartidoEmpatado();

	Integer getPartidoGanado();

	Integer getPartidoPerdido();

	Integer getPuntos();

}

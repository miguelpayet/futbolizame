package pe.trazos.dao;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.dominio.Competencia;
import pe.trazos.dominio.Posicion;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeSet;

public class ProviderPosicion implements IDataProvider<Posicion>, Serializable {

	private Competencia competencia;
	private final Logger logger = LoggerFactory.getLogger(ProviderPosicion.class);
	private Integer numero;

	public ProviderPosicion(Competencia unCompetencia) {
		competencia = unCompetencia;
		numero = 1;
	}

	@Override
	public void detach() {
		// método vacío
	}


	@Override
	public Iterator<? extends Posicion> iterator(long first, long count) {
		TreeSet<Posicion> posiciones = new TreeSet<>(competencia.getPosiciones().values());
		return posiciones.iterator();
	}

	@Override
	public IModel<Posicion> model(Posicion object) {
		return Model.of(object);
	}

	@Override
	public long size() {
		if (competencia == null) {
			logger.info("sin competencia");
			return 0;
		}
		if (competencia.getPosiciones() == null) {
			competencia.crearPosiciones();
		}
		return competencia.getPosiciones().size();
	}

}

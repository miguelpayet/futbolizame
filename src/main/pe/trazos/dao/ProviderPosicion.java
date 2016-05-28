package pe.trazos.dao;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.dominio.Posicion;
import pe.trazos.homepage.ModeloCompetencia;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeSet;

public class ProviderPosicion implements IDataProvider<Posicion>, Serializable {

	private static final Logger logger = LoggerFactory.getLogger(ProviderPosicion.class);

	private ModeloCompetencia modelo;
	private Integer numero;

	public ProviderPosicion(ModeloCompetencia unCompetencia) {
		modelo = unCompetencia;
		numero = 1;
	}

	@Override
	public void detach() {
		// método vacío
	}


	@Override
	public Iterator<? extends Posicion> iterator(long first, long count) {
		TreeSet<Posicion> posiciones = new TreeSet<>(modelo.getPosiciones().values());
		return posiciones.iterator();
	}

	@Override
	public IModel<Posicion> model(Posicion object) {
		return Model.of(object);
	}

	@Override
	public long size() {
		if (modelo == null) {
			logger.info("sin competencia");
			return 0;
		}
		if (modelo.getPosiciones() == null) {
			modelo.crearPosiciones();
		}
		return modelo.getPosiciones().size();
	}

}

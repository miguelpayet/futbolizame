package pe.trazos.dao;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.dominio.Concurso;
import pe.trazos.dominio.Posicion;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeSet;

public class ProviderPosicion implements IDataProvider<Posicion>, Serializable {

	private Concurso concurso;
	private final Logger logger = LoggerFactory.getLogger(ProviderPosicion.class);
	private Integer numero;

	public ProviderPosicion(Concurso unConcurso) {
		concurso = unConcurso;
		numero = 1;
	}

	@Override
	public void detach() {
		// método vacío
	}


	@Override
	public Iterator<? extends Posicion> iterator(long first, long count) {
		TreeSet<Posicion> posiciones = new TreeSet<>(concurso.getPosiciones().values());
		return posiciones.iterator();
	}

	@Override
	public IModel<Posicion> model(Posicion object) {
		return Model.of(object);
	}

	@Override
	public long size() {
		if (concurso == null) {
			logger.info("sin concurso");
			return 0;
		}
		if (concurso.getPosiciones() == null) {
			concurso.crearPosiciones();
		}
		return concurso.getPosiciones().size();
	}

}

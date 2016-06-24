package pe.trazos.homepage;

import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.slf4j.LoggerFactory;
import pe.trazos.dao.DaoFecha;
import pe.trazos.dominio.Fecha;

public class ModeloFecha extends LoadableDetachableModel<Fecha> {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(ModeloFecha.class);

	private Integer idFecha;

	public ModeloFecha(Fecha unaFecha) {
		if (unaFecha != null) {
			idFecha = unaFecha.getId();
		}
	}

	@Override
	protected Fecha load() {
		log.debug("load");
		DaoFecha df = new DaoFecha();
		Fecha f = df.get(idFecha);
		return f;
	}

	@Override
	protected void onDetach() {
		log.debug("onDetach");
	}

}

package pe.trazos.homepage;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.PropertyPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import pe.trazos.componentes.ImagePanel;

class ImagePopulatorCompetencia<T> extends PropertyPopulator<T> {

	private String propiedad;

	public ImagePopulatorCompetencia(String property) {
		super(property);
		propiedad = property;
	}

	@Override
	public void populateItem(final Item<ICellPopulator<T>> cellItem, final String componentId, final IModel<T> rowModel) {
		ImagePanel celda = new ImagePanel(componentId, new PropertyModel<>(rowModel, propiedad));
		cellItem.add(celda);
	}

}
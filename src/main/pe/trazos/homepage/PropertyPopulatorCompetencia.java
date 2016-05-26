package pe.trazos.homepage;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.PropertyPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

class PropertyPopulatorCompetencia<T> extends PropertyPopulator<T> {

	private String clase;
	private String propiedad;

	public PropertyPopulatorCompetencia(String unaPropiedad, String unaClase) {
		super(unaPropiedad);
		propiedad = unaPropiedad;
		clase = unaClase;
	}

	@Override
	public void populateItem(final Item<ICellPopulator<T>> cellItem, final String componentId, final IModel<T> rowModel) {
		Label celda = new Label(componentId, new PropertyModel<>(rowModel, propiedad));
		cellItem.add(celda);
		cellItem.add(new AttributeModifier("class", clase));
	}

}
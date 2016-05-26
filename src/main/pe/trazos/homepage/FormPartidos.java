package pe.trazos.homepage;

import org.apache.wicket.markup.html.form.Form;

class FormPartidos extends Form {

	private HomePage homePage;

	public FormPartidos(String id, HomePage unHomePage) {
		super(id);
		homePage = unHomePage;
	}

	@Override
	protected void onSubmit() {
		homePage.formSubmit();
	}

}
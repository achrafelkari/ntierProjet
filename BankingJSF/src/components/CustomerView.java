package components;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneMenu;

@ManagedBean(name = "customerView", eager = true)
@SessionScoped
public class CustomerView implements Serializable {
	private static final long serialVersionUID = -3661528974200035383L;
	private HtmlSelectOneMenu citymenu;
	private HtmlInputText name;
	public void setCitymenu(HtmlSelectOneMenu inputCity) {
		this.citymenu = inputCity;
	}
	public HtmlSelectOneMenu getCitymenu() {
		return citymenu;
	}
	public HtmlInputText getName() {
		return name;
	}
	public void setName(HtmlInputText name) {
		this.name = name;
	}
}

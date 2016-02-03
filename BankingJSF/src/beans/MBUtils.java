package beans;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MBUtils {
	
	public static void setMessage(String label, String mesg) {
		FacesContext.getCurrentInstance().addMessage(label,
				new FacesMessage(mesg));
	}
	
	public static void redirect(String url) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		} catch (IOException ex) {
			ex.printStackTrace();
			// Logger.getLogger(Navigation.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
	}

}

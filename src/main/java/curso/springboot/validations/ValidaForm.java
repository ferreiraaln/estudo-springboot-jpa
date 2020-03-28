package curso.springboot.validations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.ObjectError;

public class ValidaForm {
	
	private List<String> msg = new ArrayList<String>();

	public ValidaForm(List<ObjectError> bind) {
		for(ObjectError objError: bind) {
			this.msg.add(objError.getDefaultMessage());
		}
	}

	public List<String> getMsg() {
		return msg;
	}
}

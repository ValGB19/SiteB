package prode;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.ValidatorAdapter;

public class EnumeMatchValidator extends ValidatorAdapter {
    public void validate(Model m){
	    boolean valid = false; 
	    String value = m.getString("result");
        valid = value == null || "visit".equals(value) || "tie".equals(value) || "local".equals(value) || "suspended".equals(value);
        if(!valid) m.addValidator(this, "Bad value");
   }
}
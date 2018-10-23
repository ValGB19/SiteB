package prode;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.ValidatorAdapter;

public class EnumeMatchValidator extends ValidatorAdapter {
    public void validate(Model m){
	    boolean valid = false; 
	    Object value = m.getString("result");
        if(null == value){
            m.addValidator(this, "result");
            return;
        }
        if(!(value instanceof String))
            throw new IllegalArgumentException("Attribute must be a String");
        valid = "visit".equals(value) || "tie".equals(value) || "local".equals(value) || "suspended".equals(value);
        if(!valid) m.addValidator(this, "Bad value");
   }
}
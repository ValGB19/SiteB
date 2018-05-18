package prode;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.ValidatorAdapter;

public class EnumeMatchValidator extends ValidatorAdapter {
   void validate(Model m){
       	boolean valid = false;
     
		Object value = m.get("result");

        if(null == value) {
            m.addValidator(this, "result");
            return;
        }

        if(!(value instanceof String)) {
            throw new IllegalArgumentException("Attribute must be a String");
        }else{
            if(value == "visit" || value == "tie" || value == "local" || value == "suspended"){
        		valid = true;
        	}
        }


      if(!valid)
         m.addValidator(this, "Bad value");
    }
}
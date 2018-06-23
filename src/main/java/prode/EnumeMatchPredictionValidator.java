package prode;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.ValidatorAdapter;

public class EnumeMatchPredictionValidator extends ValidatorAdapter {
  public void validate(Model m){
       	boolean valid = false;
     
		Object value = m.getString("prediction");

        if(null == value) {
            m.addValidator(this, "prediction");
            valid = true;
        }else{
          if(!(value instanceof String)) {
              throw new IllegalArgumentException("Attribute must be a String");
          }else{
              if("visit".equals(value) || "tie".equals(value) || "local".equals(value)){
              valid = true;
            }
          }
        }
      System.out.println("********************************"+value);
      if(!valid)
         m.addValidator(this, "Bad value");
    }
}
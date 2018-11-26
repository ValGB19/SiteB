package prode;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.ValidatorAdapter;

public class EnumeMatchPredictionValidator extends ValidatorAdapter {
	public void validate(Model m) {
		boolean valid = false;
		String value = m.getString("prediction");
		if (null == value) {
			m.addValidator(this, "prediction");
			valid = true;
			return;
		}
		valid = "visit".equals(value) || "tie".equals(value) || "local".equals(value);
		if (!valid)
			m.addValidator(this, "Bad value");
	}
}
package prode;

import org.javalite.activejdbc.Model;

public class ScheduleScore extends Model {
	static{
    validatePresenceOf("user_id");
    validatePresenceOf("score").message("Please, provide score");
    validateRange("Score", 0, 999999999).message("Score cannot be less than " + 0 + " or more than 999.999.999");
	}
}

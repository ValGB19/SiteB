package prode;

import org.javalite.activejdbc.Model;

public class ScheduleScore extends Model {
	static{
    validatePresenceOf("score").message("Please, provide score");
    validatePresenceOf("user_id").message("Please, provide user_id");

	}
}

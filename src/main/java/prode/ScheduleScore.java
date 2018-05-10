package prode;

import org.javalite.activejdbc.Model;

public class ScheduleScore extends Model {
	static{
    validatePresenceOf("user_id");
    validatePresenceOf("score").message("Please, provide score");
	}
}

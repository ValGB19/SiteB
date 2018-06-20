package prode;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

public class ScheduleScore extends Model {
	static{
    validatePresenceOf("user_id");
    validatePresenceOf("score").message("Please, provide score");
    validateRange("Score", 0, 999999999).message("Score cannot be less than " + 0 + " or more than 999.999.999");
	}
	
	public Fixture getFixture() {
		return null;
	}
	
	public Integer getAcum(Fixture e, User u) {
		LazyList<ScheduleScore> l = Model.findAll();
		int i = 0;
		this.getFixture().getInteger("id");
		l.removeIf((p) -> p.getFixture().getInteger("id") != i);
		int res = 0;
		for (ScheduleScore scheduleScore : l) {
			res += scheduleScore.getInteger("score");
		}
		return res;
	}
}

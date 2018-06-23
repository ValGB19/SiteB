package prode;

import java.util.HashMap;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;

@BelongsToParents({
	@BelongsTo(foreignKeyName="local_team_id",parent=Team.class),
	@BelongsTo(foreignKeyName="visit_team_id",parent=Team.class)
})
public class Match extends Model {
	static{
		validatePresenceOf("day").message("Please, provide date");
    	validatePresenceOf("schedule").message("Please, provide schedule");
    	validateWith(new EnumeMatchValidator());
	}
	
	//@pre schedulescore asociado
	public Fixture getFixture() {
		return Fixture.findFirst("id = ?",get("fixture_id"));
	}
	
	public int getSchedule() {
		return this.getInteger("schedule");
	}
	
	public int getLocal() {
		return getInteger("local_team_id");
	}
	
	public int getVisit() {
		return getInteger("visit_team_id");
	}
	
	public HashMap paraPredic() {
		HashMap res = new HashMap();
		res.put("local",Team.findFirst("id = ?",getInteger("local_team_id")).getString("name"));
		res.put("id",getInteger("schedule"));
		res.put("visit",Team.findFirst("id = ?",getInteger("visit_team_id")).getString("name"));

		return res;
	}
}

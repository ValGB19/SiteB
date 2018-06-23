package prode;

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
	
	public Object[] paraPredic() {
		Object[] res = new Object[3];
		res[0] = Team.findFirst("id = ?",getInteger("local_team_id")).getString("name");
		res[1] = getInteger("schedule");
		res[2] = Team.findFirst("id = ?",getInteger("visit_team_id")).getString("name");
		return res;
	}
}

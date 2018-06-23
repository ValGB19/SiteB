package prode;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;

import java.util.Date;

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
		return Fixture.findById(get("fixture_id"));
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
	
	public Object[] getPartePerfil(){
		Object[] res = new Object[3];
		res[0] = ((Match) Match.findFirst("id = ?", getInteger("match_id"))).getFixture().getString("league") ;//getAll(Match.class).get(0).getFixture().getString("league");
		res[1] = ((Match) Match.findFirst("id = ?", getInteger("match_id"))).getInteger("schedule");
		res[2] = getInteger("score");
		return res;
	}
	
	public Object[] paraPredic() {
		Object[] res = new Object[4];
		res[0] = getInteger("fixture_id");
		res[1] = getInteger("schedule_id");
		res[2] = getInteger("local_team_id");
		res[3] = getInteger("visit_team_id");
		return res;
	}
}

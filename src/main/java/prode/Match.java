package prode;

import java.util.HashMap;
import java.util.function.Predicate;
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
	
	public HashMap<String,Object> paraPredic() {
		HashMap<String,Object> res = new HashMap<String,Object>();
		res.put("local",Team.findFirst("id = ?",getInteger("local_team_id")).getString("name"));
		res.put("id",getInteger("id"));
		res.put("visit",Team.findFirst("id = ?",getInteger("visit_team_id")).getString("name"));
		return res;
	}

	public int idParaPred() {
		return getInteger("id");
	}
	
	public static Predicate<Match> filterById(int idU){
		return (Match x) -> (x.getString("result") != null || "null".equals(x.getString("result")) || new MatchPrediction().comprobaJuego(idU, x.getInteger("id")));
	}
}

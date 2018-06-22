package prode;

import org.javalite.activejdbc.Model;

public class MatchPrediction extends Model{
	static{
    	validatePresenceOf("prediction").message("Please, provide a prediction");
    	validateWith(new EnumeMatchPredictionValidator());
  	}
	
	public String[] getPartePerfil(){
		String[] res = new String[3];
		res[0] = getAll(Match.class).get(0).getFixture().getString("league");
		res[1] = getAll(Match.class).get(0).getInteger("schedule").toString();
		res[2] = this.getInteger("score").toString();
		return res;
	}
	
	//@pre user asociado
	public User getUser() {
		return this.getAll(User.class).get(0);
	}
	
	//@pre schedulescore asociado
	public ScheduleScore getScheduleScore() {
		return this.getAll(ScheduleScore.class).get(0);
	}
	
	//@pre schedulescore asociado
	public Match getMatch() {
		return this.getAll(Match.class).get(0);
	}
	
}

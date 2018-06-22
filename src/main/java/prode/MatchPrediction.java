package prode;

import org.javalite.activejdbc.Model;

public class MatchPrediction extends Model{
	static{
    	validatePresenceOf("prediction").message("Please, provide a prediction");
    	validateWith(new EnumeMatchPredictionValidator());
  	}
	
	public Object[] getPartePerfil(){
		Object[] res = new Object[3];
		res[0] = getAll(ScheduleScore.class).get(0).getInteger("score");
		res[1] = getAll(Match.class).get(0).getInteger("schedule");
		res[2] = getAll(Match.class).get(0).getFixture().getInteger("id");
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

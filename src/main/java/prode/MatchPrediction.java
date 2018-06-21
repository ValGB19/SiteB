package prode;

import org.javalite.activejdbc.Model;

public class MatchPrediction extends Model{
	static{
    	validatePresenceOf("prediction").message("Please, provide a prediction");
    	validateWith(new EnumeMatchPredictionValidator());
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

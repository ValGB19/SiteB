package prode;

import org.javalite.activejdbc.Model;

public class MatchPrediction extends Model{
	static{
    	validatePresenceOf("prediction").message("Please, provide a prediction");
    	validateWith(new EnumeMatchPredictionValidator());
  	}
	
	public String getLeague(){
		return ((Match) Match.findFirst("id = ?", getInteger("match_id"))).getFixture().getString("league");
	}
	
	public Integer getSchedule(){
		return ((Match) Match.findFirst("id = ?", getInteger("match_id"))).getInteger("schedule");
	}
	
	public Integer getScore(){
		return getInteger("score");
	}
	
	//@pre user asociado
	public User getUser() {
		return User.findFirst("id = ?",getInteger("user_id"));
	}
	
	public boolean comprobaJuego(Integer idU, Integer idM) {
		return null != MatchPrediction.findFirst("user_id = ? && match_id = ?", idU, idM); 
	}
	
	//@pre schedulescore asociado
	public Match getMatch() {
		return Match.findFirst("id = ?",getInteger("match_id"));
	}
	
}

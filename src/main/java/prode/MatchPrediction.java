package prode;

import org.javalite.activejdbc.Model;

public class MatchPrediction extends Model{
	static{
    	validatePresenceOf("prediction").message("Please, provide a prediction");
    	validateWith(new EnumeMatchPredictionValidator());
  	}
	
	/**
	 * @return
	 */
	public String getLeague() {
		return ((Match) Match.findFirst("id = ?", getInteger("match_id"))).getFixture().getString("league");
	}
	
	/**
	 * @return
	 */
	public Integer getSchedule() {
		return ((Match) Match.findFirst("id = ?", getInteger("match_id"))).getInteger("schedule");
	}
	
	/**
	 * @return
	 */
	public Integer getScore() {
		return getInteger("score");
	}
	
	//@pre user asociado
	/**
	 * @return
	 */
	public User getUser() {
		return User.findFirst("id = ?",getInteger("user_id"));
	}
	
	/**
	 * 
	 * @param idUser
	 * @param idMatch
	 * @return
	 */
	public boolean checkGame(Integer idUser, Integer idMatch) {
		return MatchPrediction.findFirst("user_id = ? && match_id = ?", idUser, idMatch) != null; 
	}
	
	/**
	 * @return
	 */
	public Match getMatch() {
		return Match.findFirst("id = ?",getInteger("match_id"));
	}
	
}

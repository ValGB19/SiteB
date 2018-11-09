package prode;

import org.javalite.activejdbc.Model;

public class MatchPrediction extends Model{
	static{
    	validatePresenceOf("prediction").message("Please, provide a prediction");
    	validateWith(new EnumeMatchPredictionValidator());
  	}
	
	/**
	 * @return league to which the match prediction belongs
	 */
	public String getLeague() {
		return ((Match) Match.findFirst("id = ?", getInteger("match_id"))).getFixture().getString("league");
	}
	
	/**
	 * @return schedule the match prediction belongs to
	 */
	public Integer getSchedule() {
		return ((Match) Match.findFirst("id = ?", getInteger("match_id"))).getInteger("schedule");
	}
	
	/**
	 * @return score obtained from the prediction
	 */
	public Integer getScore() {
		return getInteger("score");
	}
	
	/**
	 * @return the user who made the prediction
	 */
	public User getUser() {
		return User.findFirst("id = ?",getInteger("user_id"));
	}
	
	/**
	 * Check that if a given User makes a prediction about the given match.
	 * @param idUser
	 * @param idMatch
	 * @return true if the user predicted the match
	 */
	public boolean checkGame(Integer idUser, Integer idMatch) {
		return MatchPrediction.findFirst("user_id = ? && match_id = ?", idUser, idMatch) != null; 
	}
	
	/**
	 * @return Match to which the prediction belongs
	 */
	public Match getMatch() {
		return Match.findFirst("id = ?",getInteger("match_id"));
	}
	
}

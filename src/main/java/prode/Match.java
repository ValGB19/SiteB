package prode;

import java.util.HashMap;
import java.util.function.Predicate;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;

@BelongsToParents({ 
	@BelongsTo(foreignKeyName = "local_team_id", parent = Team.class),
	@BelongsTo(foreignKeyName = "visit_team_id", parent = Team.class)})
public class Match extends Model {

	static {
		validatePresenceOf("day").message("Please, provide date");
		validatePresenceOf("schedule").message("Please, provide schedule");
		validateWith(new EnumeMatchValidator());
	}

	/**
	 * @return fixture to which the match belongs
	 */
	public Fixture getFixture() {
		return Fixture.findFirst("id = ?", get("fixture_id"));
	}

	/**
	 * @return schedule the match belongs to
	 */
	public int getSchedule() {
		return this.getInteger("schedule");
	}

	/**
	 * @return the local team of the match
	 */
	public int getLocal() {
		return getInteger("local_team_id");
	}

	/**
	 * @return the visiting team of the match
	 */
	public int getVisit() {
		return getInteger("visit_team_id");
	}

	/**
	 * @return id of the match
	 */
	public int getID() {
		return getInteger("id");
	}

	/**
	 * Get and return the data of a match
	 * 
	 * @pre Match id =! null, local_team_id =! and visit_team_id
	 * @return the data of a match in a HashMap
	 */
	public HashMap<String, Object> forPrediction() {
		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("local", Team.findFirst("id = ?", getInteger("local_team_id")).getString("name"));
		res.put("id", getID());
		res.put("visit", Team.findFirst("id = ?", getInteger("visit_team_id")).getString("name"));
		return res;
	}

	/**
	 * @param idUser
	 * @return Predicate for the Match class on whether the user predicted the match
	 *         or if the match was played
	 */
	public static Predicate<Match> filterById(int idUser) {
		return (Match x) -> (null != x.getString("result"))
				|| new MatchPrediction().checkGame(idUser, x.getInteger("id"));
	}

	/**
	 * @param idUser
	 * @return Predicate for the Match class on whether the user predicted the match
	 *         or if the match was played
	 */
	public static Predicate<Match> notPlayed() {
		return (Match x) -> null != x.getString("result");
	}
}

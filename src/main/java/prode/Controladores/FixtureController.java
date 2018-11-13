package prode.Controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import spark.*;
import prode.*;

public class FixtureController {

	/**
	 * Returns a view for the administrator to upload results.
	 *
	 * @param req the request which contains the username.
	 * @param res the response.
	 * @return an ModelAndView to show.
	 */
	public static TemplateViewRoute mainFixturesAdmin = (req, res) -> {
		GeneralController.map.put("nick", req.session().attribute("username"));
		GeneralController.map.put("fixs", Fixture.getAllFixtures());
		return new ModelAndView(GeneralController.map, "./src/main/resources/loged/admin.mustache");
	};

	/**
	 * Returns a view for the user to bet in the prode.
	 *
	 * @param req the request which contains the usename.
	 * @param res the response.
	 * @return an ModelAndView to show.
	 */
	public static TemplateViewRoute mainFixturesPlayer = (req, res) -> {
		GeneralController.map.put("nick", req.session().attribute("username"));
		GeneralController.map.put("fixs", Fixture.getAllFixtures());
		return new ModelAndView(GeneralController.map, "./src/main/resources/loged/prode.mustache");
	};

	/**
	 * Returns the first Fixture name.
	 *
	 * @param req the request which contains the id from the fixture.
	 * @return an ModelAndView to show.
	 */
	private static String getFstFixture(Request req) {
		List<String> listFixtures = Fixture.getAllFixtures();
		int index = 0;
		String fix = null;
		while (fix == null && index < listFixtures.size()) {
			fix = req.queryParams(listFixtures.get(index));
			index++;
		}
		if (fix == null) {
			return null;
		}
		return listFixtures.get(index - 1);
	}

	/**
	 * Set in the session the attribute <code>"lastFixture"</code> , load the map
	 * with the match and redirect to "/loged/admin".
	 *
	 * @param req the request which contains the id from the fixture.
	 * @param res the response.
	 * @return nothing.
	 */
	public static TemplateViewRoute viewProdeScheduleAdmin = (req, res) -> {
		GeneralController.map.put("nick", req.session().attribute("username"));
		String fix = getFstFixture(req);
		if (fix == null)
			return null;
		req.session().attribute("lastFixture", fix);
		List<Match> listMatches = new Fixture().getFix(fix).getMatch();
		listMatches.removeIf((x) -> x.getString("result") != null);
		getFromMatchToShow(listMatches, GeneralController.map, fix);
		res.redirect("/loged/admin");
		return null;
	};

	/**
	 * Set in the session the attribute <code>"lastFixture"</code> , load the map
	 * with the match and redirect to "/loged/prode".
	 *
	 * @param req the request which contains the id from the fixture.
	 * @param res the response.
	 * @return nothing.
	 */
	public static TemplateViewRoute viewProdeSchedulePlayer = (req, res) -> {
		GeneralController.map.put("nick", req.session().attribute("username"));
		String fix = getFstFixture(req);
		res.redirect("/loged/prode");
		if (fix == null)
			return null;
		req.session().attribute("lastFixture", fix);
		int idUser = new User().getUser(req.session().attribute("username")).getInteger("id");
		List<Match> listMatches = new Fixture().getFix(fix).getMatch();
		listMatches.removeIf(Match.filterById(idUser));
		getFromMatchToShow(listMatches, GeneralController.map, fix);
		return null;
	};

	/**
	 * It puts on the map the current Fixture number, the name of the last fixture
	 * and the Match data that make it up.
	 * 
	 * @param list        the list of the matchs
	 * @param map         the map to fill
	 * @param lastFixture the number of the last fixture
	 */
	private static void getFromMatchToShow(List<Match> list, Map<String, Object> map, String lastFixture) {
		ArrayList<Map<String, Object>> listDataMatch = new ArrayList<Map<String, Object>>();
		if (list.size() != 0) {
			int schedule = list.get(0).getInteger("schedule");
			list.removeIf((x) -> x.getInteger("schedule") != schedule);
			map.put("scheduleCurrent", schedule);
			map.put("lastFixture", lastFixture);
			for (Match match : list) {
				listDataMatch.add(match.forPrediction());
			}
		}
		map.put("fixtureCurrent", listDataMatch);
	}
}
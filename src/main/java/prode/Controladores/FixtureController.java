package prode.Controladores;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.LazyList;

import spark.*;
import prode.*;
import prode.Utils.Consts;

public class FixtureController {

	/**
	 * Returns a view for the administrator to upload results.
	 *
	 * @param req the request which contains the username.
	 * @param res the response.
	 * @return an ModelAndView to show.
	 */
	public static TemplateViewRoute mainFixtures = (req, res) -> {
		return new ModelAndView(GeneralController.map, "./src/main/resources/admin/main.mustache");
	};

	/**
	 * Returns a view for the user to bet.
	 *
	 * @param req the request which contains the username.
	 * @param res the response.
	 * @return an ModelAndView to show.
	 */
	public static TemplateViewRoute betView = (req, res) -> {
		String user = req.session().attribute(Consts.ATTRIBUTEUSERNAME);
		int idU = new User().getUser(user).getInteger("id");
		LazyList<Fixture> fixtureList = Fixture.getAllFixturesAvailables();
		fixtureList.removeIf((Fixture f) ->{
			List<Match> list = new Fixture().getFix((String) f.get("league")).getMatch();
			list.removeIf(Match.filterById(idU));
			return list.isEmpty();});
		GeneralController.map.put("fixs", fixtureList.collect("league"));
		return new ModelAndView(GeneralController.map, "./src/main/resources/loged/prode.mustache");
	};
	
	/**
	 * Returns the first Fixture name.
	 *
	 * @param req the request which contains the id from the fixture.
	 * @return an ModelAndView to show.
	 */
	private static String getFstFixture(Request req) { //make it hard test
		String fix = "action";
		Iterator<String> i = req.queryParams().iterator();
		while(i.hasNext() && "action".equals(fix))
			fix = i.next(); 
		if ("action".equals(fix))
			return null;
		return fix;
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
		GeneralController.map.put("nick", req.session().attribute(Consts.ATTRIBUTEUSERNAME));
		String fix = getFstFixture(req);
		res.redirect("/admin/main");
		if (fix == null)
			return null;
		req.session().attribute(Consts.ATTRIBUTELASTFIXTURE, fix);
		List<Match> listMatches = new Fixture().getFix(fix).getMatch();
		listMatches.removeIf((x) -> x.getString("result") != null);
		getFromMatchToShow(listMatches, GeneralController.map, fix);
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
		String fix = getFstFixture(req);
		if (fix == null)
			return null;
		req.session().attribute(Consts.ATTRIBUTELASTFIXTURE, fix);
		int idUser = new User().getUser(req.session().attribute(Consts.ATTRIBUTEUSERNAME)).getInteger("id");
		List<Match> listMatches = new Fixture().getFix(fix).getMatch();
		listMatches.removeIf(Match.filterById(idUser));
		getFromMatchToShow(listMatches, GeneralController.map, fix);
		res.redirect("/loged/prode");
		return null;
	};

	
	public static TemplateViewRoute loadCountry = (req,res) ->{
		String countryName = req.queryParams("sendContrys");
		if(countryName != null) {
			Country c = new Country();
			c.set("name",countryName);
			c.save();
		}
		res.redirect("/admin/main");
		return null;
	} ;

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
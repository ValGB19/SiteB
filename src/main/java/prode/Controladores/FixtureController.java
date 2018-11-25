package prode.Controladores;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
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
		int idU = User.getUser(user).getInteger("id");
		LazyList<Fixture> fixtureList = Fixture.getAllFixturesAvailables();
		fixtureList.removeIf((Fixture f) -> {
			List<Match> list = new Fixture().getFix((String) f.get("league")).getMatch();
			list.removeIf(Match.filterById(idU));
			return list.isEmpty();
		});
		GeneralController.map.put("fixs", fixtureList.collect("league"));
		return new ModelAndView(GeneralController.map, "./src/main/resources/loged/prode.mustache");
	};

	/**
	 * Returns the first Fixture name.
	 *
	 * @param req the request which contains the id from the fixture.
	 * @return an ModelAndView to show.
	 */
	private static String getFstFixture(Request req) {
		String fix = "action";
		Iterator<String> i = req.queryParams().iterator();
		while (i.hasNext() && "action".equals(fix))
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
		if (fix == null) {
			res.redirect("/admin/main");
			return null;
		}
		req.session().attribute(Consts.ATTRIBUTELASTFIXTURE, fix);
		List<Match> listMatches = new Fixture().getFix(fix).getMatch();
		listMatches.removeIf((x) -> x.getString("result") != null);
		getFromMatchToShow(listMatches, GeneralController.map, fix);
		res.redirect("/admin/main");
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
		if (fix == null) {
			res.redirect("/loged/prode");
			return null;
		}
		int idUser = User.getUser(req.session().attribute(Consts.ATTRIBUTEUSERNAME)).getInteger("id");
		List<Match> listMatches = new Fixture().getFix(fix).getMatch();
		listMatches.removeIf(Match.filterById(idUser));
		getFromMatchToShow(listMatches, GeneralController.map, fix);
		req.session().attribute(Consts.ATTRIBUTELASTFIXTURE, fix);
		res.redirect("/loged/prode");
		return null;
	};

	public static ModelAndView saveModel(Request req, Response res, Model c, String[] fields, String[] info) {
		if (!GeneralController.checkQueryParams(req, info)) {
			res.redirect("/admin/main");
			return null;
		}
		for (int i = 0; i< fields.length; i++) {
			c.set(fields[i], req.queryParams(info[i]));
		}
		c.save();
		res.redirect("/admin/main");
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

	public static ModelAndView loadGame(Request req, Response res) {
		if(!GeneralController.checkQueryParams(req, "visitante","local","fixture","date")) {
			GeneralController.map.put("messageLoadgame", "Juego no guardado, faltan parametros");
			res.redirect("/admin/main");
			return null;
		}
		String visitante = req.queryParams("visitante");
		String local = req.queryParams("local");
		Team v = Team.findFirst("name = ?", visitante);
		Team l = Team.findFirst("name = ?", local);
		Fixture f = Fixture.findFirst("league = ?", req.queryParams("fixture"));
		Integer i = Integer.parseInt(req.queryParams("fecha"));
		if(visitante.equals(local) || i <= 0 || v == null || l == null) {
			GeneralController.map.put("messageLoadgame", "Juego no guardado, parametros incorrectos");
			res.redirect("/admin/main");
			return null;
		}
		Match m = new Match();
		m.set("schedule", i, 
				"fixture_id", f.getId(), 
				"local_team_id", l.getId(), 
				"visit_team_id", v.getId(),
				"day", LocalDate.parse(req.queryMap("date").value()));
		m.saveIt();
		res.redirect("/admin/main");
		return null;
	}
}
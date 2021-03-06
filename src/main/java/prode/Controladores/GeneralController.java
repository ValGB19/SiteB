package prode.Controladores;

import spark.*;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import prode.*;
import prode.Utils.Consts;
import org.javalite.activejdbc.Base;

public class GeneralController {

	public static Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * Open the connection to the data base
	 */
	public static Filter openConectionToDataBase = (req, res) -> {
		if (!Base.hasConnection())
			Base.open("com.mysql.jdbc.Driver",
					"jdbc:mysql://127.0.0.1/prode?nullNamePatternMatchesAll=true&useSSL=false", "root", "root");
	};

	/**
	 * Close the connection to the data base
	 */
	public static Filter disconectDataBase = (req, res) -> {
		if (Base.hasConnection()) {
			Base.close();
		}
	};

	/**
	 * If the user is not login, redirect him to "/"
	 */
	public static Filter checkIfLoged = (req, res) -> {
		if (req.session().attribute(Consts.ATTRIBUTELOGED) == null)
			res.redirect("/");
		else if ((boolean) req.session().attribute(Consts.ATTRIBUTEADMIN))
			res.redirect("/admin/main");
	};

	/**
	 * If the user is not login, redirect him to "/loged/admin"
	 */
	public static Filter checkIfAdmin = (req, res) -> {
		if (req.session().attribute(Consts.ATTRIBUTELOGED) == null) {
			res.redirect("/");
			return;
		}
		if (!((boolean) req.session().attribute(Consts.ATTRIBUTEADMIN)))
			res.redirect("/loged/perfil");
	};

	/**
	 * Checks if the user wants bet or view the fixture
	 */
	public static TemplateViewRoute bet = (req, res) -> {
		switch (req.queryParams("action")) {
		case "fixtures":
			return FixtureController.viewProdeSchedulePlayer.handle(req, res);
		case "matches":
			return PredictionController.cargarPrediction.handle(req, res);
		}
		res.redirect("/loged/prode");
		return null;
	};

	/**
	 * Checks if the user wants bet or view the fixture
	 */
	public static TemplateViewRoute actionAdmin = (req, res) -> {
		map.remove("messageLoadgame");
		map.put("allTeams", Team.getAllTeams());
		map.put("allFixtures", Fixture.getAllFixtures());
		switch (req.queryParams("action")) {
		case "fixtures":
			return FixtureController.viewProdeScheduleAdmin.handle(req, res);
		case "fixAvailable":
			GeneralController.map.put("fixs", Fixture.getAllFixturesAvailables().collect("league"));
			res.redirect("/admin/main");
			return null;
		case "loadCountry":
			return FixtureController.saveModel(req, res, new Country(), new String[] {"name"}, new String[]{"sendContrys"});
		case "loadGame":
			return FixtureController.loadGame(req,res);
		case "loadTeam":
			return FixtureController.saveModel(req, res, new Team(), new String[]{"name"}, new String[]{"team"});
		case "loadFixture":
			return FixtureController.saveModel(req, res,new Fixture() , new String[]{"league"}, new String[] {"sendFixture"});
		case "matches":
			return PredictionController.cargaResulMatch.handle(req, res);
		default:
			res.redirect("/admin/main");
		}
		return null;
	};

	/**
	 * Sum all the items in the List of List and return the accumulated
	 * 
	 * @param listOflist a <code>List</code> of <code>List</code>
	 * @return the result to sum all the items in the List of List
	 */
	private static Integer foldrSum(List<List<Object>> listoflist) {
		Integer result = 0;
		for (List<Object> list : listoflist) {
			result = result + (Integer) list.get(0);
		}
		return result;
	}

	/**
	 * Take a List of List and group it him by the first component.
	 * 
	 * @param listoflist a <code>List</code> of <code>List</code>
	 * @return a <code>HashMap</code> grouped by the first component of the list
	 */
	private static HashMap<Object, List<List<Object>>> groupByFstField(List<List<Object>> listoflist) {
		HashMap<Object, List<List<Object>>> resultList = new HashMap<Object, List<List<Object>>>();
		List<List<Object>> auxList = new ArrayList<List<Object>>();
		for (List<Object> l : listoflist) {
			Object aux = l.get(0);
			l.remove(0);
			if (resultList.containsKey(aux))
				resultList.get(aux).add(l);
			else {
				auxList = new ArrayList<List<Object>>();
				auxList.add(l);
				resultList.put(aux, auxList);
			}
		}
		return resultList;
	}

	/**
	 * Take a map and transform it into a list
	 * 
	 * @param map the map that is going to be ungrouped
	 * @return a List of List witch contains all the map
	 */
	private static List<List<Object>> unGroup(HashMap<Object, List<List<Object>>> map) {
		ArrayList<List<Object>> res = new ArrayList<List<Object>>();
		List<Object> list = new ArrayList<Object>();
		for (Object k : map.keySet()) {
			for (List<Object> j : getAcum(map.get(k))) {
				list = new ArrayList<Object>();
				list.add(k);
				list.addAll(j);
				res.add(list);
			}
		}
		return res;
	}

	/**
	 * Take a <code>List</code> from <code>List</code> and if the size of the inner
	 * list is 0, return to the <code>List</code> with the cumulative.
	 * 
	 * @param listOflist a <code>List</code> of <code>List</code>
	 * @return a list of list
	 */
	public static List<List<Object>> getAcum(List<List<Object>> listOflist) {
		if (listOflist == null || listOflist.size() == 0)
			return null;
		if (listOflist.get(0).size() == 1) {
			List<Object> l = new ArrayList<Object>();
			List<List<Object>> arr = new ArrayList<List<Object>>();
			l.add(foldrSum(listOflist));
			arr.add(l);
			return arr;
		}
		return unGroup(groupByFstField(listOflist));
	}

	public static boolean checkQueryParams(Request req, String... s) {
		boolean b = req.queryParams().containsAll(Arrays.asList(s));
		if (!b) return b;
		for (String string : s)
			if ("".equals(req.queryParams(string)))
				b = false;
		return b;
	}
}
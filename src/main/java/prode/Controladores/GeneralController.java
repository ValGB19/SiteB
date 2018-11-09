package prode.Controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.javalite.activejdbc.Base;
import spark.*;

public class GeneralController {

	/**
	 * Open the connection to the data base
	 */
	public static Filter openConectionToDataBase = (req, res) -> {
		if (!Base.hasConnection()) {
			Base.open("com.mysql.jdbc.Driver",
					"jdbc:mysql://127.0.0.1/prode?nullNamePatternMatchesAll=true&useSSL=false", "root", "root");
		}
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
		if (req.session().attribute("logueado") == null) {
			res.redirect("/");
		}
	};

	/**
	 * Checks if the user wants bet or view the fixture
	 */
	public static TemplateViewRoute redicProde = (req, res) -> {
		if (req.queryParams("action").equals("fixtures"))
			return FixtureController.viewProdeSchedulePlayer.handle(req, res);
		return PredictionController.cargarPrediction.handle(req, res);
	};

	/**
	 * Check if the administrator wants load or view the fixture
	 */
	public static TemplateViewRoute redicAdmin = (req, res) -> {
		if (req.queryParams("action").equals("fixtures"))
			return FixtureController.viewProdeScheduleAdmin.handle(req, res);
		return PredictionController.cargaResulMatch.handle(req, res);
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
}
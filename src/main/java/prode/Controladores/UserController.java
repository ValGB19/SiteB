package prode.Controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.*;
import prode.*;
import prode.Utils.Consts;

public class UserController {

	/**
	 * Register the user or place the reasons why they did not register it in the
	 * hashmap
	 * 
	 * @param req The request
	 * @param res The response
	 * @return a <code>HashMap</code> with the info of the errors
	 */
	public static HashMap<String, Object> register(Request req, Response res) {
		GeneralController.map.remove("errorLogin");
		GeneralController.map.remove("errorRegister");
		Map<String, String> data = new HashMap<String, String>();
		data.put("nick", req.queryParams("rUsername"));
		data.put("pwd", req.queryParams("pswRegister"));
		data.put("pwd2", req.queryParams("pswValida"));
		data.put("name", req.queryParams("nombre"));
		data.put("surname", req.queryParams("apellido"));
		data.put("email", req.queryParams("mail"));
		data.put("country", req.queryParams("rpais"));
		data.put("dni", req.queryParams("rdni"));
		data.put("key", req.queryParams("clave"));
		boolean isSaved = false;
		if ((data.get("pwd")).equals(data.get("pwd2")) && data.get("dni").length() <= 8) {
			User temp = new User();
			isSaved = temp.setUserTemp(data);
		}
		HashMap<String, Object> mape = new HashMap<String, Object>();
		if (!isSaved) {
			ArrayList<String> tmp = errorsRegister(data);
			mape.put("errorRegister", tmp);
		}
		return mape;
	};

	/**
	 * It take a map with data and analyze them.
	 * 
	 * @param data the map with the info to be analyzed
	 * @return a list of <code>String</code> with info of the errors
	 */
	private static ArrayList<String> errorsRegister(Map<String, String> data) {
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.add("Datos incorrectos");
		addError((!data.get("pwd").equals(data.get("pwd2"))), "*Las contrase?s no coinciden", tmp);
		addError((User.findFirst("nick = ?", data.get("nick")) != null), "*El nickname ya esta en uso", tmp);
		addError((User.findFirst("dni = ?", Integer.parseInt(data.get("dni"))) != null), "*Ese dni ya esta registrado",
				tmp);
		addError((User.findFirst("email = ?", data.get("email")) != null), "*Ese email ya esta registrado", tmp);
		addError((!"traemelapromocionmessi".equals(data.get("key")) && (data.get("key")) != null),
				"*Palabla magica incorrecta", tmp);
		return tmp;
	}

	/**
	 * If condition is true add to the list the message
	 * 
	 * @param condition a boolean to check
	 * @param message   the message to be place in the list
	 * @param list      the list to be load
	 */
	private static void addError(Boolean condition, String message, ArrayList<String> list) {
		if (condition)
			list.add(message);
	}

	/**
	 * Try to log the user in the system
	 */
	public static TemplateViewRoute login = (req, res) -> {
		GeneralController.map.remove("errorLogin");
		GeneralController.map.remove("errorRegister");
		String username = req.queryParams("usernamelogin");
		String psw = req.queryParams("pswLogin");
		boolean log = false;
		String errorMessage = "";
		if (User.log(username, psw)) {
			req.session(true);
			req.session().attribute(Consts.ATTRIBUTEUSERNAME, username);
			req.session().attribute(Consts.ATTRIBUTELOGED, true);
			if (User.findFirst("nick = ?", username).getBoolean("admin")) {
				req.session().attribute(Consts.ATTRIBUTEADMIN, true);
				GeneralController.map.put("admin", true);
			}else{
				req.session().attribute(Consts.ATTRIBUTEADMIN, false);
			}
			log = true;
			String name = ((User) User.findFirst("nick = ?", username)).getNameUser();
			String surname = ((User) User.findFirst("nick = ?", username)).getSurnameUser();
			GeneralController.map.put("nick", username);
			GeneralController.map.put("name", name);
			GeneralController.map.put("surname", surname);
			System.out.println("Loged " + username);
		} else {
			errorMessage = "Los datos ingresados son incorrectos";
		}
		if (log) {
			res.redirect("/loged/profile");
			return null;
		}
		res.redirect("/");
		GeneralController.map.put("errorLogin", errorMessage);
		return null;
	};

	/**
	 * Load in the map the predictions of the user logged
	 */
	public static TemplateViewRoute contain2Perfil = (req, res) -> {
		String username = req.session().attribute(Consts.ATTRIBUTEUSERNAME);
		User user = (User.findFirst("nick = ?", username));
		ArrayList<List<Object>> predUser = new ArrayList<List<Object>>();
		List<Object> list;
		for (MatchPrediction matchPrediction : user.getMatchPrediction()) {
			list = new ArrayList<Object>();
			list.add(matchPrediction.getLeague());
			list.add(matchPrediction.getSchedule());
			list.add(matchPrediction.getScore());
			predUser.add(list);
		}
		GeneralController.map.remove("lastFixture");
		GeneralController.map.remove("fixtureCurrent");
		GeneralController.map.remove("scheduleCurrent");
		GeneralController.map.put("nick", username);
		GeneralController.map.put("totalPredictions", user.getTotalMatchPrediction().size());
		GeneralController.map.put("totalFixtures", new UsersFixtures().totalFixturesUser(user.getInteger("id")));
		GeneralController.map.put("predUser", GeneralController.getAcum(predUser));
		return new ModelAndView(GeneralController.map, "./src/main/resources/loged/perfil.mustache");
	};

	/**
	 * Check if the user is logged. Otherwise redirect him to "/loged/profile"
	 */
	public static TemplateViewRoute redicProfile = (req, res) -> {
		if (req.session().attribute(Consts.ATTRIBUTELOGED) != null) {
			res.redirect("/loged/profile");
			return null;
		} else {
			GeneralController.map.put("countries", Country.getAllCountrys());
		}
		return new ModelAndView(GeneralController.map, "./src/main/resources/inicio.mustache");
	};

	/**
	 * If the user was logged, it close his session and clear the map.
	 */
	public static Filter closeSession = (req, res) -> {
		if (req.session().attribute(Consts.ATTRIBUTELOGED) != null) {
			req.session().removeAttribute("logueado");
			if (req.session().attribute(Consts.ATTRIBUTEADMIN) != null) {
				req.session().removeAttribute("admin");
				GeneralController.map.remove("admin");
			}
		}
		GeneralController.map.remove("lastFixture");
		GeneralController.map.remove("schedule");
		GeneralController.map.remove("errorLogin");
		GeneralController.map.remove("errorRegister");
		res.redirect("/");
	};

	/**
	 * Check if the user wants sign in o sign out
	 */
	public static TemplateViewRoute home = (req, res) -> {
		if (req.queryParams("action").equals("signin"))
			return login.handle(req, res);
		GeneralController.map.putAll(register(req, res));
		res.redirect("/");
		return null;
	};

}
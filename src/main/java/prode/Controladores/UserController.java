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
	 * Register the user or place the reasons why they did not register it in the hashmap
	 * 
	 * @param req The request
	 * @param res The response
	 * @return a <code>HashMap</code> with the info of the errors
	 */
	public static HashMap<String, Object> register(Request req) {
		GeneralController.map.remove("errorLogin");
		GeneralController.map.remove("errorRegister");
		GeneralController.map.remove("messageReset");
		Map<String, String> data = new HashMap<String, String>();
		data.put("nick", req.queryParams("rUsername"));
		data.put("pwd", req.queryParams("pswRegister"));
		data.put("pwd2", req.queryParams("pswValida"));
		data.put("name", req.queryParams("nombre"));
		data.put("surname", req.queryParams("apellido"));
		data.put("email", req.queryParams("mail"));
		data.put("clave", req.queryParams("seguro"));
		data.put("country", req.queryParams("rpais"));
		data.put("dni", req.queryParams("rdni"));
		data.put("key", req.queryParams("clave"));
		boolean isSaved = false;
		if (data.get("pwd").equals(data.get("pwd2")) && data.get("dni").length() <= 8) {
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
		addError((User.findFirst("nick = ?", data.get("nick")) != null), "*El nickname ya esta en uso", tmp);
		addError((User.findFirst("dni = ?", Integer.parseInt(data.get("dni"))) != null), "*Ese dni ya esta registrado",
				tmp);
		addError(!(data.get("pwd").equals(data.get("pwd2"))), "*Las contraseñas no coinciden", tmp);
		addError((User.findFirst("email = ?", data.get("email")) != null), "*Ese email ya esta registrado", tmp);
		addError((!Consts.SECRETWORD.equals(data.get("key")) && (data.get("key")) != null),
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
		GeneralController.map.remove("messageReset");
		String username = req.queryParams("usernamelogin");
		String psw = req.queryParams("pswLogin");
		String errorMessage = "";
		if (GeneralController.checkQueryParams(req, "usernamelogin", "pswLogin") && User.log(username, psw)) {
			req.session(true);
			req.session().attribute(Consts.ATTRIBUTEUSERNAME, username);
			req.session().attribute(Consts.ATTRIBUTELOGED, true);
			boolean adm = User.findFirst("nick = ?", username).getBoolean("admin");
			req.session().attribute(Consts.ATTRIBUTEADMIN, adm);
			String name = ((User) User.findFirst("nick = ?", username)).getNameUser();
			String surname = ((User) User.findFirst("nick = ?", username)).getSurnameUser();
			GeneralController.map.put("nick", username);
			GeneralController.map.put("name", name);
			GeneralController.map.put("surname", surname);
			GeneralController.map.put("allTeams", Team.getAllTeams());
			GeneralController.map.put("allFixtures", Fixture.getAllFixtures());
			if (adm)
				res.redirect("/admin/main");
			else
				res.redirect("/loged/perfil");
			return null;
		} else {
			errorMessage = "Los datos ingresados son incorrectos";
			res.redirect("/");
		}
		GeneralController.map.put("errorLogin", errorMessage);
		return null;
	};

	/**
	 * Load in the map the predictions of the user logged
	 */
	public static TemplateViewRoute viewPerfil = (req, res) -> {
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
		GeneralController.map.put("totalPredictions", user.getTotalMatchPrediction().size());
		GeneralController.map.put("totalFixtures", new UsersFixtures().totalFixturesUser(user.getInteger("id")));
		GeneralController.map.put("predUser", GeneralController.getAcum(predUser));
		return new ModelAndView(GeneralController.map, "./src/main/resources/loged/perfil.mustache");
	};

	/**
	 * Check if the user is logged. Otherwise redirect him to "/loged/profile"
	 */
	public static TemplateViewRoute gHome = (req, res) -> {
	
		if(req.session().attribute(Consts.ATTRIBUTELOGED) != null){
			if ((!(boolean) req.session().attribute(Consts.ATTRIBUTEADMIN)))
				res.redirect("/loged/perfil");
			else
				res.redirect("/admin/main");
			return null;
		}
		GeneralController.map.put("countries", Country.getAllCountrys());
		return new ModelAndView(GeneralController.map, "./src/main/resources/inicio.mustache");
	};

	/**
	 * Check if the user wants sign in o sign out
	 */
	public static TemplateViewRoute pHome = (req, res) -> {
		GeneralController.map.remove("messageReset");
		if (GeneralController.checkQueryParams(req, "action")) {
			if (req.queryParams("action").equals("signin"))
				return login.handle(req, res);
			if (req.queryParams("action").equals("signup"))
				GeneralController.map.putAll(register(req));
		}
		res.redirect("/");
		return null;
	};

	public static TemplateViewRoute gResetPass = (req, res) -> {
		return new ModelAndView(GeneralController.map, "./src/main/resources/public/reset.mustache");
	};

	/**
	 * Check that the user's password is changed correctly.
	 */
	public static TemplateViewRoute pSavePass = (req, res) -> {
		String id = req.queryParams("resetID");
		if (req.queryParams("psw").equals(req.queryParams("pswReset"))) {
			int i;
			if (id.contains("@"))
				i = User.update("password = ?", "email = ?", req.queryParams("psw"), id);
			else
				i = User.update("password = ?", "nick = ?", req.queryParams("psw"), id);
			if (i == 1) {
				GeneralController.map.put("resetPass", true);
				res.redirect("/exit");
				return null;
			}
			else
				GeneralController.map.put("messageReset", "* Error al guardar");
		} else {
			GeneralController.map.put("messageReset", "* Las contraseñas no coinciden ");
		}
		res.redirect("/reset");
		return null;
	};

	/**
	 * Check that when wanting to change the password the user name or email and the
	 * security word are valid
	 */
	public static TemplateViewRoute pResetPass = (req, res) -> {
		GeneralController.map.remove("messageReset");
		GeneralController.map.remove("resetID");
		String id = req.queryParams("resetID");
		String seguro = req.queryParams("seguro");
		User us;
		if (!GeneralController.checkQueryParams(req, "resetID", "seguro", "psw", "pswReset")) {
			GeneralController.map.put("messageReset", "* Faltan datos");
			res.redirect("/reset");
			return null;
		}
		if (id.contains("@"))
			us = User.getUserforMail(id);
		else
			us = User.getUser(id);
		if (us != null) {
			if (req.session().attribute(Consts.ATTRIBUTEUSERNAME) != null) 
					if(req.session().attribute(Consts.ATTRIBUTEUSERNAME).equals(us.getString("nick")))
						if (us.getString("clave").equals(seguro))
							return pSavePass.handle(req, res);
						else
							GeneralController.map.put("messageReset", "* Palabra de seguridad incorrecta");
					else
						GeneralController.map.put("messageReset", "* Ingresa correctamente tu nombre de usuario");
			else
				if (us.getString("clave").equals(seguro))
					return pSavePass.handle(req, res);
				else
					GeneralController.map.put("messageReset", "* Palabra de seguridad incorrecta");					
		} else
			GeneralController.map.put("messageReset", "* Usuario inexistente");
		res.redirect("/reset");
		return null;
	};
	
	public static TemplateViewRoute gResetEmail = (req, res) -> {
		return new ModelAndView(GeneralController.map, "./src/main/resources/loged/resetEmail.mustache");
	};
	
	public static TemplateViewRoute gResetEmailAdmin = (req, res) -> {
		return new ModelAndView(GeneralController.map, "./src/main/resources/admin/resetEmail.mustache");
	};
	
	public static TemplateViewRoute pSaveEmail = (req, res) -> {
		GeneralController.map.remove("messageResetEmail");
		String username = req.session().attribute(Consts.ATTRIBUTEUSERNAME);
		boolean ad = req.session().attribute(Consts.ATTRIBUTEADMIN);
		if(User.findFirst("email = ?", req.queryParams("mail")) == null) {
			int i =	User.update("email = ?", "nick = ?", req.queryParams("mail"), username);
			if (i == 1)
				GeneralController.map.put("messageResetEmail", "El Email fue cambiado exitosamente ! ");
			else
				GeneralController.map.put("messageResetEmail", "* Error al guardar");
		}else
			GeneralController.map.put("messageResetEmail", "* Ese Email ya esta registrado");
		if(ad)
			res.redirect("/admin/resetEmail");
		else
			res.redirect("/loged/resetEmail");
		return null;
	};
	
	/**
	 * 
	 */
	public static TemplateViewRoute pResetEmail = (req, res) -> {
		GeneralController.map.remove("messageResetEmail");
		boolean ad = req.session().attribute(Consts.ATTRIBUTEADMIN);
		if (!GeneralController.checkQueryParams(req, "mail", "pass")) {
			GeneralController.map.put("messageResetEmail", "* Faltan datos");
			if(ad)
				res.redirect("/admin/resetEmail");
			else
				res.redirect("/loged/resetEmail");
			return null;
		}
		String username = req.session().attribute(Consts.ATTRIBUTEUSERNAME);
		User us = User.getUser(username);
		if (us.getString("password").equals(req.queryParams("pass"))) {
			return pSaveEmail.handle(req, res);
		}else
			GeneralController.map.put("messageResetEmail", "* Contraseña incorrecta");
		if(ad)
			res.redirect("/admin/resetEmail");
		else
			res.redirect("/loged/resetEmail");
		return null;
	};

	/**
	 * If the user was logged, it close his session and clear the map.
	 */
	public static Filter closeSession = (req, res) -> {
		for (String attribute : req.session().attributes())
			req.session().removeAttribute(attribute);
		if (GeneralController.map.get("resetPass") != null) {
			GeneralController.map.clear();
			GeneralController.map.put("messageResetPass", "---> Tu contraseña fue cambiada exitosamente! <--- Inicie Sesion!");
		}else
			GeneralController.map.clear();
		res.redirect("/");
	};
}
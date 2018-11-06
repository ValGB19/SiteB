package prode.Controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.*;
import prode.*;

public class UserController{

    static Map<String, Object> map = new HashMap<String, Object>();

	public static HashMap<String, Object> register (Request req, Response res) {
        map.remove("errorLogin");
        map.remove("errorRegister");
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

    private static ArrayList<String> errorsRegister (Map<String, String> data){
		ArrayList<String> tmp = new ArrayList<String>();
    	tmp.add("Datos incorrectos");
    	addError((!data.get("pwd").equals(data.get("pwd2"))),"*Las contrase?s no coinciden", tmp);
    	addError((User.findFirst("nick = ?", data.get("nick")) != null),"*El nickname ya esta en uso", tmp);
    	addError((User.findFirst("dni = ?", Integer.parseInt(data.get("dni"))) != null),"*Ese dni ya esta registrado", tmp);
    	addError((User.findFirst("email = ?", data.get("email")) != null),"*Ese email ya esta registrado", tmp);
    	addError((!"traemelapromocionmessi".equals(data.get("key")) && (data.get("key"))!=null),"*Palabla magica incorrecta", tmp);
		return tmp;
    }
    
    private static void addError(Boolean condition, String message, ArrayList<String> list) {
    	if(condition)
    		list.add(message);
    }
    
    public static TemplateViewRoute login=(req, res) -> {
        map.remove("errorLogin");
        map.remove("errorRegister");
        String usernameL = req.queryParams("usernamelogin");
        String pswL = req.queryParams("pswLogin");
        System.out.println(usernameL + " " + pswL);
        boolean log = false;
        String mes="";
        if(User.log(usernameL,pswL)){
            req.session(true);
            req.session().attribute("username", usernameL);
            req.session().attribute("logueado", true);
            if(User.findFirst("nick = ?", usernameL).getBoolean("admin")) {
                req.session().attribute("admin", true);
                map.put("admin", true);
            }
            log = true;
            String name = ((User) User.findFirst("nick = ?",usernameL)).getNameUser();
            String surname = ((User) User.findFirst("nick = ?",usernameL)).getSurnameUser();
            map.put("nick", usernameL);
            map.put("name", name);
            map.put("surname", surname);
            System.out.println("Loged "+ usernameL);
        }else{
            mes="Los datos ingresados son incorrectos";
        }
        if(log){
            res.redirect("/loged/profile");
            return null;
        }
        res.redirect("/");
        map.put("errorLogin", mes);
        return null;
    };
    
    public static TemplateViewRoute contain2Perfil=(req, res) -> {
        String n = req.session().attribute("username");
        User u = (User.findFirst("nick = ?",n));
        int fix = new UsersFixtures().totalFixturesUser(u.getInteger("id"));
        int pred = (u.getTotalMatchPrediction()).size();
        List<MatchPrediction> mpu = u.getMatchPrediction();
        ArrayList<List<Object>> p = new ArrayList<List<Object>>(); 
        List<Object> l;
        for (MatchPrediction a: mpu) { //change this. Make a function in fixture wich returns the total poinst of the user in a league
        	l = new ArrayList<Object>();
        	l.add(a.getLeague());
        	l.add(a.getSchedule());
        	l.add(a.getScore());
            p.add(l);
        }
        map.put("totalPredictions",pred);
        map.put("totalFixtures",fix);
        map.put("nick", n);
        map.put("predUser", GeneralController.getAcum(p));
        return new ModelAndView(map, "./src/main/resources/loged/perfil.mustache");
    };

    public static TemplateViewRoute redicProfile=(req, res) -> {
        if (req.session().attribute("logueado") != null) {
            res.redirect("/loged/profile");
            return null;
        }else{
            map.put("countries", Country.getAllCountrys());           
        }
        return new ModelAndView(map, "./src/main/resources/inicio.mustache");
    };

    public static Filter closeSession = (req, res) -> {
        if (req.session().attribute("logueado") != null) {
            req.session().removeAttribute("logueado");
            if(req.session().attribute("admin") != null) {
                req.session().removeAttribute("admin");
                map.remove("admin");
            }
        }
        map.remove("lastFixture");
        map.remove("schedule");
        map.remove("errorLogin");
        map.remove("errorRegister");
        res.redirect("/");
    };
    
    public static TemplateViewRoute home=(req, res) -> {
    	if(req.queryParams("action").equals("signin"))
    		return login.handle(req, res);
        map.putAll(register(req,res));
        res.redirect("/");
        return null;
    };
    
}
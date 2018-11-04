package prode.Controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.*;
import prode.*;

public class UserController{

    static Map<String, Object> map = new HashMap<String, Object>();

    public static ArrayList<String> errorsRegister (String pwd, String pwd2, String nick, String dni, String email, String key){
		ArrayList<String> tmp = new ArrayList<String>();
    	tmp.add("Datos incorrectos");
		if (!pwd.equals(pwd2)) {
			tmp.add("*Las contrase?s no coinciden");
		}
		if (User.findFirst("nick = ?",nick) != null) {
			tmp.add("*El nickname ya esta en uso");
		}
		if (User.findFirst("dni = ?",Integer.parseInt(dni)) != null) {
			tmp.add("*Ese dni ya esta registrado");
		}
		if (User.findFirst("email = ?", email) != null) {
			tmp.add("*Ese email ya esta registrado");
		}
		if (!"traemelapromocionmessi".equals(key) && key!=null) {
			tmp.add("*Palabla magica incorrecta");
		}
		return tmp;
    }
	
	public static HashMap<String, Object> register (Request req, Response res) {
        map.remove("errorLogin");
        map.remove("errorRegister");
    	String nick = req.queryParams("rUsername");
		String pwd = req.queryParams("pswRegister");
		String pwd2 = req.queryParams("pswValida"); 	
		String name = req.queryParams("nombre");
		String surname = req.queryParams("apellido");
		String email = req.queryParams("mail");
		String country = req.queryParams("rpais");
		String dni = req.queryParams("rdni");
		String key = req.queryParams("clave");
		boolean isSaved = false;
    	if (pwd.equals(pwd2) && dni.length() <= 8) {
    		User temp = new User();
    		isSaved = temp.setUserTemp(name, surname, nick, email, pwd, Integer.parseInt(dni), country, "traemelapromocionmessi".equals(key), key);
		}
		HashMap<String, Object> mape = new HashMap<String, Object>();
    	if (!isSaved) {
    		ArrayList<String> tmp = errorsRegister(pwd, pwd2, nick, dni, email, key);
			mape.put("errorRegister", tmp);
		}
    	return mape;
    };

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
            map.put("nic", usernameL);
            map.put("name", name);
            map.put("surname", surname);
            
            System.out.println("Loged "+ usernameL);
        }else{
            mes="Los datos ingresados son incorrectos";
        }
        if(log){
            res.redirect("/loged/perfil");
            return null;
        }
        res.redirect("/");
        map.put("errorLogin", mes);
        return null;
    };
    
    public static TemplateViewRoute contain2Perfil=(req, res) -> {
        String n = req.session().attribute("username");
        User u = (User.findFirst("nick = ?",n));
        int fix = new UsersFixtures().cantFixUser(u.getInteger("id"));
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
        map.put("cantPred",pred);
        map.put("cantFix",fix);
        map.put("nic", n);
        map.put("predUser", GeneralController.getAcum(p));
        return new ModelAndView(map, "./src/main/resources/loged/perfil.mustache");
    };

    public static TemplateViewRoute redicPerfil=(req, res) -> {
        if (req.session().attribute("logueado") != null) {
            res.redirect("/loged/perfil");
            return null;
        }else{
            map.put("paisl", Country.getAllCountrys());           
        }
        return new ModelAndView(map, "./src/main/resources/inicio.mustache");
    };

    public static Filter closeSesion = (req, res) -> {
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
    
    public static TemplateViewRoute redicInicSesion=(req, res) -> {
        map.putAll(register(req,res));
        res.redirect("/");
        return null;
    };
}
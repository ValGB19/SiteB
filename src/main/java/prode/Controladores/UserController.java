package prode.Controladores;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import spark.*;
import prode.*;

public class UserController{

    static Map map = new HashMap();

	public static HashMap register (Request req, Response res) {
    	String nick = req.queryParams("rUsername");
		String pwd = req.queryParams("pswRegister");
		String pwd2 = req.queryParams("pswValida"); 	
		String nombre = req.queryParams("nombre");
		String apellido = req.queryParams("apellido");
		String mail = req.queryParams("mail");
		String pais = req.queryParams("rpais");
		String dni = req.queryParams("rdni");
		String pm = req.queryParams("clave");
		boolean e = false;
    	if (pwd.equals(pwd2) && dni.length() <= 8) {
    		User temp = new User();
    		temp.set("name", nombre);
    		temp.set("surname", apellido);
    		temp.set("nick", nick);
    		temp.set("email", mail);
    		temp.set("password", pwd);
    		temp.set("dni", Integer.parseInt(dni));
    		temp.set("country_id", Country.findFirst("name = ?", pais).get("id"));
 			temp.set("admin", "traemelapromocionmessi".equals(pm));
 			if ("traemelapromocionmessi".equals(pm) || pm == null || pm.isEmpty()) {
 				e = temp.save();
 			}	
		}
		HashMap mape = new HashMap();
    	if (!e) {
    		ArrayList tmp = new ArrayList();
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
    		if (User.findFirst("email = ?", mail) != null) {
    			tmp.add("*Ese email ya esta registrado");
			}
			if (!"traemelapromocionmessi".equals(pm) && pm!=null) {
    			tmp.add("*Palabla magica incorrecta");
			}
			mape.put("errorr", tmp);
			mape.put("paisl", Country.getAllCountrys());
			System.out.println("No se registro");
		}
    	return mape;
    };

    public static TemplateViewRoute login=(req, res) -> {
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
        map.put("errrr", mes);
        return null;
    };
    
    public static TemplateViewRoute contain2Perfil=(req, res) -> {
        String m = req.session().attribute("username");
        User u = (User.findFirst("nick = ?",m));
        int fix = new UsersFixtures().cantFixUser(u.getInteger("id"));
        int pred = (u.getTotalMatchPrediction()).size();
        List<MatchPrediction> mpu = u.getMatchPrediction();
        ArrayList<Object[]> p = new ArrayList<Object[]>(); 
        for (MatchPrediction a: mpu) {
            p.add(a.getPartePerfil());
        }
        map.put("cantPred",pred);
        map.put("cantFix",fix);
        map.put("predUser", GeneralController.filtroFuerte(p));
        return new ModelAndView(map, "./src/main/resources/loged/perfil.mustache");
    };

    public static TemplateViewRoute redicPerfil=(req, res) -> {
        if (req.session().attribute("logueado") != null) {
            res.redirect("/loged/perfil");
            return null;
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
        res.redirect("/");
    };
    
    public static TemplateViewRoute redicInicSesion=(req, res) -> {
        map.putAll(register(req,res));
        res.redirect("/");
        return null;
    };
}
package prode;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spark.*;
import static spark.Spark.*;

import org.javalite.activejdbc.Base;


public class Ensalada{
		
    @SuppressWarnings("rawtypes")
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
    
	static Map map = new HashMap();
    
    
    
    public static Filter redicInic = (req,res) -> {
    	if (req.session().attribute("logueado") == null) {
    		res.redirect("/");
    	}
   	};
   	
   
   	
    public static  Filter getCountrys = (req,res) -> {
    	if (!Base.hasConnection()) {
    		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode?nullNamePatternMatchesAll=true&useSSL=false", "root", "root");
    	    map.put("paisl", Country.getAllCountrys());
		}
   	};
   	
   	public static Filter conecBase=(req,res) -> {
    	if (Base.hasConnection()) {
    		Base.close();
		}
    };

    public static Filter closeSesion = (req, res) -> {
    	if (req.session().attribute("logueado") != null) {
    		req.session().removeAttribute("logueado");
    		res.redirect("/");
    	}
    	res.redirect("/");
    };
    
    public static TemplateViewRoute redicInicSesion=(req, res) -> {
    	map.putAll(register(req,res));
    	res.redirect("/");
    	return null;
    };

    public static TemplateViewRoute redicPerfil=(req, res) -> {
    	if (req.session().attribute("logueado") != null) {
    		res.redirect("/loged/perfil");
    		return null;
		}
        return new ModelAndView(map, "./src/main/resources/inicio.mustache");
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
    	//map.clear();
		map.put("errrr", mes);
    	return null;
    };
    
    public static TemplateViewRoute contain2Perfil=(req, res) -> {
    	String m = req.session().attribute("username");
    	User u = (User.findFirst("nick = ?",m));
    	List<MatchPrediction> mpu = u.getMatchPrediction();
    	ArrayList<Object[]> p = new ArrayList<Object[]>(); 
    	for (MatchPrediction a: mpu) {
    		p.add(a.getPartePerfil());
    	}
    	map.put("predUser", filtroFuerte(p));
        return new ModelAndView(map, "./src/main/resources/loged/perfil.mustache");
    };
    
    public static TemplateViewRoute vistaProdeFecha = (req,res) ->{
    	//String league = req.queryParams(queryParam);
    	List<String> f = Fixture.getAllFixtures();
    	String r = null;
    	int i=0;
    	while(r == null && i<f.size()) {
    		r=req.queryParams(f.get(i));
    		i++;
    	}
        if (r == null) {
            res.redirect("/loged/prode");
            return null;    
        }
        r = f.get(i-1);
        
    	List<Match> l = new Fixture().getFix(r).getMatch();
    	int fecha = l.get(0).getInteger("schedule");
    	l.removeIf((x)->x.getInteger("schedule") != fecha);
    	
    	ArrayList<Object[]> p = new ArrayList<Object[]>();
    	for (Match a: l) {
    		p.add(a.paraPredic());
    	}
    	map.put("jugarFix", p);
    	
    	res.redirect("/loged/prode");
    	return null;
    };
    
    public static TemplateViewRoute mainFixtures=(req, res) -> {
    	map.put("fixs", Fixture.getAllFixtures());
        return new ModelAndView(map, "./src/main/resources/loged/prode.mustache");
    };
    
    public static ArrayList<Object[]> filtroFuerte(ArrayList<Object[]> x){
    	HashMap<String, HashMap> f = new HashMap();
    	HashMap<Integer, Integer> p = new HashMap();
    	for (Object[] objects : x) {
    		if (f.containsKey(objects[0])) {
    			p = f.get(objects[0]);
				if (p.containsKey(objects[1])) {
					p.replace((Integer) objects[1], p.get(objects[1])+ (Integer) objects[2]);
				}else{
					p.put((Integer) objects[1], (Integer) objects[2]);
				}
			}else{
				p = new HashMap();
				f.put((String)objects[0], p);
				p.put((Integer) objects[1], (Integer) objects[2]);
			}
		}
    	ArrayList<Object[]> res = new ArrayList<Object[]>();
    	for (String c :f.keySet()) {
			for(Object m : f.get(c).keySet()){
				res.add(new Object[]{c,m,f.get(c).get(m)});
			}
		}
    	return res;
    }
}
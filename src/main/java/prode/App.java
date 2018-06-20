package prode;

import org.javalite.activejdbc.Base;
import prode.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import spark.*;
import static spark.Spark.*;
import spark.template.mustache.MustacheTemplateEngine;
import spark.template.*;

public class App{
    
    public static void main( String[] args ){
	   	staticFiles.location("/public");
    	
        before("*", (req,res) -> {
	    	if (!Base.hasConnection()) {
	    		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode?nullNamePatternMatchesAll=true&useSSL=false", "root", "root");
			}
	   	});
        
        after("*", (req,res) -> {
	    	if (Base.hasConnection()) {
	    		Base.close();
			}
	    });
        
        Map map = new HashMap();
	    map.put("paisl", Country.getAllCountrys());
        get("/", (req, res) -> {
	        return new ModelAndView(map, "./src/main/resources/inicio.mustache");
	    		}, new MustacheTemplateEngine()
	    );

	    get("/perfil", (req, res) -> {
	        return new ModelAndView(map, "./src/main/resources/perfil.mustache");
	    		}, new MustacheTemplateEngine()
	    );
	    
	    post("/", (req, res) -> {
	    	//inicio sesion
	    	boolean log = false;
	    	/*String mes="";
	    	String usernameL = req.queryParams("usernamelogin");
	    	String pswL = req.queryParams("pswLogin");
	    	if(usernameL!=null && pswL!=null){
	    		if(User.log(usernameL,pswL)!=null){
	    			req.session().attribute("username", usernameL);
		    		log = true;
	    		}else{
	    			mes="Los datos ingresados son incorrectos";
	    		}
	    	}else{
	    		mes="Complete todos los campos";
	    	}
	    	if(log){
	    		res.redirect("/perfil");
		    }else{
		    	res.status(401);
		    	//Map<String,String> p = new HashMap();
		    	map.put("error", mes);
		    }*/
	    	String nick = req.queryParams("rUsername");
	    	String pwd = req.queryParams("pswRegister");
	    	String pwd2 = req.queryParams("pswValida"); 	
	    	String nombre = req.queryParams("nombre");
	    	String apellido = req.queryParams("apellido");
	    	String mail = req.queryParams("mail");
	    	String pais = req.queryParams("rpais");
	    	String dni = req.queryParams("rdni");
	    	
	    	if (pwd2 == null && dni == null && apellido == null) {
	    		return new ModelAndView(map, "./src/main/resources/perfil.mustache");
	    	}
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
	    		e = temp.save();
			}
	    	if (!e) {
	    		ArrayList tmp = new ArrayList();
	    		tmp.add("Datos incorrectos");
	    		if (pwd != pwd2) {
	    			tmp.add("*Las contraseñas no coinciden");
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
				map.put("errorr", tmp);
				System.out.println("No se registro");
			}
	    	
	    	return new ModelAndView(map, "./src/main/resources/inicio.mustache");
        	}, new MustacheTemplateEngine()
	    );

	    get("/perfil", (req, res) -> {
	        return new ModelAndView(map, "./src/main/resources/perfil.mustache");
	    		}, new MustacheTemplateEngine()
	    );
	    get("/prode", (req, res) -> {
	        return new ModelAndView(map, "./src/main/resources/prode.mustache");
	    		}, new MustacheTemplateEngine()
	    );
	    
	  //Control de  Exceptions
/*
    	exception(Exception.class, (exception, request, response) -> {

    		response.body( exception.getMessage());

		});*/
    }
}

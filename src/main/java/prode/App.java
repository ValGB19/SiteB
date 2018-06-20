package prode;

import org.javalite.activejdbc.Base;
import prode.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spark.*;
import static spark.Spark.*;
import spark.template.mustache.MustacheTemplateEngine;
import spark.template.*;

public class App{
    
    public static void main( String[] args ){
	   	staticFiles.location("/public");
    	
	   	before("/loged/*", (req,res) -> {
	    	if (!Base.hasConnection()) {
	    		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode?nullNamePatternMatchesAll=true&useSSL=false", "root", "root");
			}
	   	});
	   	
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
        List paises = Country.getAllCountrys();
	    map.put("paisl", paises);
        get("/", (req, res) -> {
	        return new ModelAndView(map, "./src/main/resources/inicio.mustache");
	    		}, new MustacheTemplateEngine()
	    );

	    get("/perfil", (req, res) -> {
	        return new ModelAndView(map, "./src/main/resources/loged/perfil.mustache");
	    		}, new MustacheTemplateEngine()
	    );
	    
	    post("/", (req, res) -> {
	    	//inicio sesion
	    	
	    	String nick = req.queryParams("rUsername");
	    	String pwd = req.queryParams("pswRegister");
	    	String pwd2 = req.queryParams("pswValida"); 	
	    	String nombre = req.queryParams("nombre");
	    	String apellido = req.queryParams("apellido");
	    	String mail = req.queryParams("mail");
	    	String pais = req.queryParams("rpais");
	    	String dni = req.queryParams("rdni");
	    	String pm = req.queryParams("clave");
	    	
	    	if (pwd2 == null && dni == null && apellido == null) {
	    		boolean log = false;
		    	String mes="";
		    	String usernameL = req.queryParams("usernamelogin");
		    	String pswL = req.queryParams("pswLogin");
		    	System.out.println(usernameL);
		    	if(usernameL!=null && pswL!=null){
		    		if(User.log(usernameL,pswL)){
		    			req.session().attribute("username", usernameL);
		    			req.attribute("logueado", true);
			    		log = true;
		    		}else{
		    			mes="Los datos ingresados son incorrectos";
		    		}
		    	}else{
		    		mes="Complete todos los campos";
		    	}
		    	HashMap mape = new HashMap();
		    	mape.put("paisl", paises);
		    	mape.put("errrr", mes);
		    	if(log){
		    		res.redirect("/perfil");
		    		return new ModelAndView(map, "./src/main/resources/loged/perfil.mustache");
			    }
	    		
		    	return new ModelAndView(mape, "./src/main/resources/inicio.mustache");
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
	    		if ("nose".equals(pm)) 
	    			temp.set("admin", true);
	    		
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
	        return new ModelAndView(map, "./src/main/resources/loged/perfil.mustache");
	    		}, new MustacheTemplateEngine()
	    );
	    get("/loged/prode", (req, res) -> {
	        return new ModelAndView(map, "./src/main/resources/loged/prode.mustache");
	    		}, new MustacheTemplateEngine()
	    );
	    
	  //Control de  Exceptions
/*
    	exception(Exception.class, (exception, request, response) -> {

    		response.body( exception.getMessage());

		});*/
    }
}

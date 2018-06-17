package prode;

import org.javalite.activejdbc.Base;
import prode.User;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;

import spark.*;
import static spark.Spark.*;
import spark.template.mustache.MustacheTemplateEngine;
import spark.template.*;

public class App{
    
	public static Filter  afre = (req,res) -> {
	    	if (Base.hasConnection()) {
	    		Base.close();
			}
	    };

	public static Filter  bef = (req,res) -> {
	    	if (!Base.hasConnection()) {
	    		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode?nullNamePatternMatchesAll=true", "root", "root");
			}
	    };


    public static void main( String[] args ){
	   	staticFiles.location("/public");
    	
        before("*", bef);
        after("*", afre);
        
        Map map = new HashMap();
	    map.put("paisl", Country.getAllCountrys());
        get("/", (req, res) -> {
	        return new ModelAndView(map, "./src/main/resources/inicio.mustache");
	    		}, new MustacheTemplateEngine()
	    );
	    
	    post("/", (req, res) -> {
	    	String nombre = req.queryParams("nombre");
	    	String apellido = req.queryParams("apellido");
	    	String nick = req.queryParams("rUsername");
	    	String pwd = req.queryParams("pswRegister");
	    	String pwd2 = req.queryParams("pswValida");
	    	String mail = req.queryParams("mail");
	    	String pais = req.queryParams("rpais");
	    	String dni = req.queryParams("rdni");
	    	if (pwd.equals(pwd2)) {
	    		User temp = new User();
	    		temp.set("name", nombre);
	    		temp.set("surname", apellido);
	    		temp.set("nick", nick);
	    		temp.set("email", mail);
	    		temp.set("password", pwd);
	    		temp.set("dni", Integer.parseInt(dni));
	    		temp.set("country_id", Country.findFirst("name = ?", pais).get("id"));
	    		temp.save();
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

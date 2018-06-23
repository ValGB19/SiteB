package prode;

import org.javalite.activejdbc.Base;
import prode.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import spark.*;
import static spark.Spark.*;
import spark.template.mustache.MustacheTemplateEngine;
import spark.template.*;

public class App{
    
    public static void main( String[] args ){

	   	staticFiles.location("/public");
		notFound((req, res) -> {
			if (req.session().attribute("logueado") == null) {
	    		res.redirect("/");
	    	}
       		res.redirect("/loged/perfil");
    		return null;
		});

        Map map = new HashMap();
        
	   	before("/loged/*", Ensalada.redicInic);
	   	
        before("*", Ensalada.getCountrys);
        
        after("*", Ensalada.conecBase);
        
        get("/", Ensalada.redicPerfil, new MustacheTemplateEngine());
        
        post("/", Ensalada.login, new MustacheTemplateEngine());

        post("/r", (req, res) -> {
        	map.clear();
	    	map.putAll(Ensalada.register(req,res));
	    	res.redirect("/");
	    	return null;
	    });

	    get("/loged/perfil", Ensalada.contain2Perfil, new MustacheTemplateEngine());
	    
	    get("/loged/prode", Ensalada.mainFixtures, new MustacheTemplateEngine());
	    
	    after("/exit", Ensalada.closeSesion);
    }    
    
}
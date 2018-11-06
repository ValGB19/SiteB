package prode;

import static spark.Spark.*;
import spark.template.mustache.MustacheTemplateEngine;
import prode.Controladores.*;

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

	   	before("/loged/*", GeneralController.redicInic);
	   	
        before("*", GeneralController.getCountrys);
        
        after("*", GeneralController.conecBase);

        after("/exit", UserController.closeSesion);
        
        get("/", UserController.redicPerfil, new MustacheTemplateEngine());
        
        post("/", UserController.home, new MustacheTemplateEngine());

        post("/loged/prode", GeneralController.redicProde,new MustacheTemplateEngine());
        
        post("/loged/admin", GeneralController.redicAdmin,new MustacheTemplateEngine());

	    get("/loged/perfil", UserController.contain2Perfil, new MustacheTemplateEngine());

	    get("/loged/prode", FixtureController.mainFixtures, new MustacheTemplateEngine());
	    
	    get("/loged/results", PredictionController.verResults, new MustacheTemplateEngine());

	    get("/loged/admin", FixtureController.mainFixtu, new MustacheTemplateEngine());
    }    
    
}
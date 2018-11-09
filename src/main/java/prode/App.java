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
       		res.redirect("/loged/profile");
    		return null;
		});

	   	before("/loged/*", GeneralController.checkIfLoged);
	   	
        before("*", GeneralController.openConectionToDataBase);
        
        after("*", GeneralController.disconectDataBase);

        after("/exit", UserController.closeSession);
        
        get("/", UserController.redicProfile, new MustacheTemplateEngine());
        
        post("/", UserController.home, new MustacheTemplateEngine());

        post("/loged/prode", GeneralController.redicProde,new MustacheTemplateEngine());
        
        post("/loged/admin", GeneralController.redicAdmin,new MustacheTemplateEngine());

	    get("/loged/profile", UserController.contain2Perfil, new MustacheTemplateEngine());

	    get("/loged/prode", FixtureController.mainFixturesPlayer, new MustacheTemplateEngine());
	    
	    get("/loged/results", PredictionController.verResults, new MustacheTemplateEngine());

	    get("/loged/admin", FixtureController.mainFixturesAdmin, new MustacheTemplateEngine());
    }    
    
}
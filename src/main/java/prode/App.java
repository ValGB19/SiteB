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

        post("/r", Ensalada.redicInicSesion,new MustacheTemplateEngine());
        
        post("/x", Ensalada.vistaProdeFecha,new MustacheTemplateEngine());

        post("/y", Ensalada.vistaProdeFecha2,new MustacheTemplateEngine());

        post("/j", Ensalada.cargarPrediction,new MustacheTemplateEngine());
	    
	    post("/a", Ensalada.cargaResulMatch,new MustacheTemplateEngine());

	    get("/loged/perfil", Ensalada.contain2Perfil, new MustacheTemplateEngine());

	    get("/loged/carga", Ensalada.carga, new MustacheTemplateEngine());
	    
	    get("/loged/prode", Ensalada.mainFixtures, new MustacheTemplateEngine());
	    
	    get("/loged/results", Ensalada.verResults, new MustacheTemplateEngine());

	    get("/loged/admin", Ensalada.mainFixtu, new MustacheTemplateEngine());
	    
	    after("/exit", Ensalada.closeSesion);
    }    
    
}
package prode;

import org.javalite.activejdbc.Base;
import prode.User;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.mustache.MustacheTemplateEngine;
import spark.template.*;

/**
 * Hello world!
 *
 */
public class App{
    
    public static void main( String[] args ){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode?nullNamePatternMatchesAll=true", "root", "root");

       staticFiles.location("views/public");
       Map map = new HashMap();
	    map.put("name", "Sam");
	    map.put("value", 1000);
	    map.put("taxed_value", 1000 - (1000 * 0.4));
	    map.put("in_ca", true);

	    get("/inicio", (req, res) -> {
	        return new ModelAndView(map, "./views/inicio.mustache");
	    		}, new MustacheTemplateEngine()
	    );
       Base.close();
    }
}

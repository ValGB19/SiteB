package prode.Controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.javalite.activejdbc.Base;
import spark.*;

public class GeneralController{
		
    @SuppressWarnings("rawtypes")
	
    
	static Map map = new HashMap();

	public static Filter redicInic = (req,res) -> {
        if (req.session().attribute("logueado") == null) {
            res.redirect("/");
        }
    };   
   	
    public static  Filter getCountrys = (req,res) -> {
    	if (!Base.hasConnection()) {
    		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode?nullNamePatternMatchesAll=true&useSSL=false", "root", "root");
		}
   	};
   	
   	public static Filter conecBase=(req,res) -> {
    	if (Base.hasConnection()) {
    		Base.close();
		}
    };
}
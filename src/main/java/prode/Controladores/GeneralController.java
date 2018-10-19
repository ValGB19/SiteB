package prode.Controladores;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import spark.*;
import prode.*;

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
    	    map.put("paisl", Country.getAllCountrys());
		}
   	};
   	
   	public static Filter conecBase=(req,res) -> {
    	if (Base.hasConnection()) {
    		Base.close();
		}
    };
   
    public static TemplateViewRoute verResults=(req, res) -> {
    	List<User> lisu = new UsersFixtures().getAllPlayers();
    	System.out.println(lisu.size());
    	ArrayList allUs = new ArrayList();
    	for(User u : lisu) {
    		List<MatchPrediction> mpu = u.getMatchPrediction();
    		ArrayList<Object[]> p = new ArrayList<Object[]>(); 
        	for (MatchPrediction a: mpu) {
       			p.add(a.getPartePerfil());
        	}
        	allUs.add(filtroFuerte2(p,u.getString("nick")));
    	}
    	map.put("players", allUs);
    	map.put("fixs", Fixture.getAllFixtures());
        return new ModelAndView(map, "./src/main/resources/loged/results.mustache");
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
    
    public static ArrayList<Object[]> filtroFuerte2(ArrayList<Object[]> x,String username){
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
				res.add(new Object[]{username,c,m,f.get(c).get(m)});
			}
		}
    	return res;
    }
    
}
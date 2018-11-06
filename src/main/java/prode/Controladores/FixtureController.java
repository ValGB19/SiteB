package prode.Controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.*;
import prode.*;

public class FixtureController{
	static Map<String, Object> map = new HashMap<String, Object>();

	 public static TemplateViewRoute mainFixtu=(req, res) -> {
		map.put("nic",req.session().attribute("username"));
    	map.put("fixs", Fixture.getAllFixtures());
        return new ModelAndView(map, "./src/main/resources/loged/admin.mustache");
    };

    public static TemplateViewRoute mainFixtures=(req, res) -> {
    	map.put("nic",req.session().attribute("username"));
    	map.put("fixs", Fixture.getAllFixtures());
        return new ModelAndView(map, "./src/main/resources/loged/prode.mustache");
    };

  private static String getFstFixture(Request req){
    	List<String> f = Fixture.getAllFixtures();
    	int i = 0;
    	String r = null;
    	while(r == null && i<f.size()) {
    		r=req.queryParams(f.get(i));
    		i++;
    	}
    	if (r == null) {
    		return null;    
    	}
    	return f.get(i-1);
    }
    
    public static TemplateViewRoute vistaProdeFecha2 = (req,res) ->{
      map.put("nic",req.session().attribute("username"));
      List<String> f = Fixture.getAllFixtures();
    	String r = getFstFixture(req);
    	if (r == null) {
    		res.redirect("/loged/prode");
    		return null;    
    	}
    	req.session().attribute("lastFixture",r);
    	
    	List<Match> l = new Fixture().getFix(r).getMatch();
    	l.removeIf((x)-> x.getString("result") != null);
    	getFromMatchToShow(l, map, r);
    	res.redirect("/loged/admin");
    	return null;
    };

    public static TemplateViewRoute vistaProdeFecha = (req,res) ->{
    	map.put("nic",req.session().attribute("username"));
    	List<String> f = Fixture.getAllFixtures();
    	String r = getFstFixture(req);
        if (r == null) {
            res.redirect("/loged/prode");
            return null;    
        }
        req.session().attribute("lastFixture",r);
        int idU = new User().getUser(req.session().attribute("username")).getInteger("id");
    	List<Match> l = new Fixture().getFix(r).getMatch();
    	l.removeIf(Match.filterById(idU));
    	getFromMatchToShow(l, map, r);
    	res.redirect("/loged/prode");
    	return null;
    };
    
    private static void getFromMatchToShow(List<Match> l, Map<String, Object> map, String r){
    	ArrayList<Map<String,Object>> p = new ArrayList<Map<String,Object>>();
    	if(l.size()!=0) {
	    	int fecha = l.get(0).getInteger("schedule");
	    	l.removeIf((x)->x.getInteger("schedule") != fecha);
	    	map.put("fechaVig",fecha);
	    	map.put("lastFixture",r);
	    	for (Match a: l) {
	    		p.add(a.paraPredic());
	    	}
    	}
    	map.put("jugarFix", p);
    }
}
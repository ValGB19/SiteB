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
    	map.put("fixs", Fixture.getAllFixtures());
        return new ModelAndView(map, "./src/main/resources/loged/admin.mustache");
    };

    public static TemplateViewRoute mainFixtures=(req, res) -> {
    	map.put("fixs", Fixture.getAllFixtures());
        return new ModelAndView(map, "./src/main/resources/loged/prode.mustache");
    };

    
    public static TemplateViewRoute vistaProdeFecha2 = (req,res) ->{
    	List<String> f = Fixture.getAllFixtures();
    	String r = null;
    	int i=0;
    	while(r == null && i<f.size()) {
    		r=req.queryParams(f.get(i));
    		i++;
    	}
    	if (r == null) {
    		res.redirect("/loged/prode");
    		return null;    
    	}
    	r = f.get(i-1);
    	req.session().attribute("lastFixture",r);
    	
    	List<Match> l = new Fixture().getFix(r).getMatch();
    	l.removeIf((x)-> x.getString("result") != null);
    	ArrayList<HashMap<String,Object>> p = new ArrayList<HashMap<String,Object>>();
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
    	res.redirect("/loged/admin");
    	return null;
    };

    public static TemplateViewRoute vistaProdeFecha = (req,res) ->{
    	List<String> f = Fixture.getAllFixtures();
    	String r = null;
    	int i=0;
    	while(r == null && i<f.size()) {
    		r=req.queryParams(f.get(i));
    		i++;
    	}
        if (r == null) {
            res.redirect("/loged/prode");
            return null;    
        }
        r = f.get(i-1);
        req.session().attribute("lastFixture",r);
        int idU = new User().getUser(req.session().attribute("username")).getInteger("id");
    	List<Match> l = new Fixture().getFix(r).getMatch();
    	l.removeIf((x)-> x.getString("result") != null || "null".equals(x.getString("result")) || new MatchPrediction().comprobaJuego(idU, x.getInteger("id")));
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
    	res.redirect("/loged/prode");
    	return null;
    };
}
package prode.Controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.*;
import prode.*;

public class FixtureController{

	static Map<String, Object> map = new HashMap<String, Object>();

    /**
     * Returns a view for the administrator to upload results.
     *
     * @param  req  the request which contains the username.
     * @param  res  the response.
     * @return      an ModelAndView to show.
    */
	 public static TemplateViewRoute mainFixturesAdmin=(req, res) -> {
		map.put("nick",req.session().attribute("username"));
    	map.put("fixs", Fixture.getAllFixtures());
        return new ModelAndView(map, "./src/main/resources/loged/admin.mustache");
    };

    /**
     * Returns a view for the user to bet in the prode.
     *
     * @param  req  the request which contains the usename.
     * @param  res  the response.
     * @return      an ModelAndView to show.
    */
    public static TemplateViewRoute mainFixturesPlayer=(req, res) -> {
    	map.put("nick",req.session().attribute("username"));
    	map.put("fixs", Fixture.getAllFixtures());
        return new ModelAndView(map, "./src/main/resources/loged/prode.mustache");
    };

    /**
     * Returns the first Fixture name.
     *
     * @param  req  the request which contains the id from the fixture.
     * @return      an ModelAndView to show.
    */
    private static String getFstFixture(Request req) {
    	List<String> listFixtures = Fixture.getAllFixtures();
    	int index = 0;
    	String fix = null;
    	while(fix == null && index<listFixtures.size()) {
    		fix = req.queryParams(listFixtures.get(index));
    		index++;
    	}
    	if (fix == null) {
    		return null;    
    	}
    	return listFixtures.get(index - 1);
    }
    
    /**
     * Set in the session the attribute <code>"lastFixture"</code>
     * , load the map with the match and redirect to "/loged/admin".
     *
     * @param  req  the request which contains the id from the fixture.
     * @param  res  the response.
     * @return      nothing.
    */
    public static TemplateViewRoute viewProdeScheduleAdmin = (req,res) -> {
	    map.put("nick",req.session().attribute("username"));
		String fix = getFstFixture(req);
		res.redirect("/loged/admin");
        if (fix == null)
			return null;    
		req.session().attribute("lastFixture",fix);
		List<Match> listMatches = new Fixture().getFix(fix).getMatch();
		listMatches.removeIf((x)-> x.getString("result") != null);
		getFromMatchToShow(listMatches, map, fix);
		return null;
    };

    /**
     * Set in the session the attribute <code>"lastFixture"</code>
     * , load the map with the match and redirect to "/loged/prode".
     *
     * @param  req  the request which contains the id from the fixture.
     * @param  res  the response.
     * @return      nothing.
    */
    public static TemplateViewRoute viewProdeSchedulePlayer = (req,res) -> {
    	map.put("nick",req.session().attribute("username"));
    	String fix = getFstFixture(req);
        res.redirect("/loged/prode");
        if (fix == null)
            return null;    
        req.session().attribute("lastFixture",fix);
        int idUser = new User().getUser(req.session().attribute("username")).getInteger("id");
    	List<Match> listMatches = new Fixture().getFix(fix).getMatch();
    	listMatches.removeIf(Match.filterById(idUser));
    	getFromMatchToShow(listMatches, map, fix);
    	return null;
    };
    
    private static void getFromMatchToShow(List<Match> l, Map<String, Object> map, String r){
    	ArrayList<Map<String,Object>> p = new ArrayList<Map<String,Object>>();
    	if(l.size()!=0) {
	    	int schedule = l.get(0).getInteger("schedule");
	    	l.removeIf((x)->x.getInteger("schedule") != schedule);
	    	map.put("scheduleCurrent",schedule);
	    	map.put("lastFixture",r);
	    	for (Match a: l) {
	    		p.add(a.forPrediction());
	    	}
    	}
    	map.put("fixtureCurrent", p);
    }
}
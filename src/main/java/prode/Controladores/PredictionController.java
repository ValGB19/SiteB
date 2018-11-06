package prode.Controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.javalite.activejdbc.Model;
import spark.*;
import prode.*;

public class PredictionController{
	
	static Map<String,Object> map = new HashMap<String,Object>();
	
	 public static TemplateViewRoute cargarPrediction = (req,res) ->{
        String fix = req.session().attribute("lastFixture");
        String user = req.session().attribute("username");
        map.put("nic",user);
        int idU = new User().getUser(user).getInteger("id");
        List<Match> l = new Fixture().getFix(fix).getMatch();
        l.removeIf(Match.filterById(idU));
        int fecha = l.get(0).getInteger("schedule");
        l.removeIf((x)->x.getInteger("schedule") != fecha);
        for (Match a: l) {
            Integer idM=a.getInteger("id");
            MatchPrediction pred = new MatchPrediction();
            pred.setInteger("match_id", idM);
            pred.setString("user_id", idU);
            pred.setString("prediction", req.queryParams(idM.toString()));
            pred.save();
        }
        UsersFixtures uf = new UsersFixtures();
        if(UsersFixtures.findFirst("user_id = ? and fixture_id = ?",idU, new Fixture().getFix(fix).getInteger("id")) == null) {
        	 uf.set("user_id", idU);
             uf.set("fixture_id",new Fixture().getFix(fix).getInteger("id"));
             uf.save();
        }
        res.redirect("/loged/perfil");
        return null;
    };

    public static TemplateViewRoute cargaResulMatch = (req,res) ->{
        String fix = req.session().attribute("lastFixture");
        req.session().removeAttribute("lastFixture");
        req.session().removeAttribute("schedule");
        map.put("nic",req.session().attribute("username"));
        res.redirect("/loged/perfil");
        
        List<Match> l = new Fixture().getFix(fix).getMatch();
        l.removeIf((x)-> x.getString("result") != null || "null".equals(x.getString("result")));
        if (l.size() == 0)
            return null;

        int fecha = l.get(0).getInteger("schedule");
	    l.removeIf((x)-> x.getInteger("schedule") != fecha);
	    for (Match a: l) {
            Integer idM=a.getInteger("id");
            String s = req.queryParams(idM.toString());
            if(s != null) {
            	for(Model mp: MatchPrediction.find("match_id = ?", idM)) {
            		if(s.equals(mp.getString("prediction")))
            			mp.setInteger("score",3);            			
            		else
            			mp.setInteger("score",0);
            		mp.save();
            	}
            	a.setString("result", req.queryParams(idM.toString()));
            	a.save();
            }
        }
        return null;
    };

    public static TemplateViewRoute verResults=(req, res) -> {
    	List<User> listUsers = new UsersFixtures().getAllPlayers();
    	map.put("nic",req.session().attribute("username"));
    	ArrayList<List<List<Object>>> allUs = new ArrayList<List<List<Object>>>();
    	for(User u : listUsers) {
    		List<MatchPrediction> mpu = u.getMatchPrediction();
    		ArrayList<List<Object>> p = new ArrayList<List<Object>>(); 
            List<Object> l;
            for (MatchPrediction a: mpu) { //change this. Make a function in fixture wich returns the total poinst of the user in a league
            	l = new ArrayList<Object>();
            	l.add(u.getString("nick"));
            	l.add(a.getLeague());
            	l.add(a.getSchedule());
            	l.add(a.getScore());
                p.add(l);
            }
        	allUs.add(GeneralController.getAcum(p));
    	}
    	map.put("players", allUs);
    	map.put("fixs", Fixture.getAllFixtures());
        return new ModelAndView(map, "./src/main/resources/loged/results.mustache");
    }; 
}
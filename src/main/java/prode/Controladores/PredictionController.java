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

public class PredictionController{
	static Map map = new HashMap();

	 public static TemplateViewRoute cargarPrediction = (req,res) ->{
        String fix = req.session().attribute("lastFixture");
        String user = req.session().attribute("username");
        int idU = new User().getUser(user).getInteger("id");
        System.out.println("***************"+fix);
        List<Match> l = new Fixture().getFix(fix).getMatch();
        l.removeIf((x)->  x.getString("result") != null || "null".equals(x.getString("result")) || new MatchPrediction().comprobaJuego(idU, x.getInteger("id")));
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
        if(uf.findFirst("user_id = ? and fixture_id = ?",idU, new Fixture().getFix(fix).getInteger("id")) == null) {
        	System.out.println(new Fixture().getFix(fix).getInteger("id"));
        	 uf.set("user_id", idU);
             uf.set("fixture_id",new Fixture().getFix(fix).getInteger("id"));
             uf.save();
        }
       
        res.redirect("/loged/perfil");
        return null;
    };

    public static TemplateViewRoute cargaResulMatch = (req,res) ->{
        String fix = req.session().attribute("lastFixture");
        List<Match> l = new Fixture().getFix(fix).getMatch();
        l.removeIf((x)-> x.getString("result") != null || "null".equals(x.getString("result")));
        if(l.size()!=0) {
	        int fecha = l.get(0).getInteger("schedule");
	        l.removeIf((x)-> x.getInteger("schedule") != fecha);
	        for (Match a: l) {
	            Integer idM=a.getInteger("id");
	            System.out.println(a.getClass());
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
        }       
        req.session().removeAttribute("lastFixture");
        req.session().removeAttribute("schedule");
        res.redirect("/loged/perfil");
        return null;
    };
}
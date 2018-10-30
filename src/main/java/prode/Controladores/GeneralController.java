package prode.Controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    private static Integer foldrSum(List<List<Object>> l) {
		Integer r = 0;
		for (List<Object> n : l) {
			r = r + (Integer) n.get(0);
		}
		return r;
	}

	private static HashMap<Object, List<List<Object>>> groupByFstField(List<List<Object>> a) {
		HashMap<Object, List<List<Object>>> m = new HashMap<Object, List<List<Object>>>();
		List<List<Object>> l = new ArrayList<List<Object>>();
		for (List<Object> b : a) {
			Object aux = b.get(0);
			b.remove(0);
			if (m.containsKey(aux))
				m.get(aux).add(b);
			else {
				l = new ArrayList<List<Object>>();
				l.add(b);
				m.put(aux, l);
			}
		}
		return m;
	}
	
	private static List<List<Object>> ungrup(HashMap<Object, List<List<Object>>> m) {
		ArrayList<List<Object>> res = new ArrayList<List<Object>>();
		List<Object> l = new ArrayList<Object>();
		for (Object k : m.keySet()) {
			for (List<Object> j : getAcum(m.get(k))) {
				l = new ArrayList<Object>();
				l.add(k);
				l.addAll(j);
				res.add(l);
			}
		}
		return res;
	}

	// change name. Fill commentary
	public static List<List<Object>> getAcum(List<List<Object>> a) {
		if (a == null || a.size() == 0) return null;
		if (a.get(0).size() == 1) {
			List<Object> l = new ArrayList<Object>();
			List<List<Object>> r = new ArrayList<List<Object>>();
			l.add(foldrSum(a));
			r.add(l);
			return r;
		}
		return ungrup(groupByFstField(a));
	}
}
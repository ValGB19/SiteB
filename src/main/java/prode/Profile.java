package prode;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;

class Profile{
	public Array[][][] jugadasUser(){
		String query ="select u.id mp.score mp.match_id from users u, match_predictions mp where (u.id=mp.user_id) and (mp.score!=null)";
		List<Map> jug = Base.findAll(query);
		return null;
	}
}
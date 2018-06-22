package prode;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;

public class Ensalada{
	public Array[][][] jugadasUser(){
		String query ="select u.id mp.score mp.match_id from users u, match_predictions mp where (u.id=mp.user_id) and (mp.score!=null)";
		List<Map> jug = Base.findAll(query);
		return null;
	}
	
    @SuppressWarnings("rawtypes")
	public static HashMap gg (spark.Request req, spark.Response res) {
    	String nick = req.queryParams("rUsername");
		String pwd = req.queryParams("pswRegister");
		String pwd2 = req.queryParams("pswValida"); 	
		String nombre = req.queryParams("nombre");
		String apellido = req.queryParams("apellido");
		String mail = req.queryParams("mail");
		String pais = req.queryParams("rpais");
		String dni = req.queryParams("rdni");
		String pm = req.queryParams("clave");
		boolean e = false;
    	if (pwd.equals(pwd2) && dni.length() <= 8) {
    		User temp = new User();
    		temp.set("name", nombre);
    		temp.set("surname", apellido);
    		temp.set("nick", nick);
    		temp.set("email", mail);
    		temp.set("password", pwd);
    		temp.set("dni", Integer.parseInt(dni));
    		temp.set("country_id", Country.findFirst("name = ?", pais).get("id"));
 			temp.set("admin", "traemelapromocionmessi".equals(pm));
 			if ("traemelapromocionmessi".equals(pm) || pm == null || pm.isEmpty()) {
 				e = temp.save();
 			}	
		}
		HashMap mape = new HashMap();
    	if (!e) {
    		ArrayList tmp = new ArrayList();
    		tmp.add("Datos incorrectos");
    		if (!pwd.equals(pwd2)) {
    			tmp.add("*Las contrase?s no coinciden");
			}
    		if (User.findFirst("nick = ?",nick) != null) {
    			tmp.add("*El nickname ya esta en uso");
			}
    		if (User.findFirst("dni = ?",Integer.parseInt(dni)) != null) {
    			tmp.add("*Ese dni ya esta registrado");
			}
    		if (User.findFirst("email = ?", mail) != null) {
    			tmp.add("*Ese email ya esta registrado");
			}
			if (!"traemelapromocionmessi".equals(pm) && pm!=null) {
    			tmp.add("*Palabla magica incorrecta");
			}
			mape.put("errorr", tmp);
			mape.put("paisl", Country.getAllCountrys());
			System.out.println("No se registro");
		}
    	return mape;
    };
}
package prode;

import static spark.Spark.*;
import spark.template.mustache.MustacheTemplateEngine;
import prode.Controladores.*;

public class App {

	public static void main(String[] args) {

		staticFiles.location("/public");

		notFound((req, res) -> {
			if (req.session().attribute("logueado") == null) {
				res.redirect("/");
			}
			res.redirect("/loged/profile");
			return null;
		});

		before("/loged/*", GeneralController.checkIfLoged);

		before("/loged/admin/*", GeneralController.checkIfAdmin);

		before("*", GeneralController.openConectionToDataBase);

		after("*", GeneralController.disconectDataBase);

		after("/exit", UserController.closeSession);

		get("/", UserController.gHome, new MustacheTemplateEngine());

		post("/", UserController.pHome, new MustacheTemplateEngine());

		get("/loged/prode", FixtureController.mainFixtures, new MustacheTemplateEngine());
		//

		post("/loged/prode", GeneralController.adminOrBet, new MustacheTemplateEngine());

		post("/loged/admin/", GeneralController.adminOrBet, new MustacheTemplateEngine());

		get("/loged/perfil", UserController.contain2Perfil, new MustacheTemplateEngine());

		get("/loged/results", PredictionController.verResults, new MustacheTemplateEngine());

		get("/loged/admin/", FixtureController.mainFixtures, new MustacheTemplateEngine());
	}

}
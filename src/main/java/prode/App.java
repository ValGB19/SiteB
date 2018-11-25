package prode;

import static spark.Spark.*;
import spark.template.mustache.MustacheTemplateEngine;
import prode.Controladores.*;

public class App {

	public static void main(String[] args) {

		staticFiles.location("/public");

		notFound((req, res) -> {
			if (req.session().attribute("logueado") == null)
				res.redirect("/");
			else
				res.redirect("/loged/profile");
			return null;
		});

		before("*", GeneralController.openConectionToDataBase);

		after("*", GeneralController.disconectDataBase);

		before("/loged/*", GeneralController.checkIfLoged);

		before("/admin/*", GeneralController.checkIfAdmin);

		after("/exit", UserController.closeSession);

		get("/", UserController.gHome, new MustacheTemplateEngine());

		post("/", UserController.pHome, new MustacheTemplateEngine());

		get("/reset", UserController.gResetPass, new MustacheTemplateEngine());

		post("/reset", UserController.pResetPass, new MustacheTemplateEngine());

		get("/loged/perfil", UserController.viewPerfil, new MustacheTemplateEngine());

		get("/loged/prode", FixtureController.betView, new MustacheTemplateEngine());

		post("/loged/prode", GeneralController.bet, new MustacheTemplateEngine());

		get("/results", PredictionController.verResults, new MustacheTemplateEngine());

		get("/admin/main", FixtureController.mainFixtures, new MustacheTemplateEngine());

		post("/admin/main", GeneralController.actionAdmin, new MustacheTemplateEngine());
	}
}
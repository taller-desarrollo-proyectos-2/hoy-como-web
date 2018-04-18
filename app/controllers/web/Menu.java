
package controllers.web;

import play.mvc.Result;
import static play.mvc.Results.ok;
import play.mvc.Controller;

/**
 *
 * @author aperrotta
 */
public class Menu extends Controller {
    
    public static Result showCategories() {
        return ok(views.html.menu.categories.render());
    }

    public static Result showOptionals() {
        return ok(views.html.menu.optionals.render());
    }

    public static Result showPlates() {
        return ok(views.html.menu.plates.render());
    }
}

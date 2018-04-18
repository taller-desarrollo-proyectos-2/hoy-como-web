
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
        return ok(views.html.users.render());
    }

    public static Result showOptionals() {
        return ok(views.html.users.render());
    }

    public static Result showPlates() {
        return ok(views.html.users.render());
    }
}

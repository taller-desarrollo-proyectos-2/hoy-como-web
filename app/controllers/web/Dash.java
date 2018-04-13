
package controllers.web;

import play.mvc.Result;
import static play.mvc.Results.ok;
import play.mvc.Controller;

/**
 *
 * @author aperrotta
 */
public class Dash extends Controller {
    
      public static Result showMainScreen() {
            return ok(views.html.main.render());
        }
    
}

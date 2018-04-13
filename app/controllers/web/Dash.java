
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
      
      public static Result showLogin() {
            return ok(views.html.login.render());
        }
      
      public static Result showDash() {
            return ok(views.html.dash.render());
        }
    
}

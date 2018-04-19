
package controllers.web;

import play.mvc.Result;
import static play.mvc.Results.ok;
import play.mvc.Controller;

/**
 *
 * @author aperrotta
 */
public class Users extends Controller {
    
      public static Result showUsers() {
            return ok(views.html.root.users.render());
      }
}


package controllers.web;

import play.mvc.Result;
import static play.mvc.Results.ok;
import play.mvc.Controller;

/**
 *
 * @author aperrotta
 */
public class Commerces extends Controller {
    
      public static Result showCommerces() {
            return ok(views.html.commerces.render());
        }
}

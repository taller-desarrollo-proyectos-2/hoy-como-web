
package controllers.web;

import play.mvc.Result;
import static play.mvc.Results.ok;
import play.mvc.Controller;

/**
 *
 * @author aperrotta
 */
public class Requests extends Controller {
    
      public static Result showCommerceRequests() {
            return ok(views.html.admin.commerces.render());
        }
}

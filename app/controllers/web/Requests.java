
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
            return ok(views.html.admin.requests.render());
      }
    
      public static Result showRootRequests() {
            return ok(views.html.root.requests.render());
      }
}

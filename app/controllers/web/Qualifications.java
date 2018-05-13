
package controllers.web;

import play.mvc.Result;
import static play.mvc.Results.ok;
import play.mvc.Controller;

/**
 *
 * @author aperrotta
 */
public class Qualifications extends Controller {
    
    public static Result showCommerceQualifications() {
        return ok(views.html.admin.qualifications.render());
    }

}
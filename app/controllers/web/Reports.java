
package controllers.web;

import play.mvc.Result;
import static play.mvc.Results.ok;
import play.mvc.Controller;

/**
 *
 * @author aperrotta
 */
public class Reports extends Controller {
    
    public static Result showCommerceReports() {
        return ok(views.html.admin.reports.render());
    }

    public static Result showRootReports() {
        return ok(views.html.root.reports.render());
    }

}
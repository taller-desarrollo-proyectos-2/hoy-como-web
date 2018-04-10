// @SOURCE:/Users/facundocaldora/workspace/hoy-como-web/conf/routes
// @HASH:abf057e502359a0575a240483417dc695c9e0183
// @DATE:Mon Apr 09 16:22:46 ART 2018


import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._
import play.libs.F

import Router.queryString

object Routes extends Router.Routes {

private var _prefix = "/"

def setPrefix(prefix: String) {
  _prefix = prefix
  List[(String,Routes)]().foreach {
    case (p, router) => router.setPrefix(prefix + (if(prefix.endsWith("/")) "" else "/") + p)
  }
}

def prefix = _prefix

lazy val defaultPrefix = { if(Routes.prefix.endsWith("/")) "" else "/" }


// @LINE:6
private[this] lazy val controllers_Assets_at0 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("assets/"),DynamicPart("file", """.+""",false))))
        

// @LINE:11
private[this] lazy val controllers_api_v1_BackofficeUsers_create1 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("api/v1/backoffice/users"))))
        

// @LINE:16
private[this] lazy val controllers_api_v1_Authorization_authenticate2 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("api/v1/authenticate"))))
        

// @LINE:20
private[this] lazy val controllers_api_v1_Commerces_create3 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("api/v1/commerce"))))
        
def documentation = List(("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """api/v1/backoffice/users""","""controllers.api.v1.BackofficeUsers.create()"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """api/v1/authenticate""","""controllers.api.v1.Authorization.authenticate()"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """api/v1/commerce""","""controllers.api.v1.Commerces.create()""")).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
  case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
  case l => s ++ l.asInstanceOf[List[(String,String,String)]] 
}}
      

def routes:PartialFunction[RequestHeader,Handler] = {

// @LINE:6
case controllers_Assets_at0(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        invokeHandler(controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]),"GET", """ Map static resources from the /public folder to the /assets URL path""", Routes.prefix + """assets/$file<.+>"""))
   }
}
        

// @LINE:11
case controllers_api_v1_BackofficeUsers_create1(params) => {
   call { 
        invokeHandler(controllers.api.v1.BackofficeUsers.create(), HandlerDef(this, "controllers.api.v1.BackofficeUsers", "create", Nil,"POST", """""", Routes.prefix + """api/v1/backoffice/users"""))
   }
}
        

// @LINE:16
case controllers_api_v1_Authorization_authenticate2(params) => {
   call { 
        invokeHandler(controllers.api.v1.Authorization.authenticate(), HandlerDef(this, "controllers.api.v1.Authorization", "authenticate", Nil,"POST", """""", Routes.prefix + """api/v1/authenticate"""))
   }
}
        

// @LINE:20
case controllers_api_v1_Commerces_create3(params) => {
   call { 
        invokeHandler(controllers.api.v1.Commerces.create(), HandlerDef(this, "controllers.api.v1.Commerces", "create", Nil,"POST", """""", Routes.prefix + """api/v1/commerce"""))
   }
}
        
}

}
     
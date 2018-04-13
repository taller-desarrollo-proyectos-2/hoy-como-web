// @SOURCE:/Users/facundocaldora/workspace/hoy-como-web/conf/routes
// @HASH:711876e4224d4b68923ddc471d48726b59e8f56d
// @DATE:Fri Apr 13 14:22:11 ART 2018

import Routes.{prefix => _prefix, defaultPrefix => _defaultPrefix}
import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._
import play.libs.F

import Router.queryString


// @LINE:6
package controllers {

// @LINE:6
class ReverseAssets {
    

// @LINE:6
def at(file:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                                                
    
}
                          
}
                  

// @LINE:21
// @LINE:17
// @LINE:16
// @LINE:11
package controllers.api.v1 {

// @LINE:11
class ReverseCommerceUsers {
    

// @LINE:11
def create(): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "api/v1/commerce/users")
}
                                                
    
}
                          

// @LINE:21
class ReverseCommerces {
    

// @LINE:21
def create(): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "api/v1/commerce")
}
                                                
    
}
                          

// @LINE:17
// @LINE:16
class ReverseAuthorization {
    

// @LINE:17
def facebookAuthenticate(): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "api/v1/authenticate/facebook")
}
                                                

// @LINE:16
def authenticate(): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "api/v1/authenticate")
}
                                                
    
}
                          
}
                  


// @LINE:6
package controllers.javascript {

// @LINE:6
class ReverseAssets {
    

// @LINE:6
def at : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                        
    
}
              
}
        

// @LINE:21
// @LINE:17
// @LINE:16
// @LINE:11
package controllers.api.v1.javascript {

// @LINE:11
class ReverseCommerceUsers {
    

// @LINE:11
def create : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.api.v1.CommerceUsers.create",
   """
      function() {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/v1/commerce/users"})
      }
   """
)
                        
    
}
              

// @LINE:21
class ReverseCommerces {
    

// @LINE:21
def create : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.api.v1.Commerces.create",
   """
      function() {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/v1/commerce"})
      }
   """
)
                        
    
}
              

// @LINE:17
// @LINE:16
class ReverseAuthorization {
    

// @LINE:17
def facebookAuthenticate : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.api.v1.Authorization.facebookAuthenticate",
   """
      function() {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/v1/authenticate/facebook"})
      }
   """
)
                        

// @LINE:16
def authenticate : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.api.v1.Authorization.authenticate",
   """
      function() {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/v1/authenticate"})
      }
   """
)
                        
    
}
              
}
        


// @LINE:6
package controllers.ref {


// @LINE:6
class ReverseAssets {
    

// @LINE:6
def at(path:String, file:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]), "GET", """ Map static resources from the /public folder to the /assets URL path""", _prefix + """assets/$file<.+>""")
)
                      
    
}
                          
}
        

// @LINE:21
// @LINE:17
// @LINE:16
// @LINE:11
package controllers.api.v1.ref {


// @LINE:11
class ReverseCommerceUsers {
    

// @LINE:11
def create(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.api.v1.CommerceUsers.create(), HandlerDef(this, "controllers.api.v1.CommerceUsers", "create", Seq(), "POST", """""", _prefix + """api/v1/commerce/users""")
)
                      
    
}
                          

// @LINE:21
class ReverseCommerces {
    

// @LINE:21
def create(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.api.v1.Commerces.create(), HandlerDef(this, "controllers.api.v1.Commerces", "create", Seq(), "POST", """""", _prefix + """api/v1/commerce""")
)
                      
    
}
                          

// @LINE:17
// @LINE:16
class ReverseAuthorization {
    

// @LINE:17
def facebookAuthenticate(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.api.v1.Authorization.facebookAuthenticate(), HandlerDef(this, "controllers.api.v1.Authorization", "facebookAuthenticate", Seq(), "POST", """""", _prefix + """api/v1/authenticate/facebook""")
)
                      

// @LINE:16
def authenticate(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.api.v1.Authorization.authenticate(), HandlerDef(this, "controllers.api.v1.Authorization", "authenticate", Seq(), "POST", """""", _prefix + """api/v1/authenticate""")
)
                      
    
}
                          
}
        
    
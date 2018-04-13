//hoyComoApp.controller('mainCtrl', function ($scope, $http, $window) {
//    
//    
//    $scope.menu = [{showName: 'Comercios', route: '/login', icon: 'ti-panel'}, {showName: 'la vida', route: '/lavida', icon: 'ti-rocket'}];
//        
//        
//    $scope.content = "/login";
//    
//    $scope.setContent = function(route){
//        $scope.dashContent = route;
//    };
//
//    $scope.isActive = function(route){
//        if($scope.dashContent === route) return true;
//        return false;
//    };
//    
//    $scope.login = function (){
//        $scope.content = "/login";
//    };
//    
//});

hoyComoApp.controller('mainCtrl', function ($scope, $http, $window, $rootScope) {

//    $http.get("/api/isAuthenticated")
//        .success(function(data) { 
//            $scope.content = "/dash";
//        }).error(function(){
//            $scope.content = "/login";
//        });

        $scope.content = "/login";

        
    $scope.$on("login", function (event, args) {    
            $scope.content = "/dash";
    });
    
    $scope.$on("logout", function (event, args) {
            $scope.content = "/login";
    });
        
});
        
hoyComoApp.controller('loginCtrl', function ($scope, $http, $filter, $window, $rootScope) {

    $scope.login = function(){
        $scope.args = {};
        $scope.$parent.$emit('login', $scope.args);
    }

});

hoyComoApp.controller('dashCtrl', function ($scope, $http, $filter, $window, $rootScope) {

        
    $scope.menu = [{showName: 'Comercios', route: '/fruta', icon: 'ti-panel'}, {showName: 'la vida', route: '/lavida', icon: 'ti-rocket'}];

    $scope.dashContent = $scope.menu[0].route;

    $scope.logOut = function(){
        $scope.args = {};
        $scope.$parent.$emit('logout', $scope.args);
    }

    $scope.setContent = function(route){
        $scope.dashContent = route;
    };

    $scope.isActive = function(route){
        return $scope.dashContent === route;
    };
});




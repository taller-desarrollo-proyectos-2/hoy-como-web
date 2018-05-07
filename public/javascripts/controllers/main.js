
hoyComoApp.controller('mainCtrl', function ($scope, $http, $window, $rootScope, toastr) {

if($window.localStorage.getItem("route")){
        $scope.content = "/dash";
        $rootScope.menu = JSON.parse($window.localStorage.getItem("menu"));
        $rootScope.myInfo = JSON.parse($window.localStorage.getItem("myInfo"));
    } else {
    $scope.content = "/login";
    }
    
    $scope.user = {};
    
    $scope.$on("login", function (event, args) {   
        $http({
            url: "/api/v1/authenticate",
            data: $scope.user,
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        }).success(function(data, status, headers, config){
            $rootScope.menu = data;
            $http({
                url: "/api/v1/users/myinfo",
                method: "GET"
            }).success(function(data, status, headers, config){
                $rootScope.myInfo = { user: data.username, commerce: data.commerce.businessName};
                $window.localStorage.setItem("myInfo", JSON.stringify($rootScope.myInfo));
                $scope.content = "/dash";
            })
        }).error(function(err){
            toastr.error(err.message);
        });
    });
    
    $scope.$on("logout", function (event, args) {
            $scope.content = "/login";
            $window.localStorage.removeItem("route");
            $window.localStorage.removeItem("menu");
    });
    
    $scope.$on("reload", function (event, args) {
        $window.localStorage.setItem("route", args);
        $window.localStorage.setItem("menu", JSON.stringify($rootScope.menu));
    });
        
});
        
hoyComoApp.controller('loginCtrl', function ($scope, $http, $filter, $window, $rootScope) {

    $scope.login = function(){
        $scope.args = {};
        $scope.$parent.$emit('login', $scope.args);
    }

});

hoyComoApp.controller('dashCtrl', function ($scope, $http, $filter, $window, $rootScope) {
    $scope.myInfo = $rootScope.myInfo;
    $scope.commerceName = $rootScope.commerceName;
    $scope.menu = $rootScope.menu;
    if($window.localStorage.getItem("route")){
        $scope.dashContent = $window.localStorage.getItem("route");

    } else {
        $scope.dashContent = $scope.menu[0].route;
    }
    //se setea la primera opcion al entrar, deberia guardarse la que esta seleccionada

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
    
    $(window).on('beforeunload', function(){
            $scope.$parent.$emit('reload', $scope.dashContent);
    });

});




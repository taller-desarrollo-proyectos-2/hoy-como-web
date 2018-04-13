
hoyComoApp.controller('mainCtrl', function ($scope, $http, $window, $rootScope) {

    $scope.content = "/login";

    $scope.user = {};

    $scope.$on("login", function (event, args) {    
        $http.post("/api/v1/authenticate", $scope.user)
        .success(function(data) {  
            $rootScope.menu = data;
            $scope.content = "/dash";
        }).error(function(err){
            $scope.content = "/login";
            // toastr.error(err);
        });
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

        $scope.menu = $rootScope.menu;
    // $scope.menu = [{showName: 'Comercios', route: '/fruta', icon: 'ti-panel'}, {showName: 'la vida', route: '/lavida', icon: 'ti-rocket'}];

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




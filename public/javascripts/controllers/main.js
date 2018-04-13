hoyComoApp.controller('mainCtrl', function ($scope, $http, $window) {
    
    
    $scope.menu = [{showName: 'Comercios', route: '/login', icon: 'ti-panel'}, {showName: 'la vida', route: '/lavida', icon: 'ti-rocket'}];
        
        
    $scope.content = "/dash";
    
    $scope.setContent = function(route){
        $scope.dashContent = route;
    };

    $scope.isActive = function(route){
        if($scope.dashContent === route) return true;
        return false;
    };
//    $http.get("ruta para saber si estoy logueado")
//        .success(function(data) { 
//            $scope.content = "/dash";
//        }).error(function(){
//            $scope.content = "/login";
//        });
    
});

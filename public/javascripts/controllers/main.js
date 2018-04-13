hoyComoApp.controller('mainCtrl', function ($scope, $http, $window) {
    
    
    $scope.menu = [{showName: 'Comercios', route: '/login', icon: 'ti-panel'}, {showName: 'la vida', route: '/lavida', icon: 'ti-rocket'}];
        
        
    $scope.content = "/login";
    
    $scope.setContent = function(route){
        $scope.dashContent = route;
    };

    $scope.isActive = function(route){
        if($scope.dashContent === route) return true;
        return false;
    };
    
    $scope.login = function (){
        $scope.content = "/login";
    };
    
});

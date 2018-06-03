hoyComoApp.controller('monitorCtrl', function ($scope, $http, $window, $rootScope, toastr) {

    $scope.date = new Date();

    $scope.setContent = function(route){
        $scope.args = {route: route};
        $scope.$parent.$emit('dashContent', $scope.args);
    }

});

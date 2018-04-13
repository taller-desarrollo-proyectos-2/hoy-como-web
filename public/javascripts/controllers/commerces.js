
hoyComoApp.controller('commercesCtrl', function ($scope, $http, $window, $rootScope, toastr) {
    $scope.commerces = [];

    $http({
        url: "/api/v1/commerces",
        method: "GET",
        headers: {
            'authorization' : $rootScope.auth
        }
    }).success(function(data, status, headers, config){
        $scope.commerces = data;
    }).error(function(err){
        toastr.error(err.message);
    });
});
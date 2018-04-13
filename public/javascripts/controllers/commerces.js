
hoyComoApp.controller('commercesCtrl', function ($scope, $http, $window, $rootScope, toastr) {
    $scope.commerces = [];

    $http.get("/api/v1/commerces")
        .success(function(data){
            $scope.commerces = data;
        }).error(function(err){
            toastr.error(err.message);
        });
});
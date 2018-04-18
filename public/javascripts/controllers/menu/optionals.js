
hoyComoApp.controller('optionalsCtrl', function ($scope, $http, $window, $rootScope, toastr, $filter) {
    $scope.optionals = [];

    index();

    function index(){
        $http({
            url: "/api/v1/optionals",
            method: "GET",
            headers: {
                'authorization' : $rootScope.auth
            }
        }).success(function(data, status, headers, config){
            $scope.optionals = data;
        }).error(function(err){
            toastr.error(err.message);
        });
    }
});


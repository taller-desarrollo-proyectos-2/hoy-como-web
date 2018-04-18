
hoyComoApp.controller('categoriesCtrl', function ($scope, $http, $window, $rootScope, toastr, $filter) {
    $scope.categories = [];

    index();

    function index(){
        $http({
            url: "/api/v1/categories",
            method: "GET",
            headers: {
                'authorization' : $rootScope.auth
            }
        }).success(function(data, status, headers, config){
            $scope.categories = data;
        }).error(function(err){
            toastr.error(err.message);
        });
    }
});


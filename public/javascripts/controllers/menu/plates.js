
hoyComoApp.controller('platesCtrl', function ($scope, $http, $window, $rootScope, toastr, $filter) {
    $scope.plates = [];
    $scope.currentPlate = {};

    index();

    function index(){
        $http({
            url: "/api/v1/plates",
            method: "GET",
            headers: {
                'authorization' : $rootScope.auth
            }
        }).success(function(data, status, headers, config){
            $scope.plates = data;
        }).error(function(err){
            toastr.error(err.message);
        });
    }

    $scope.toggleCreateModal = function() {
        $scope.editModal = false;
        $("#platesModal").modal("toggle");
    };
});

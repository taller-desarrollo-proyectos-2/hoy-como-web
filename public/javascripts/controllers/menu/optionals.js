
hoyComoApp.controller('optionalsCtrl', function ($scope, $http, $window, $rootScope, toastr, $filter) {
    $scope.optionals = [];
    $scope.currentOptional = {};

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

    $scope.toggleCreateModal = function() {
        $scope.editModal = false;
        $("#optionalsModal").modal("toggle");
    };

    $scope.createOptional = function(){
        $http({
            url: "/api/v1/optionals",
            data: $scope.currentOptional,
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'authorization' : $rootScope.auth
            }
        }).success(function(){
            $scope.currentOptional = {};
            $("#optionalsModal").modal("toggle");
            index();
            toastr.success("Opcional creado con exito.");
        }).error(function(err){
            toastr.error(err.message);
        });
    };
});


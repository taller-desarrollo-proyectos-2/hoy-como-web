
hoyComoApp.controller('optionalsCtrl', function ($scope, $http, $window, $rootScope, toastr, $filter) {
    $scope.optionals = [];
    $scope.currentOptional = {};
    $scope.editModal = true;

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
        if($scope.editModal) $scope.currentOptional = {};
        $scope.editModal = false;
        $("#optionalsModal").modal("toggle");
    };

    $scope.toggleEditModal = function(optional) {
        $scope.editModal = true;
        angular.copy(optional, $scope.currentOptional);
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

    $scope.updateOptional = function(){
        $http({
            url: "/api/v1/optionals/" + $scope.currentOptional.id,
            data: $scope.currentOptional,
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'authorization' : $rootScope.auth
            }
        }).success(function(){
            $scope.currentOptional = {};
            $("#optionalsModal").modal("toggle");
            index();
            toastr.success("Opcional modificado con exito.");
        }).error(function(err){
            toastr.error(err.message);
        });
    };

    $scope.deleteOptional = function(optional){
        $http({
            url: "/api/v1/optionals/" + optional.id,
            method: "DELETE",
            headers: {
                'authorization' : $rootScope.auth
            }
        }).success(function(){
            index();
            toastr.success("Opcional eliminado con exito.");
        }).error(function(err){
            toastr.error(err.message);
        });
    };
});


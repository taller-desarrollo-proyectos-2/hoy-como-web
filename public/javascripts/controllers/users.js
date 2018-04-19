
hoyComoApp.controller('usersCtrl', function ($scope, $http, $window, $rootScope, toastr) {
  
    $scope.users = [];
    $scope.commerces = [];
    $scope.currentUser = {};

    indexCommerces();
    indexUsers();
    
    function indexCommerces(){
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
    }

    function indexUsers(){
        $http({
            url: "/api/v1/commerce/users",
            method: "GET",
            headers: {
                'authorization' : $rootScope.auth
            }
        }).success(function(data, status, headers, config){
            $scope.users = data;
        }).error(function(err){
            toastr.error(err.message);
        });
    }

    $scope.toggleCreateModal = function() {
        if($scope.editModal) $scope.currentUser = {};
        $scope.editModal = false;
        $("#usersModal").modal("toggle");
    };

    $scope.toggleEditModal = function(user) {
        $scope.editModal = true;
        angular.copy(user, $scope.currentUser);
        $("#usersModal").modal("toggle");
    };
    
    $scope.createUser = function(){
        $http({
            url: "/api/v1/commerce/users",
            data: $scope.currentUser,
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'authorization' : $rootScope.auth
            }
        }).success(function(){
            $scope.currentUser = {};
            $("#usersModal").modal("toggle");
            indexUsers();
            toastr.success("Usuario creado con exito.");
        }).error(function(err){
            toastr.error(err.message);
        });
    };

    $scope.updateUser = function(){
        $http({
            url: "/api/v1/users/" + $scope.currentUser.id,
            data: $scope.currentUser,
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'authorization' : $rootScope.auth
            }
        }).success(function(){
            $scope.currentUser = {};
            $("#usersModal").modal("toggle");
            index();
            toastr.success("Usuario actualizado con exito.");
        }).error(function(err){
            toastr.error(err.message);
        });
    };

    $scope.delete = function(user){
        $http({
            url: "/api/v1/users/" + user.id,
            data: $scope.currentUser,
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'authorization' : $rootScope.auth
            }
        }).success(function(){
            index();
            toastr.success("Usuario eliminado con exito.");
        }).error(function(err){
            toastr.error(err.message);
        });
    };

});

hoyComoApp.controller('usersCtrl', function ($scope, $http, $window, $rootScope, toastr) {
  
    $scope.users = [];
    $scope.commerces = [];

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

    $scope.toggleCreateModal = function() {
        $scope.editModal = false;
        $scope.currentCommerce = {};
        $("#commercesModal").modal("toggle");
    };

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
        $scope.editModal = false;
        $scope.currentUser = {};
        $("#usersModal").modal("toggle");
    };
    
    $scope.createUser = function(user){
            $http({
                url: "/api/v1/commerce/users",
                data: user,
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
                $("#usersModal").modal("toggle");
                toastr.error(err.message);
            });
    };
});
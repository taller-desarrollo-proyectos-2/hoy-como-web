
hoyComoApp.controller('categoriesCtrl', function ($scope, $http, $window, $rootScope, toastr, $filter) {
    $scope.categories = [];
    $scope.currentCategory = {};

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

    $scope.toggleCreateModal = function() {
        $scope.editModal = false;
        $("#categoriesModal").modal("toggle");
    };

    $scope.toggleEditModal = function(category) {
        $scope.editModal = true;
        angular.copy(category, $scope.currentCategory);
        $("#categoriesModal").modal("toggle");
    };

    $scope.createCategory = function(){
        $http({
            url: "/api/v1/categories",
            data: $scope.currentCategory,
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'authorization' : $rootScope.auth
            }
        }).success(function(){
            $scope.currentCategory = {};
            $("#categoriesModal").modal("toggle");
            index();
            toastr.success("Categoria creado con exito.");
        }).error(function(err){
            toastr.error(err.message);
        });
    };

    $scope.updateCategory = function(){
        $http({
            url: "/api/v1/categories/" + $scope.currentCategory.id,
            data: $scope.currentCategory,
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'authorization' : $rootScope.auth
            }
        }).success(function(){
            $scope.currentCategory = {};
            $("#categoriesModal").modal("toggle");
            index();
            toastr.success("Categoria actualizada con exito.");
        }).error(function(err){
            toastr.error(err.message);
        });
    };

    $scope.delete = function(category){
        $http({
            url: "/api/v1/categories/" + category.id,
            data: $scope.currentUser,
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'authorization' : $rootScope.auth
            }
        }).success(function(){
            index();
            toastr.success("Categoria eliminada con exito.");
        }).error(function(err){
            toastr.error(err.message);
        });
    };

});


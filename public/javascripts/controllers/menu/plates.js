
hoyComoApp.controller('platesCtrl', function ($scope, $http, $window, $rootScope, toastr, $filter) {
    
    $scope.plates = [];
    $scope.currentPlate = {};
    $scope.categories = [];

    indexPlates();
    indexCategories();

    function indexPlates(){
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

    function indexCategories(){
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
        $("#platesModal").modal("toggle");
    };

    $scope.toggleCategoryCreationModal = function() {
        $scope.currentCategory = {};
        $("#platesModal").modal("toggle");
        $("#categoriesModal").modal("toggle");
    };

    $scope.backToPlatesModal = function() {
        $scope.currentCategory = {};
        $("#categoriesModal").modal("toggle");
        $("#platesModal").modal("toggle");
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
        }).success(function(data){
            $scope.currentPlate.category = data;
            $scope.currentCategory = {};
            $("#categoriesModal").modal("toggle");
            $("#platesModal").modal("toggle");
            indexCategories();
            toastr.success("Categoria creado con exito.");
        }).error(function(err){
            toastr.error(err.message);
        });
    };


});

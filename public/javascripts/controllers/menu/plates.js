
hoyComoApp.controller('platesCtrl', function ($scope, $http, $window, $rootScope, toastr, $filter) {
    $scope.plates = [];
    $scope.currentPlate = {};

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
        $scope.category = {};
        $("#platesModal").modal("toggle");
        $("#categoriesModal").modal("toggle");
    };

    $scope.backToPlatesModal = function() {
        $scope.category = {};
        $("#categoriesModal").modal("toggle");
        $("#platesModal").modal("toggle");
    };


});

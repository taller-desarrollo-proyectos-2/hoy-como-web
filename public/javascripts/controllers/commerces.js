
hoyComoApp.controller('commercesCtrl', function ($scope, $http, $window, $rootScope, toastr) {
  
    $scope.commerces = [];

    index();
    
    function index(){
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
    
    $scope.createCommerce = function(commerce){
        if(commerce.businessName){
            $http({
                url: "/api/v1/commerces",
                data: commerce,
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                    'authorization' : $rootScope.auth
                }
            }).success(function(){
                $scope.currentCommerce = {};
                $("#commercesModal").modal("toggle");
                index();
                toastr.success("Comercio creado con exito.");
            }).error(function(err){
                $("#commercesModal").modal("toggle");
                toastr.error(err.message);
            });
        } else {
            toastr.error("La razon social no puede estar vacia.");
        }
    };
});
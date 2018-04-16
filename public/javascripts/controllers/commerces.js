
hoyComoApp.controller('commercesCtrl', function ($scope, $http, $window, $rootScope, toastr) {
  
    $scope.commerces = [];

    index();
    $scope.daySelected = {};
    $scope.actualPhone = {};
    //Cambiar por consulta back-end;
    $scope.days = [
        "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO", "TODOS LOS DÍAS"
    ];
    
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
            commerce.phones = [];
            commerce.phones.push($scope.actualPhone);
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

    $scope.addCurrentTime = function(){
        if(!$scope.currentCommerce.times){
            $scope.currentCommerce.times = [];
        }
        if($scope.daySelected && $scope.from && $scope.to) {
            $scope.currentCommerce.times.push({
                "day": $scope.daySelected,
                "fromHour": $scope.from,
                "toHour": $scope.to
            });
        }else{
            toastr.error("Seleccione un día y un horario de comienzo y fin.");
        }
    }

    $scope.deleteTime = function(index){
        $scope.currentCommerce.times.splice(index, 1);
    }
});
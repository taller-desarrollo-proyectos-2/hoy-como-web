hoyComoApp.controller('reportsCtrl', function ($scope, $http, $window, $rootScope, toastr, $interval, $uibModal) {

    $scope.data = {};
    $scope.validPeriodSelected = false;

    $scope.checkPeriod = () =>{
        if (!$scope.data.from) toastr.error("Se debe seleccionar una fecha inicial para visualizar el reporte"); 
        if (!$scope.data.to) toastr.error("Se debe seleccionar una fecha final para visualizar el reporte"); 
        if($scope.data.from && $scope.data.to){
            if ($scope.data.to < $scope.data.from) {
                toastr.error("Revise las fechas ingresadas"); 
            } else {
                $scope.validPeriodSelected = true;
            }
        }
    };

    $scope.back = () =>{
        $scope.validPeriodSelected = false;
    };


});

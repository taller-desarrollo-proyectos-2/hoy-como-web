hoyComoApp.controller('reportsCtrl', function ($scope, $http, $window, $rootScope, toastr, $interval, $uibModal) {

    $scope.data = {};
    $scope.data.from = new Date();
    $scope.data.from.setMonth($scope.data.from.getMonth()-1);
    $scope.data.to = new Date();
    $scope.validPeriodSelected = false;

    $scope.checkPeriod = () =>{
        if (!$scope.data.from) toastr.error("Se debe seleccionar una fecha inicial para visualizar el reporte"); 
        if (!$scope.data.to) toastr.error("Se debe seleccionar una fecha final para visualizar el reporte"); 
        if($scope.data.from && $scope.data.to){
            if ($scope.data.to < $scope.data.from) {
                toastr.error("Revise las fechas ingresadas"); 
            } else if (dateDiffInDays() > 31){
                toastr.error("El periodo seleccionado no puede ser mayor a 31 dias."); 
            }else {
                $scope.validPeriodSelected = true;
                getData();
            }
        }
    };

    $scope.back = () =>{
        $scope.validPeriodSelected = false;
    };

    function dateDiffInDays () {
        var diff =($scope.data.to.getTime() - $scope.data.from.getTime()) / 1000;
        diff /= (60 * 60 * 24);
        return Math.abs(Math.round(diff));
    }

    function getData(){
        var from = $scope.data.from.toISOString().split("T")[0];
        var to = $scope.data.to.toISOString().split("T")[0];
        $http({
            url: "/api/v1/reports?from=" + from + "&to=" + to,
            method: "GET"
        }).success(function(data, status, headers, config){
            $scope.reports = data;
        }).error(function(err){
            toastr.error(err.message);
        });
    }
});

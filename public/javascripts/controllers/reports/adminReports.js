hoyComoApp.controller('reportsCtrl', function ($scope, $http, $window, $rootScope, toastr, $interval, $uibModal) {

    $scope.data = {};
    $scope.data.from = new Date();
    $scope.data.from.setMonth($scope.data.from.getMonth()-1);
    $scope.data.to = new Date();
    $scope.validPeriodSelected = true;
    getData();

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
            calculateTotals();
        }).error(function(err){
            toastr.error(err.message);
        });
    }


    $scope.export = function(){

        var from = $scope.data.from.toISOString().split("T")[0];
        var to = $scope.data.to.toISOString().split("T")[0];
        var url= "/api/v1/reports/export?from=" + from + "&to=" + to;
            var xhr = new XMLHttpRequest();
            xhr.responseType = 'blob';
            xhr.onload = function() {
              if(this.status === 200){
                var a = document.createElement('a');
                a.href = window.URL.createObjectURL(xhr.response); // xhr.response is a blob
                a.style.display = 'none';
                a.download = "Reporte.xls";
                document.body.appendChild(a);
                a.click();
                delete a;
              }else{
                    toastr.error("Error exportando reporte.");
              }
            };
            xhr.open('GET', url);
            xhr.send();
    };

    function calculateTotals(){
        $scope.totals = {};
        $scope.totals.totalRequests = 0;
        $scope.totals.totalBilled = 0;
        $scope.totals.totalFee = 0;
        $scope.totals.avgLeadTime = 0;
        $scope.totals.avgQualif = 0;
        var totalQualifs = 0;
        var totalLeadTimes = 0;
        for(var report of $scope.reports){
            $scope.totals.totalRequests += report.requests;
            $scope.totals.totalBilled += report.billed;
            $scope.totals.totalFee += report.fee;
            if (report.leadTime != 0){
                $scope.totals.avgLeadTime += report.leadTime;
                totalLeadTimes ++;
            } 
            if (report.score != 0){
                $scope.totals.avgQualif += report.score;
                totalQualifs ++;
            } 
        }
        if(totalQualifs){
            $scope.califs = true;
            $scope.totals.avgQualif /= totalQualifs;
        } else {
            $scope.califs = false;
            $scope.totals.avgQualif = "No hubieron calificaciones"
        }
        $scope.totals.avgLeadTime /= totalLeadTimes;
    }

});

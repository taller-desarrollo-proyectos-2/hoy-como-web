hoyComoApp.controller('monitorCtrl', function ($scope, $http, $window, $rootScope, toastr, $q, $interval) {
   
    $scope.indicators = {};
    $scope.date = new Date();
    var HOY_COMO_TAX = 0.1;

    $scope.setContent = function(route){
        $scope.args = {route: route};
        $scope.$parent.$emit('dashContent', $scope.args);
    }

    getIndicators();

    var polling = $interval(function () {
        getIndicators();
    }, 5000);

    $scope.$on("$destroy", function () {
        $interval.cancel(polling);
    });

    function getIndicators(){
        var dailyIndicators = $http({method: 'GET', url: "/api/v1/dashboard/info?from=" + $scope.date.toISOString()});
        var historicIndicators = $http({method: 'GET', url: "/api/v1/users/myinfo"});

        $q.all([dailyIndicators, historicIndicators]).then(function (results) {
            $scope.indicators = {};
            $scope.indicators.pendingRequests = results[0].data.requests.WAITING_CONFIRMATION;
            $scope.indicators.acceptedRequests = results[0].data.requests.ON_PREPARATION;
            $scope.indicators.deliveredRequests = results[0].data.requests.DELIVERED;
            $scope.indicators.cancelledRequests = results[0].data.requests.CANCELLED_BY_COMMERCE + results[0].data.requests.CANCELLED_BY_USER;
            $scope.indicators.requestsOnTheWay = results[0].data.requests.ON_THE_WAY;
            $scope.indicators.requestsRatio = ($scope.indicators.cancelledRequests === 0)? "-" : $scope.indicators.deliveredRequests / $scope.indicators.cancelledRequests;
            $scope.indicators.historicQualif = results[1].data.commerce.score;
            $scope.indicators.dailyQualif = results[0].data.qualifications.score;
            $scope.indicators.historicLeadTime = results[1].data.commerce.leadTime;
            $scope.indicators.dailyLeadTime = results[0].data.leadTime + " m";
            $scope.indicators.totalMoney = results[0].data.money;
            $scope.indicators.qualifsWithoutResponse = results[0].data.qualifications.withoutResponse * HOY_COMO_TAX;
            $scope.showDailyQualif = true;
            if($scope.indicators.dailyQualif == 0) $scope.showDailyQualif = false; 
            $scope.showDailyLeadTime = true;
            if($scope.indicators.dailyLeadTime == "0 m") $scope.showDailyLeadTime = false; 
        });
    }

});

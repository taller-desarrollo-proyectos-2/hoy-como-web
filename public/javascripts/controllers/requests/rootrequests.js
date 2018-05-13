
hoyComoApp.controller('requestsRootCtrl', function ($scope, $http, $interval, $window, $rootScope, toastr, $filter, $uibModal) {
    
    $scope.currentCommerce = {};
    $scope.requests = [];
    $scope.commerces = [];
    $scope.statusEnum = {
                        waitingConfirmation: "WAITING_CONFIRMATION",
                        onPreparation: "ON_PREPARATION", 
                        cancelledByUser: "CANCELLED_BY_USER", 
                        cancelledByCommerce: "CANCELLED_BY_COMMERCE", 
                        delivered: "DELIVERED",
                        onTheWay: "ON_THE_WAY" 
                    };

    $scope.polling = undefined;

    indexCommerces();

    function indexCommerces() {
        $http({
            url: "/api/v1/commerces",
            method: "GET"
        }).success(function(data, status, headers, config){
            $scope.commerces = data;
        }).error(function(err){
            toastr.error(err.message);
        });
    }

    $scope.index = function() {
        $http({
            url: "/api/v1/requests?commerceId=" + $scope.currentCommerce.id,
            method: "GET"
        }).success(function(data, status, headers, config){
            $scope.requests = data;
            if($scope.polling === undefined){
                $scope.polling = $interval(function () {
                    $scope.index();
                }, 3000);
            }
        }).error(function(err){
            toastr.error(err.message);
        });
    }

    $scope.$on("$destroy", function () {
        $interval.cancel($scope.polling);
    });

    $scope.updateRequestStatus = (request, status) => {
        var data = {status: status};
        $http({
            url: "/api/v1/requests/" + request.id,
            method: "PUT",
            data: data
        }).success(function(data, status, headers, config){
            toastr.success("Estado de pedido actualizado con exito.");
            $scope.index();
        }).error(function(err){
            toastr.error(err.message);
        });
    };

    //--------------- MODALS --------------------//

    $scope.openUserDetailsModal = (user, destination) =>{
        const modalInstance = $uibModal.open({
            animation: true,
            templateUrl: '/assets/javascripts/controllers/requests/userDetailsModal/userinfomodal.html',
            controller: 'userDetailsCtrl',
            size: "md",
            resolve: {
              user: () =>  user,
              destination: () =>  destination
            }
          });
    };  

    $scope.openRequestDetailsModal = (request) =>{
        const modalInstance = $uibModal.open({
            animation: true,
            templateUrl: '/assets/javascripts/controllers/requests/requestDetailsModal/requestinfomodal.html',
            controller: 'requestDetailsCtrl',
            size: "md",
            resolve: {
                singleRequests: () =>  request.singleRequests,
                number: () =>  request.id
            }
          });
    };
});


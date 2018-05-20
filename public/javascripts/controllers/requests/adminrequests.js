
hoyComoApp.controller('requestsAdminCtrl', function ($scope, $http, $interval, $window, $rootScope, toastr, $filter, $uibModal) {
    
    $scope.requests = [];
    $scope.statusEnum = {
                        waitingConfirmation: "WAITING_CONFIRMATION",
                        onPreparation: "ON_PREPARATION", 
                        cancelledByUser: "CANCELLED_BY_USER", 
                        cancelledByCommerce: "CANCELLED_BY_COMMERCE", 
                        delivered: "DELIVERED",
                        onTheWay: "ON_THE_WAY" 
                    };

    index();

    var polling = $interval(function () {
        index();
    }, 3000);

    $scope.$on("$destroy", function () {
        $interval.cancel(polling);
    });

    function index() {
        $http({
            url: "/api/v1/requests",
            method: "GET"
        }).success(function(data, status, headers, config){
            $scope.requests = data;
        }).error(function(err){
            toastr.error(err.message);
        });
    }

    $scope.updateRequestStatus = (request, status) => {
        var data = {status: status};
        $http({
            url: "/api/v1/requests/" + request.id,
            method: "PUT",
            data: data
        }).success(function(data, status, headers, config){
            toastr.success("Estado de pedido actualizado con exito.");
            index();
        }).error(function(err){
            toastr.error(err.message);
        });
    };

    $scope.cancelRequest = (reason) => {
        var data = {status: $scope.statusEnum.cancelledByCommerce,
                    reason: reason};
        if(reason === undefined || reason === "") {
            toastr.error("El motivo de cancelación no puede estar vacio.");
        } else {
            $http({
                url: "/api/v1/requests/" + $scope.currentRequest.id,
                method: "PUT",
                data: data
            }).success(function(data, status, headers, config){
                toastr.success("Pedido cancelado con exito.");
                $scope.reason = undefined;
                $scope.currentRequest = undefined;
                $("#cancelationModal").modal("toggle");
                index();
            }).error(function(err){
                toastr.error(err.message);
            });
        }
    };

    //--------------- MODALS --------------------//

    $scope.toggleCancelationModal = (request) =>{
        $scope.currentRequest = request;
        $("#cancelationModal").modal("toggle");
        $scope.cancelationReason = undefined;
    };

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

    $scope.viewRejectReason = (request) => {
        $scope.cancelationReason = request.reason;
        $("#viewCancelationModal").modal("toggle");
    };
});


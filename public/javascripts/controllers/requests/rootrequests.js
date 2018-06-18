
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

    $scope.status = [
        { showName: "Todos", filter: ''},
        { showName: "Pendiente", filter: $scope.statusEnum.waitingConfirmation},
        { showName: "En Preparación", filter: $scope.statusEnum.onPreparation},
        { showName: "En Camino", filter: $scope.statusEnum.onTheWay},
        { showName: "Entregado", filter: $scope.statusEnum.delivered},
        { showName: "Cancelado", filter: $scope.statusEnum.cancelledByUser},
        { showName: "Rechazado", filter: $scope.statusEnum.cancelledByCommerce}
    ];

    $scope.selectedStatus = $scope.status[0];
    $scope.selectedDates = {};

    $scope.selectStatus = function(status){
        $scope.selectedStatus = status;
        $scope.index();;
    };

    $scope.isActive = function(state){
        return (state.filter === $scope.selectedStatus.filter);
    };

    function generateFiltersString (){
        var filtersList = [];
        var filterString = "";
        if ($scope.selectedStatus.filter != '') filtersList.push({key:"status", value: $scope.selectedStatus.filter});
        for(filter of filtersList){
            filterString += "&";
            filterString += filter.key;
            filterString += "=";
            filterString += filter.value;
        }
        return filterString;
    }

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
        var filters = generateFiltersString();
        $http({
            url: "/api/v1/requests?commerceId=" + $scope.currentCommerce.id + filters,
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


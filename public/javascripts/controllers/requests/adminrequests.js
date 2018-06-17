
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
        index();
    };

    $scope.isActive = function(state){
        return (state.filter === $scope.selectedStatus.filter);
    };

    $scope.setDateFilters = function (){
        $scope.selectedDates.from = $scope.filter.date.from;
        $scope.selectedDates.to = $scope.filter.date.to;
        index();
        toastr.success("Los filtros de fecha han sido actualizados.");
        $scope.datesFiltered = true;
    };

    $scope.clearDateFilters = function (){
        $scope.selectedDates = {};
        index();
        toastr.success("Los filtros de fecha han sido limpiados.");
        $scope.datesFiltered = false;
    };

    $scope.filter = {};
    index();

    var polling = $interval(function () {
        index();
    }, 3000);

    $scope.$on("$destroy", function () {
        $interval.cancel(polling);
    });

    function generateFiltersString (){
        var filtersList = [];
        var filterString = "";
        if ($scope.selectedStatus.filter != '') filtersList.push({key:"status", value: $scope.selectedStatus.filter});
        if ($scope.selectedDates.from) filtersList.push({key:"from", value: $scope.selectedDates.from.toISOString()});
        if ($scope.selectedDates.to) filtersList.push({key:"to", value: $scope.selectedDates.to.toISOString()});
        if (filtersList.length > 0) filterString = "?";
        for(filter of filtersList){
            filterString += filter.key;
            filterString += "=";
            filterString += filter.value;
            filterString += "&";
        }
        if (filterString.length > 0) filterString.slice(0, filterString.length-1);
        return filterString;
    }

    function index() {
        var filters = generateFiltersString();
        $http({
            url: "/api/v1/requests" + filters,
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
            rejectedReason: reason};
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
        $scope.cancelationReason = request.rejectedReason;
        $("#viewCancelationModal").modal("toggle");
    };

    $scope.cleanStatusFilters = function (){
        $scope.filter.status = {};
    };
});



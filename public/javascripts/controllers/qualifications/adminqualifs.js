hoyComoApp.controller('qualifCtrl', function ($scope, $http, $window, $rootScope, toastr, $interval, $uibModal) {

     index();

     var polling = $interval(function () {
        index();
    }, 3000);

    $scope.$on("$destroy", function () {
        $interval.cancel(polling);
    });

    function index() {
        $http({
            url: "/api/v1/qualifications",
            method: "GET"
        }).success(function(data, status, headers, config){
            $scope.qualifications = data;
        }).error(function(err){
            toastr.error(err.message);
        });
    }

    $scope.openQualifDetailsModal = (qualification) =>{
        const modalInstance = $uibModal.open({
            animation: true,
            templateUrl: '/assets/javascripts/controllers/qualifications/qualifDetailModal/qualifinfomodal.html',
            controller: 'qualifDetailsCtrl',
            size: "md",
            resolve: {
                    qualif: () =>  qualification
                }
            }).closed.then(function(){
                index();
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

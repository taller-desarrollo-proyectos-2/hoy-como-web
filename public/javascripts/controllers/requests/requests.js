
hoyComoApp.controller('requestsCtrl', function ($scope, $http, $window, $rootScope, toastr, $filter, $uibModal) {

    index();

    function index(){
        $http({
            url: "/api/v1/requests",
            method: "GET"
        }).success(function(data, status, headers, config){
            $scope.requests = data;
        }).error(function(err){
            toastr.error(err.message);
        });
    }

    $scope.openUserDetailsModal = (user) =>{
        const modalInstance = $uibModal.open({
            animation: true,
            templateUrl: '/assets/javascripts/controllers/requests/userDetailsModal/userinfomodal.html',
            controller: 'userDetailsCtrl',
            size: "md",
            resolve: {
              user: () =>  user
            }
          });
    };  
});


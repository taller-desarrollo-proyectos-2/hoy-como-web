
hoyComoApp.controller('requestsCtrl', function ($scope, $http, $window, $rootScope, toastr, $filter, $uibModal) {

    $scope.openUserDetailsModal = (user) =>{
        const modalInstance = $uibModal.open({
            animation: true,
            templateUrl: '/assets/javascripts/controllers/requests/userDetailsModal/userinfomodal.html',
            controller: 'userDetailsCtrl',
            size: "md",
            resolve: {
              user: () => {
                  return {name: "agus", address: "roosevelt"}
              }
            }
          });
    };  
});



hoyComoApp.controller('userDetailsCtrl', function ($scope, user, destination, $uibModalInstance) {
    $scope.destination = destination;
    $scope.user = user;

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
      };
});

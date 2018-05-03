
hoyComoApp.controller('userDetailsCtrl', function ($scope, user, $uibModalInstance) {
    $scope.user = user;

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
      };
});

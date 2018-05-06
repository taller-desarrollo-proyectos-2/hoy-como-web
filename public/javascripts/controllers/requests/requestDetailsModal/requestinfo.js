
hoyComoApp.controller('requestDetailsCtrl', function ($scope, number, singleRequests, $uibModalInstance) {
    $scope.requestNumber = number;
    $scope.singleRequests = singleRequests;

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
      };
});

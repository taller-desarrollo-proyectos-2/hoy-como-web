
hoyComoApp.controller('qualifDetailsCtrl', function ($scope, qualif, $http, $uibModalInstance, toastr) {
    $scope.qualif = qualif;
    $scope.data = {response: undefined};


    $scope.cancel = () => {
        $uibModalInstance.dismiss('cancel');
      };

    $scope.reply = () => {
        if($scope.data.response){
            $http({
                url: "/api/v1/qualifications/" + qualif.id,
                method: "PUT",
                data: {response: $scope.data.response}
            }).success(function(data, status, headers, config){
                toastr.success("Calificación replicada con exito.");
                $uibModalInstance.dismiss('cancel');
            }).error(function(err){
                toastr.error(err.message);
            });
        } else {
            toastr.error("la réplica no puede estar vacia.");
        }
    };
});

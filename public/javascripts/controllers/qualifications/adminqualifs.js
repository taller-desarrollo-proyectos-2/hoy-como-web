hoyComoApp.controller('qualifCtrl', function ($scope, $http, $window, $rootScope, toastr) {

    $scope.qualifications = [{
                                date: "20/3/18 21:30hs",
                                rating: 4,
                                username: "Agus",
                                comment: "Muy buena la comida."
                            }, {
                                date: "20/3/18 19:00hs",
                                rating: 2,
                                username: "Juan",
                                comment: "Un desastre",
                                replica: "Lamentamos lo ocurrido."
                            }];

});

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
                            }, {
                                date: "19/3/18 14:00hs",
                                rating: 5,
                                username: "Roberto",
                                comment: "Perfecto como siempre!",
                                replica: "Esperamos tu pedido nuevamente."
                            }, {
                                date: "19/3/18 8:00hs",
                                rating: 3,
                                username: "Jorge",
                                comment: "Todo bien.."
                            }, {
                                date: "17/3/18 12:00hs",
                                rating: 1,
                                username: "Micaela",
                                comment: "Nunca más."
                            }];

});

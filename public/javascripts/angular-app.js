var hoyComoApp = angular.module('hoyComo', ['toastr', 'ui.select', 'ui.bootstrap', 'GoogleMaps']);

hoyComoApp.config(function(toastrConfig) {
    angular.extend(toastrConfig, {
      autoDismiss: false,
      containerId: 'toast-container',
      maxOpened: 0,    
      newestOnTop: true,
      positionClass: 'toast-bottom-right',
      preventDuplicates: false,
      preventOpenDuplicates: false,
      target: 'body'
    });
});

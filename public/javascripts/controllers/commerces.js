
hoyComoApp.controller('commercesCtrl', function ($scope, $http, $window, $rootScope, toastr, $filter, $compile, GoogleMaps) {
    $scope.commerces = [];
    $scope.currentCommerce = {};
    $scope.editModal = true;
    $scope.fotoPreview = false;
    $scope.imageSrc = "assets/images/uploadImage.png";


    index();
    indexCategories();
    
    $scope.daySelected = {};
    $scope.actualPhone = {};
    //Cambiar por consulta back-end;
    $scope.days = [
        "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO", "TODOS LOS DÍAS"
    ];
    
    function index(){
        $http({
            url: "/api/v1/commerces",
            method: "GET"
        }).success(function(data, status, headers, config){
            $scope.commerces = data;
        }).error(function(err){
            toastr.error(err.message);
        });
    }


    function indexCategories(){
        $http({
            url: "/api/v1/commercecategories",
            method: "GET",
            headers: {
                'authorization' : $rootScope.auth
            }
        }).success(function(data, status, headers, config){
            $scope.categories = data;
        }).error(function(err){
            toastr.error(err.message);
        });
    }

    $scope.toggleCreateModal = function() {
        if($scope.editModal){
            $('#commerceForm').get(0).reset();
            $scope.imageSrc = "assets/images/uploadImage.png";
            $scope.currentCommerce = {};
            $scope.fotoPreview = false;
            $scope.actualPhone = {};
            $scope.cleanMapAddress();
        }
        $scope.editModal = false;
        $("#commercesModal").modal("toggle");
    };

    $scope.toggleEditModal = function(commerce) {
        $scope.editModal = true;
        $('#commerceForm').get(0).reset();
        $scope.fotoPreview = false;
        $scope.imageSrc = "assets/images/uploadImage.png";
        angular.copy(commerce, $scope.currentCommerce);
        $scope.actualPhone = $scope.currentCommerce.phones[0];
        $("#commercesModal").modal("toggle");
        $scope.loadMapAddress($scope.currentCommerce.address.street, $scope.currentCommerce.address.number);
    };

    $scope.createCommerce = function (){
        if($scope.currentCommerce.businessName && $scope.currentCommerce.categories && $scope.currentCommerce.times && $scope.currentCommerce.address.street && $scope.currentCommerce.address.number){
            var formData = new FormData($('#commerceForm').get(0)); 
            if($scope.currentCommerce.name) formData.append("name", $scope.currentCommerce.name);
            if($scope.currentCommerce.businessName) formData.append("businessName", $scope.currentCommerce.businessName);
            if($scope.currentCommerce.email) formData.append("email", $scope.currentCommerce.email);
            if($scope.currentCommerce.address){
                if($scope.currentCommerce.address.street) formData.append("address.street", $scope.currentCommerce.address.street);
                if($scope.currentCommerce.address.number) formData.append("address.number", $scope.currentCommerce.address.number);
            }
            if($scope.actualPhone.number) formData.append("phones[0].number", $scope.actualPhone.number);
            if (document.getElementById('fileInput').files.item(0)) formData.append("pictureFileName", document.getElementById('fileInput').files.item(0).name); 
            var index = 0;
            for (var category of $scope.currentCommerce.categories) {
                formData.append('categories[' + index + '].id', category.id);
                index++;
            }
            index = 0;
            for (var time of $scope.currentCommerce.times) {
                formData.append('times[' + index + '].from', time.from);
                formData.append('times[' + index + '].day', time.day);
                formData.append('times[' + index + '].to', time.to);
                index++;
            }
            formData.append("suspended", false);
            xhr = new XMLHttpRequest();
            xhr.addEventListener('load', createFinish, false);
            xhr.open('POST',"/api/v1/commerces");
            xhr.setRequestHeader('Accept','application/json, text/plain, */*');
            xhr.send(formData);
        } else {
            toastr.error("La razon social, categoria, direccion y horarios no pueden estar vacios.");
        }
    };

    function createFinish(e){ 
        if(this.status === 200){
            $('#commerceForm').get(0).reset();
            toastr.success("Comercio creado con exito.");
            $scope.currentCommerce = {};
            $scope.imageSrc = "assets/images/uploadImage.png";
            $("#commercesModal").modal("toggle");
            $scope.actualPhone = {};
            $scope.daySelected = undefined;
            $scope.from = undefined;
            $scope.to = undefined;
            index();
        } else {
            toastr.error("No se pudo crear el comercio.");
        }
    }

    function setTimes(){
        for(var i =0; i<$scope.currentCommerce.times.length; i++){
            if($scope.currentCommerce.times[i].fromHour) {
                $scope.currentCommerce.times[i].from = $scope.currentCommerce.times[i].fromHour;
                delete $scope.currentCommerce.times[i].fromHour;
            }
            if($scope.currentCommerce.times[i].toHour) {
                $scope.currentCommerce.times[i].to = $scope.currentCommerce.times[i].toHour;
                delete $scope.currentCommerce.times[i].toHour;
            }
        }
    }

    $scope.updateCommerce = function(){
        if($scope.currentCommerce.businessName && $scope.currentCommerce.categories && $scope.currentCommerce.times && $scope.currentCommerce.address.street && $scope.currentCommerce.address.number){
            setTimes();
            var formData = new FormData($('#commerceForm').get(0)); 
            if($scope.currentCommerce.name) formData.append("name", $scope.currentCommerce.name);
            if($scope.currentCommerce.businessName) formData.append("businessName", $scope.currentCommerce.businessName);
            if($scope.currentCommerce.email) formData.append("email", $scope.currentCommerce.email);
            if($scope.currentCommerce.address){
                if($scope.currentCommerce.address.id) formData.append("address.id", $scope.currentCommerce.address.id);
                if($scope.currentCommerce.address.street) formData.append("address.street", $scope.currentCommerce.address.street);
                if($scope.currentCommerce.address.number) formData.append("address.number", $scope.currentCommerce.address.number);
            }
            if($scope.actualPhone != {}) formData.append("phones[0].number", $scope.actualPhone.number);
            if (document.getElementById('fileInput').files.item(0)) formData.append("pictureFileName", document.getElementById('fileInput').files.item(0).name); 
            var index = 0;
            for (var category of $scope.currentCommerce.categories) {
                formData.append('categories[' + index + '].id', category.id);
                index++;
            }
            index = 0;
            for (var time of $scope.currentCommerce.times) {
                if(time.id) formData.append('times[' + index + '].id', time.id);
                formData.append('times[' + index + '].from', time.from);
                formData.append('times[' + index + '].day', time.day);
                formData.append('times[' + index + '].to', time.to);
                index++;
            }
            xhr = new XMLHttpRequest();
            xhr.addEventListener('load', updateFinish, false);
            xhr.open('PUT',"/api/v1/commerces/"  + $scope.currentCommerce.id);
            xhr.setRequestHeader('Accept','application/json, text/plain, */*');
            xhr.send(formData);
        } else {
            toastr.error("La razon social, categoria, direccion y horarios no pueden estar vacios.");
        }
    };

    function updateFinish(e){ 
        if(this.status === 200){
            $('#commerceForm').get(0).reset();
            toastr.success("Comercio actualizado con exito.");
            $scope.currentCommerce = {};
            $scope.imageSrc = "assets/images/uploadImage.png";
            $("#commercesModal").modal("toggle");
            $scope.actualPhone = {};
            $scope.daySelected = undefined;
            $scope.from = undefined;
            $scope.to = undefined;
            index();
        } else {
            toastr.error("No se pudo actualizar el comercio.");
        }
    }

    $scope.addCurrentTime = function(){
        if(!$scope.currentCommerce.times){
            $scope.currentCommerce.times = [];
        }
        if($scope.daySelected && $scope.from && $scope.to) {
            $scope.currentCommerce.times.push({
                "day": $scope.daySelected,
                "from": $filter('date')($scope.from, 'yyyy-MM-ddTHH:mm:ss'),
                "to": $filter('date')($scope.to, 'yyyy-MM-ddTHH:mm:ss')
            });
        }else{
            toastr.error("Seleccione un día y un horario de comienzo y fin.");
        }
    }

    $scope.deleteTime = function(index){
        $scope.currentCommerce.times.splice(index, 1);
    }

    $('#commerceForm').change(function(evt) {
        if(document.getElementById('fileInput').files.item(0)){
            $scope.fotoPreview = true;
        } else {
            $scope.fotoPreview = false;
        }
    });


    //------------------------mapa---------------------------


    var map = angular.element("#playground");

    $scope.markers = "Ciudad autonoma de Buenos Aires";

    $scope.updateMap = function (address) {
        if(!address){
            toastr.error("Complete calle y numero para ver en mapa");
        } else {
            if(!address.number || !address.street){
                toastr.error("Complete calle y numero para ver en mapa");
            } else {
                $scope.loadMapAddress(address.street, address.number);
            }
        }
    }

    $scope.loadMapAddress = (street,number) =>{
        $scope.markers = number + " " + street + " Argentina";
        $compile(map)($scope);
    };

    $scope.cleanMapAddress = () =>{
        $scope.markers = "Ciudad autonoma de Buenos Aires";
        $compile(map)($scope);
    };

    $scope.toggleSuspend = function (commerce){
        var data = {
            "suspended" : commerce.suspended
        };
        $http({
            url: "/api/v1/commerces/" + commerce.id,
            data: data,
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        }).success(function(){
            var state = (commerce.suspended) ? "habilitado" : "suspendido";
            toastr.success("Comercio " + state +" con exito.");
            indexPlates();
        }).error(function(err){
            toastr.error(err.message);
        });
    };
});
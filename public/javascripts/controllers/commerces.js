
hoyComoApp.controller('commercesCtrl', function ($scope, $http, $window, $rootScope, toastr, $filter) {
    $scope.commerces = [];
    $scope.currentCommerce = {};
    $scope.editModal = true;
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
        }
        $scope.editModal = false;
        $("#commercesModal").modal("toggle");
    };

    $scope.toggleEditModal = function(commerce) {
        $scope.editModal = true;
        $('#commerceForm').get(0).reset();
        $scope.imageSrc = "assets/images/uploadImage.png";
        angular.copy(commerce, $scope.currentCommerce);
        $("#commercesModal").modal("toggle");
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
            if($scope.actualPhone != {}) formData.append("phones[0].number", $scope.actualPhone);
            if (document.getElementById('fileInput').files.item(0)) formData.append("commerceFileName", document.getElementById('fileInput').files.item(0).name); 
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
    

    $scope.updateCommerce = function(){
        if($scope.currentCommerce.businessName){
            $scope.currentCommerce.phones = [];
            $scope.currentCommerce.phones.push($scope.actualPhone);
            $http({
                url: "/api/v1/commerces",
                data: $scope.currentCommerce,
                method: "PUT",
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            }).success(function(){
                $scope.currentCommerce = {};
                $("#commercesModal").modal("toggle");
                index();
                toastr.success("Comercio creado con exito.");
            }).error(function(err){
                toastr.error(err.message);
            });
        } else {
            toastr.error("La razon social no puede estar vacia.");
        }
    };

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
});
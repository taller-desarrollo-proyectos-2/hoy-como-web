
hoyComoApp.controller('platesCtrl', function ($scope, $http, $window, $rootScope, toastr, $filter) {
    
    $scope.plates = [];
    $scope.currentPlate = {};
    $scope.categories = [];

    indexPlates();
    indexCategories();

    function indexPlates(){
        $http({
            url: "/api/v1/plates",
            method: "GET",
            headers: {
                'authorization' : $rootScope.auth
            }
        }).success(function(data, status, headers, config){
            $scope.plates = data;
        }).error(function(err){
            toastr.error(err.message);
        });
    }

    function indexCategories(){
        $http({
            url: "/api/v1/categories",
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
        $scope.editModal = false;
        $("#platesModal").modal("toggle");
    };

    $scope.toggleCategoryCreationModal = function() {
        $scope.currentCategory = {};
        $("#platesModal").modal("toggle");
        $("#categoriesModal").modal("toggle");
    };

    $scope.backToPlatesModal = function() {
        $scope.currentCategory = {};
        $("#categoriesModal").modal("toggle");
        $("#platesModal").modal("toggle");
    };

    $scope.createCategory = function(){
        $http({
            url: "/api/v1/categories",
            data: $scope.currentCategory,
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'authorization' : $rootScope.auth
            }
        }).success(function(data){
            $scope.currentPlate.category = data;
            $scope.currentCategory = {};
            $("#categoriesModal").modal("toggle");
            $("#platesModal").modal("toggle");
            indexCategories();
            toastr.success("Categoria creado con exito.");
        }).error(function(err){
            toastr.error(err.message);
        });
    };

    $scope.createPlate = function (){
        if($scope.currentPlate.category != undefined && $scope.currentPlate.name != undefined && $scope.currentPlate.price != undefined){
            var formData = new FormData($('#plateForm').get(0)); 
            Object.keys($scope.currentPlate).forEach(function(key) {
                formData.append(key,$scope.currentPlate[key]);
            });
            if (document.getElementById('fileInput').files.item(0)) formData.append("pictureFileName", document.getElementById('fileInput').files.item(0).name); 
            xhr = new XMLHttpRequest();
            xhr.addEventListener('load', createFinish, false);
            xhr.open('POST',"/api/plates");
            xhr.setRequestHeader('Accept','application/json, text/plain, */*');
            xhr.setRequestHeader('authorization', $rootScope.auth);
            xhr.send(formData);
        } else {
            toastr.error("El nombre, la categoria y el precio no pueden estar vacios.");
        }
    };
    
    function createFinish(e)
    { 
        if(this.status === 200){
            $('#plateForm').get(0).reset();
            toastr.success("Plato creado con exito.");
            $scope.currentPlate = {};
            $("#platesModal").modal("toggle");
            indexPlates();
        } else {
            toastr.error("No se pudo crear el plato.");
        }
    }

    $scope.updatePlate = function (){
        var formData = new FormData($('#plateForm').get(0)); 
        Object.keys($scope.currentPlate).forEach(function(key) {
            formData.append(key,$scope.currentPlate[key]);
        });
        var pictureFileName = document.getElementById('fileInput').files.item(0).name; 
        formData.append("pictureFileName", pictureFileName);
        xhr = new XMLHttpRequest();
        xhr.addEventListener('load', updateFinish, false);
        xhr.open('POST',"/api/plates");
        xhr.setRequestHeader('Accept','application/json, text/plain, */*');
        xhr.setRequestHeader('authorization', $rootScope.auth);
        xhr.send(formData);
    };
    
    function updateFinish(e)
    { 
        if(this.status === 200){
            $('#plateForm').get(0).reset();
            toastr.success("Plato creado con exito.");
            $scope.currentPlate = {};
            $("#platesModal").modal("toggle");
            indexPlates();
        } else {
            toastr.error("No se pudo crear el plato.");
        }
    }


});

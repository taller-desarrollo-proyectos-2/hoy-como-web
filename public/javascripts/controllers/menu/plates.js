
hoyComoApp.controller('platesCtrl', function ($scope, $http, $window, $rootScope, toastr, $filter) {
    
    $scope.plates = [];
    $scope.currentPlate = {};
    $scope.categories = [];
    $scope.editModal = true;
    $scope.selected = {};


    indexPlates();
    indexCategories();
    indexOptionals();

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

    function indexOptionals(){
        $http({
            url: "/api/v1/optionals",
            method: "GET",
            headers: {
                'authorization' : $rootScope.auth
            }
        }).success(function(data, status, headers, config){
            $scope.optionals = data;
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
        if($scope.editModal) $scope.currentPlate = {optionals : []};
        $scope.editModal = false;
        $("#platesModal").modal("toggle");
    };

    $scope.toggleEditModal = function(plate) {
        $scope.editModal = true;
        angular.copy(plate, $scope.currentPlate);
        $("#platesModal").modal("toggle");
    };

    $scope.toggleInCreationModal = function(modal) {
        $scope.currentCategory = {};
        $("#platesModal").modal("toggle");
        $(modal).modal("toggle");
    };

    $scope.backToPlatesModal = function(modal) {
        $scope.currentCategory = {};
        $scope.currentOptional = {};
        $(modal).modal("toggle");
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
            if($scope.currentPlate.name) formData.append("name", $scope.currentPlate.name);
            if($scope.currentPlate.price) formData.append("price", $scope.currentPlate.price);
            if (document.getElementById('fileInput').files.item(0)) formData.append("pictureFileName", document.getElementById('fileInput').files.item(0).name); 
            if($scope.currentPlate.category) formData.append("category.id", $scope.currentPlate.category.id);
            if($scope.currentPlate.optionals) formData.append("optionals.id", [1,2]);
            var index = 0;
            for (var opt of $scope.currentPlate.optionals) {
                formData.append('optionals[' + index + '].id', opt.id);
                index++;
            }
            xhr = new XMLHttpRequest();
            xhr.addEventListener('load', createFinish, false);
            xhr.open('POST',"/api/v1/plates");
            xhr.setRequestHeader('Accept','application/json, text/plain, */*');
            xhr.setRequestHeader('authorization', $rootScope.auth);
            xhr.send(formData);
        } else {
            toastr.error("El nombre, la categoria y el precio no pueden estar vacios.");
        }
    };
    
    function createFinish(e){ 
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
        if($scope.currentPlate.category != undefined && $scope.currentPlate.name != undefined && $scope.currentPlate.price != undefined){
            var formData = new FormData($('#plateForm').get(0)); 
            if($scope.currentPlate.name) formData.append("name", $scope.currentPlate.name);
            if($scope.currentPlate.price) formData.append("price", $scope.currentPlate.price);
            if (document.getElementById('fileInput').files.item(0)) formData.append("pictureFileName", document.getElementById('fileInput').files.item(0).name); 
            if($scope.currentPlate.category) formData.append("category.id", $scope.currentPlate.category.id);
            if($scope.currentPlate.optionals) formData.append("optionals.id", [1,2]);
            var index = 0;
            for (var opt of $scope.currentPlate.optionals) {
                formData.append('optionals[' + index + '].id', opt.id);
                index++;
            }
            xhr = new XMLHttpRequest();
            xhr.addEventListener('load', updateFinished, false);
            xhr.open('PUT',"/api/v1/plates/" + $scope.currentPlate.id);
            xhr.setRequestHeader('Accept','application/json, text/plain, */*');
            xhr.setRequestHeader('authorization', $rootScope.auth);
            xhr.send(formData);
        } else {
            toastr.error("El nombre, la categoria y el precio no pueden estar vacios.");
        }
    };
    
    function updateFinished(e){ 
        if(this.status === 200){
            $('#plateForm').get(0).reset();
            toastr.success("Plato actualizado con exito.");
            $scope.currentPlate = {};
            $("#platesModal").modal("toggle");
            indexPlates();
        } else {
            toastr.error("No se pudo actualizar el plato.");
        }
    }

    $scope.addOptional = function (optional){
        if(!optionalInList(optional)){
            $scope.currentPlate.optionals.push(optional);
        } else {
            toastr.error("El opcional ya se encuentra agregado.");
        }
        $scope.selected.optional = undefined;
    };

    $scope.removeOptional = function (index){
        $scope.currentPlate.optionals.splice(index, 1);
    };

    function optionalInList(optional){
        for(var option of $scope.currentPlate.optionals){
            if(option.id == optional.id) return true;
        }
        return false;
    }

    $scope.deletePlate = function(plate){
        $http({
            url: "/api/v1/plates/" + plate.id,
            method: "DELETE",
            headers: {
                'authorization' : $rootScope.auth
            }
        }).success(function(){
            index();
            toastr.success("Plato eliminado con exito.");
        }).error(function(err){
            toastr.error(err.message);
        });
    };

});

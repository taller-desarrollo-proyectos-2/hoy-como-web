
hoyComoApp.controller('platesCtrl', function ($scope, $http, $window, $rootScope, toastr, $filter, fileReader) {
    
    $scope.plates = [];
    $scope.currentPlate = {};
    $scope.categories = [];
    $scope.editModal = true;
    $scope.selected = {};
    $scope.imageSrc = "assets/images/uploadImage.png";
    $scope.fotoPreview = false;

    $scope.discountOptions = [{value: 5, show: "5%"},{value:10, show: "10%"}];

    indexPlates();
    indexCategories();
    indexOptionals();

    function indexPlates(){
        $http({
            url: "/api/v1/plates?deletedAt=null",
            method: "GET",
            headers: {
                'authorization' : $rootScope.auth
            }
        }).success(function(data, status, headers, config){
            $scope.plates = data;
            $scope.showingPlates = data;
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
        if($scope.editModal){
            $('#plateForm').get(0).reset();
            $scope.imageSrc = "assets/images/uploadImage.png";
            $scope.currentPlate = {optionals : [], glutenFree: false};
            $scope.fotoPreview = false;
        }
        $scope.editModal = false;
        $("#platesModal").modal("toggle");
    };

    $scope.toggleEditModal = function(plate) {
        $('#plateForm').get(0).reset();
        $scope.fotoPreview = false;
        $scope.imageSrc = "assets/images/uploadImage.png";
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

    $scope.createOptional = function(){
        $http({
            url: "/api/v1/optionals",
            data: $scope.currentOptional,
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'authorization' : $rootScope.auth
            }
        }).success(function(data){
            $scope.addOptional(data);
            $("#optionalsModal").modal("toggle");
            $("#platesModal").modal("toggle");
            indexOptionals();
            toastr.success("Opcional creado con exito.");
        }).error(function(err){
            toastr.error(err.message);
        });
    };

    $scope.createPlate = function (){
        if($scope.currentPlate.category != undefined && $scope.currentPlate.name != undefined && $scope.currentPlate.price != undefined){
            var formData = new FormData($('#plateForm').get(0)); 
            if($scope.currentPlate.name) formData.append("name", $scope.currentPlate.name);
            if($scope.currentPlate.description) formData.append("description", $scope.currentPlate.description);
            formData.append("glutenFree", $scope.currentPlate.glutenFree);
            formData.append("active", true);
            if($scope.currentPlate.price) formData.append("price", $scope.currentPlate.price);
            if (document.getElementById('fileInput').files.item(0)) formData.append("pictureFileName", document.getElementById('fileInput').files.item(0).name); 
            if($scope.currentPlate.category) formData.append("category.id", $scope.currentPlate.category.id);
            var index = 0;
            for (var opt of $scope.currentPlate.optionals) {
                formData.append('optionals[' + index + '].id', opt.id);
                index++;
            }
            xhr = new XMLHttpRequest();
            xhr.addEventListener('load', createFinish, false);
            xhr.open('POST',"/api/v1/plates");
            xhr.setRequestHeader('Accept','application/json, text/plain, */*');
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
            $scope.imageSrc = "assets/images/uploadImage.png";
            $("#platesModal").modal("toggle");
            indexPlates();
        } else {
            toastr.error("No se pudo crear el plato.");
        }
    }
    
    $('#plateForm').change(function(evt) {
        if(document.getElementById('fileInput').files.item(0)){
            $scope.fotoPreview = true;
        } else {
            $scope.fotoPreview = false;
        }
    });

    $scope.updatePlate = function (){
        if($scope.currentPlate.category != undefined && $scope.currentPlate.name != undefined && $scope.currentPlate.price != undefined){
            var formData = new FormData($('#plateForm').get(0)); 
            if($scope.currentPlate.name) formData.append("name", $scope.currentPlate.name);
            formData.append("glutenFree", $scope.currentPlate.glutenFree);
            formData.append("active", $scope.currentPlate.active);
            if($scope.currentPlate.description) formData.append("description", $scope.currentPlate.description);
            if($scope.currentPlate.price) formData.append("price", $scope.currentPlate.price);
            if (document.getElementById('fileInput').files.item(0)) formData.append("pictureFileName", document.getElementById('fileInput').files.item(0).name); 
            if($scope.currentPlate.category) formData.append("category.id", $scope.currentPlate.category.id);
            var index = 0;
            for (var opt of $scope.currentPlate.optionals) {
                formData.append('optionals[' + index + '].id', opt.id);
                index++;
            }
            formData.append("onPromotion", $scope.currentPlate.onPromotion);
            if($scope.currentPlate.onPromotion){
                formData.append("discount", $scope.currentPlate.discount.value);
            } else {
                formData.append("discount", 0);
            }

            xhr = new XMLHttpRequest();
            xhr.addEventListener('load', updateFinished, false);
            xhr.open('PUT',"/api/v1/plates/" + $scope.currentPlate.id);
            xhr.setRequestHeader('Accept','application/json, text/plain, */*');
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

    $scope.updatePlateActive = function (plate){
        var data = {
            "active" : plate.active
        };
        $http({
            url: "/api/v1/plates/" + plate.id,
            data: data,
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        }).success(function(){
            var state = (plate.active) ? "" : "des";
            toastr.success("Plato " + state +"activado con exito.");
            indexPlates();
        }).error(function(err){
            toastr.error(err.message);
        });
    };

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
            indexPlates();
            toastr.success("Plato eliminado con exito.");
        }).error(function(err){
            toastr.error(err.message);
        });
    };

    $scope.search = "";
	
	$scope.filterFeed = function (){
		if ($scope.search === ""){
			$scope.showingPlates = $scope.plates;			
		} else {
			var auxList = [];
			for(var plate of $scope.plates){
				if(plate.name.toUpperCase().includes($scope.search.toUpperCase())){
					auxList.push(plate);
				}
			}
			$scope.showingPlates = auxList;
		}
	};

});

hoyComoApp.directive("ngFileSelect", function(fileReader, $timeout) {
    return {
      scope: {
        ngModel: '='
      },
      link: function($scope, el) {
        function getFile(file) {
          fileReader.readAsDataUrl(file, $scope)
            .then(function(result) {
              $timeout(function() {
                $scope.ngModel = result;
              });
            }).catch(function(err){
                $scope.ngModel = "assets/images/uploadImage.png";
            });
        }

        el.bind("change", function(e) {
          var file = (e.srcElement || e.target).files[0];
          getFile(file);
        });
      }
    };
  });

  hoyComoApp.factory("fileReader", function($q, $log) {
    var onLoad = function(reader, deferred, scope) {
      return function() {
        scope.$apply(function() {
          deferred.resolve(reader.result);
        });
      };
    };
  
    var onError = function(reader, deferred, scope) {
      return function() {
        scope.$apply(function() {
          deferred.reject(reader.result);
        });
      };
    };
  
    var onProgress = function(reader, scope) {
      return function(event) {
        scope.$broadcast("fileProgress", {
          total: event.total,
          loaded: event.loaded
        });
      };
    };
  
    var getReader = function(deferred, scope) {
      var reader = new FileReader();
      reader.onload = onLoad(reader, deferred, scope);
      reader.onerror = onError(reader, deferred, scope);
      reader.onprogress = onProgress(reader, scope);
      return reader;
    };
  
    var readAsDataURL = function(file, scope) {
      var deferred = $q.defer();
  
      var reader = getReader(deferred, scope);
      if(file) {
        reader.readAsDataURL(file);
      } else{
        return $q.reject();
      }
      return deferred.promise;
    };
  
    return {
      readAsDataUrl: readAsDataURL
    };
  });
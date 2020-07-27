var app = angular.module('registerApp', []);
app.controller('register', function($scope, $http) {

	$scope.createUser = function() {
		var data = {
			username : $scope.username,
			email : $scope.email,
			password : $scope.password,
			mobile : $scope.mobile
		};
		console.log(data);

		$http({
			method : "POST",
			url : "/user/create",
			dataType : 'json',
			data : data,
			headers : {
				'Content-Type' : 'application/json;charset=utf-8;'
			}
		}).then(function(response) {
			console.log(response.data);
			$scope.users = response.data;
			window.location = '/login';
		}, function myError(response) {
			$scope.error = response.data;
			console.log(response.data);

		});
	}

});
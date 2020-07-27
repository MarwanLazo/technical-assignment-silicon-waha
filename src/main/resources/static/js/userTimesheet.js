var app = angular.module('userTimesheetApp', []);
app.controller('userTimesheet', function($scope, $http) {
		
	$http({
		method : "GET",
		url : "/user/loginEmail"
	}).then(function mySuccess(response) {
		$scope.email = response.data.email;
		userTimesheetByUserEmail($scope.email);

	}, function myError(response) {
		console.log(response.data);
	});

	$scope.logUserTimesheet = function() {
	
		var data = {};
		if ($scope.logInTime != null && $scope.logOutTime != null) {
			data = {
				logInTime : new Date($scope.logInTime),
				logOutTime : new Date($scope.logOutTime),
			};
		}

		console.log(data);
		

		var action = $http({
			method : 'POST',
			url : "/timesheet/create/" + $scope.email,
			headers : {
				"Content-Type" : "application/json;charset=UTF-8"
			},
			data : data
		}).then(function mySuccess(response) {

			userTimesheetByUserEmail($scope.email);
			$scope.error = null;
			$scope.logOutTime = null;
			$scope.logInTime = null;
		}, function myError(response) {
			console.log(response.data);
			$scope.error = response.data;

		});
	};

	userTimesheetByUserEmail = function(email) {
		$http({
			method : "GET",
			url : "/timesheet/" + email
		}).then(function mySuccess(response) {
			console.log(response.data);
			$scope.logHistoryArr = response.data;

		}, function myError(response) {
			console.log(response.data);
		});

	}

});

const formatDate = function(date_o) {

	var date_ob = new Date(date_o);

	var day = ("0" + date_ob.getDate()).slice(-2);
	var month = ("0" + (date_ob.getMonth() + 1)).slice(-2);
	var year = date_ob.getFullYear();
	var hours = date_ob.getHours() + '';
	var minutes = date_ob.getMinutes() + '';

	if (month.length < 2)
		month = '0' + month;
	if (day.length < 2)
		day = '0' + day;

	if (hours.length < 2)
		hours = '0' + hours;
	if (minutes.length < 2)
		minutes = '0' + minutes;

	return day + "." + month + "." + year + " " + hours + ":" + minutes;
}
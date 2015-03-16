var scheduleModule = angular.module('scheduleModule', []);

scheduleModule.controller('ScheduleCtrl', function($scope, $http){

	$scope.scheduleId = '';
	
	$scope.schedule = {};
	
	$scope.fetchSchedule = function() {
		$http.get('http://localhost:8080/schedule/' + $scope.scheduleId).
			success(function(fechedData) {
				$scope.scheduleJson = fechedData;
			});
		};

	$scope.saveHtml = function() {
		$http({
            method : 'POST',
            url : 'http://localhost:8080/schedule/save',
            data : $scope.schedule
        })
			.success(function(data, status, headers, config) {
				alert("poszło");
			})
			.error(function(data, status, headers, config) {
				alert("nie przeszło !");
			});
	};

});

var scheduleModule = angular.module('scheduleModule', []);

scheduleModule.controller('ScheduleCtrl', function($scope, $http) {

	$scope.scheduleId = '';
	$scope.secondScheduleId = '';

	$scope.schedule = {};

	$scope.alert = false;

	$scope.alertContent = "";

	$scope.fetchSchedule = function() {
		$http.get('http://localhost:8080/schedule/' + $scope.scheduleId)
				.success(function(fechedData) {
					$scope.scheduleJson = fechedData;

					fillScheduleTable(fechedData);

				});
	};

	$scope.saveHtml = function() {
		$http({
			method : 'POST',
			url : 'http://localhost:8080/schedule/save',
			data : $scope.schedule
		}).success(function(data, status, headers, config) {
			$scope.alert = true;
			$scope.alertContent = "Zapisano plan";
		}).error(function(data, status, headers, config) {
			$scope.alertErr = true;
			$scope.alertContent = "Nie zapisano planu";
		});
	};

	$scope.compareSchedules = function() {
		$http.get(
				'http://localhost:8080/schedule/compare/' + $scope.scheduleId
						+ '/' + $scope.secondScheduleId).success(
				function(fechedData) {
					$scope.scheduleJson = fechedData;
					fillScheduleTable(fechedData);
				});
	};

	var fillScheduleTable = function(fechedData) {
		for (subject in fechedData) {
			var fetchedDetails = fechedData[subject].details;

			var hour = fetchedDetails.start.replace(":", "_");
			var day = fetchedDetails.dayOfWeek.substring(0, 2);
			var type = fechedData[subject].type;
			// do poprawienia (kodowanie na utf8 w backendzie)
			if (day === "śr") {
				$scope["sr" + hour] = fechedData[subject].name + '\n'
						+ fechedData[subject].lector;
				$scope["sr" + hour + "class"] = type.substring(0, 2);
				;
			} else {
				$scope[day + hour] = fechedData[subject].name + '\n'
						+ fechedData[subject].lector;
				$scope[day + hour + "class"] = type.substring(0, 2);
				;
			}
		};
	};

	
	
});

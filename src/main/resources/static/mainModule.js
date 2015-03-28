var scheduleModule = angular.module('scheduleModule', ['ui.bootstrap']);


scheduleModule.config([ '$httpProvider', function($httpProvider) {
	$httpProvider.defaults.useXDomain = true;
	delete $httpProvider.defaults.headers.common['X-Requested-With'];
} ]);

scheduleModule.controller('ScheduleCtrl', function($scope, $http) {

//	var url = 'http://planpwr.unicloud.pl/schedule/';
	var url = 'http://localhost:8080/schedule/';
	
	$scope.scheduleId = '';
	$scope.secondScheduleId = '';

	$scope.schedule = {};

	$scope.alert = false;

	$scope.alertContent = "";
	$scope.fetchSchedule = function() {
		$http.get(url + $scope.scheduleId)
				.success(function(fechedData) {
					$scope.scheduleJson = fechedData;

					fillScheduleTable(fechedData);

				});
	};

	$scope.saveHtml = function() {
		$http({
			method : 'POST',
			url : url + '/save',
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
				url + '/compare/' + $scope.scheduleId
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

			if (day === "śr") {
				$scope["sr" + hour]  = {};
				
				$scope["sr" + hour].name = fechedData[subject].name;
				$scope["sr" + hour].lector = fechedData[subject].lector;
				$scope["sr" + hour].details = {};
				
				$scope["sr" + hour].details.dayOfWeek = fechedData[subject].details.dayOfWeek;
				$scope["sr" + hour].details.building = fechedData[subject].details.building;
				$scope["sr" + hour].details.room = fechedData[subject].details.room;

				$scope["sr" + hour + "class"] = type.substring(0, 2);
			} else {
				$scope[day + hour] = {};
				
				$scope[day + hour].name = fechedData[subject].name;
				$scope[day + hour].lector = fechedData[subject].lector;
				$scope[day + hour].details = {};
				
				$scope[day + hour].details.dayOfWeek = fechedData[subject].details.dayOfWeek;
				$scope[day + hour].details.building = fechedData[subject].details.building;
				$scope[day + hour].details.room = fechedData[subject].details.room;

				$scope[day + hour + "class"] = type.substring(0, 2);
			}
		};
	};
});

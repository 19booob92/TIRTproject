var scheduleModule = angular.module('scheduleModule', []);

var urlBase = "http://localhost:8080/";

scheduleModule.controller('ScheduleCtrl', function($scope, $http){

	$scope.indexNo = "";
	#scope.html = "";

	$scope.fetchSchedule = function() {
		$http.get(urlBase + 'schedule/194225').success(function(fechedData) {
			$scope.scheduleJson = fechedData;
		});
	};

	$scope.saveHtml = function() {
		$http ({
		    url: 'urlBase' + schedule/save,
		    method: "POST",
		    data: { 'indexNo' : indexNo, 'html' : html };
		});
	.then(function(response) {
        	// success
	}, 
	    function(response) { // optional
        	// failed
	});
	};
});

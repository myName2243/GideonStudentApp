//App for studentList and chart displays
var app = angular.module('studentApp', []);

//Controller used for StudentList.html: mainly to display students who have data for selection
app.controller('studentCtrl', function($scope, $http, $window) {
	//Retrieves students with data
	$http.get("http://localhost:8080/dataStudents")
	.then(function(response) {
		$scope.students = response.data;
	});

	//Function for selecting a student and going to the chart page
	$scope.logStudent = function(id, name) {
		$window.localStorage.setItem(0, id);
		$window.localStorage.setItem(1, name);
		window.location.href = "lineChart.html"
	}
});

//Controller used for lineChart.html: responsible for creating and displaying students' graphs
app.controller('chartCtrl', function($scope, $http, $window) {

		$scope.studentName = $window.localStorage.getItem(1);

		//Retrieves all categories the selected student is working in
		$http.get("http://localhost:8080/categoriesByStudent?Id=" + $window.localStorage.getItem(0))
		.then(function(response) {
			$scope.categoriesOfStudent = response.data;
			console.log($scope.categoriesOfStudent);
		});

		//Retrieves possible repetition selection options for the selected category
		$scope.getReps = function() {
			if($scope.selectedCategory == "Calculation") {
				console.log("Calculation is selected");
				$scope.repOptions = ["1", "2", "3", "4", "5", "All"];
			} else {
				console.log("Something besides Calculation is selected");
				$scope.repOptions = ["1", "2", "All"]
			}
		}

		//Generates the lineChart based on instructor specifications
		$scope.generateChart = function() {
				console.log($scope.selectedRep);
				var selectedStudentId = $window.localStorage.getItem(0);
				var b = document.getElementById("months").value;
		$http.get("http://localhost:8080/recordsById?StudentId=" + selectedStudentId + "&Category=" + $scope.selectedCategory + "&Months=" + b + "&Reps=" + $scope.selectedRep)	
		.then(function(response) {
			console.log(response.data);
			$scope.records = response.data;
			$scope.first = $scope.records[0];

			let myChart = document.getElementById('lineChart').getContext('2d');

			var dates = [];
			var values = [];
			var books = [];

			//Helps display error message if there is no data
			var a = 0;

			for(i = 0; i < $scope.records.length; i++) {
				if($scope.records[i].startDate != null) {
					var d = new Date($scope.records[i].startDate);
					//dates.push((d.getMonth()+1) + "/" + d.getDate() + "/" + d.getFullYear());
					var displayed = (d.getMonth()+1) + "/" + d.getDate() + "/" + d.getFullYear();
					//dates.push(displayed);
					console.log(moment(displayed).format('MMM DD YYYY'));
					dates.push(moment(displayed).format('MMM YYYY'));
					//dates.push($scope.records[i].startDate);
					values.push($scope.records[i].sequenceLarge);
					books.push($scope.records[i].bookTitle);
					a++;
				}
			}

			$scope.errorMessage = true;
			if(a > 0) {
				$scope.errorMessage = false;
			} 	

			//Chart.defaults.global.defaultFontFamily ='Lato';
			Chart.defaults.global.defaultFontSize = 18;
			Chart.defaults.global.defaultFontColor = '#000';

			let exampleChart = new Chart(myChart,{
				type: 'line',
				data:{
					xLabels: dates,
					yLabels: books,
					datasets:[{
						label: $scope.first.name,
						data: values,
						backgroundColor: "rgba(255, 0, 0, 0.4)",
						borderColor: "rgba(255, 0, 0, 0.4)",
						fill: false,
						lineTension: 0
					}, {
						label: 'asdfasd',
						//data: [2, 29, 5,7, 2, 3, 10, 8, 7, 4, 2, 8],
						backgroundColor: "rgba(255, 153, 0, 0.7)",
						fill: false,
						lineTension: 0
					}
						
					]
				},
				options: {
					title: {
						display: true,
						text: $scope.first.name,
						fontSize: 25
					},
					legend: {
						position: 'right'
					},
					layout: {
						padding: {
							left: 10,
							bottom: 5
						}
					},
					scales: {
						xAxes: [{
							ticks: {
								unit: 'month',
								unitStepSize: 6
							}
						}]
					}
				}
			});
		});	
		};			
	});

//App for inserting data through insertRecord.html
var app2 = angular.module('insertApp', ['ngMaterial']);

	app2.controller('insertCtrl', function($scope, $http){

	//Returns a list of all students for easy name selection	
	$http.get("http://localhost:8080/students")
	.then(function(response) {
		$scope.students = response.data;

		$scope.names = [];
		for(i = 0; i < $scope.students.length; i++) {
			$scope.names.push($scope.students[i].client);
		}

	});

	//Returns a list of subcategories based on the selected category
	$scope.getSubcategories = function() {
		$http.get("http://localhost:8080/subcategories?Category=" + $scope.selectedCategory)
		.then(function(response) {
			console.log(response.data);
			$scope.subcategories = response.data;
		});
	}

	//Returns a list of titles based on the selected subCategory
	$scope.getTitles = function() {
		$http.get("http://localhost:8080/titles?Subcategory=" + $scope.selectedSubCategory)
		.then(function(response) {
			$scope.titles = response.data;
		});
	}

	//Returns all books
	$http.get("http://localhost:8080/books")
	.then(function(response) {
		$scope.books = response.data;
		$scope.categories = [];

		for(i = 0; i < $scope.books.length; i++) {
			if($scope.categories.indexOf($scope.books[i].category) < 0) {
				$scope.categories.push($scope.books[i].category);
			}
		}

		//$http.defaults.headers.post["Content-Type"] = "application/json";
		//Creates JSON for the record based on form data
		$scope.createRecord = function() {
			var newRecordDetails = JSON.stringify({
				client: $scope.Client,
				category: $scope.selectedCategory,
				subcategory: $scope.selectedSubCategory,
				title: $scope.selectedTitle,
				startDate: $scope.startDate,
				testTime: $scope.testTime,
				mistakes: $scope.mistakes,
				rep: $scope.rep
			});
			//Inserts the record with an HTTP post call
			$http({
				url: 'http://localhost:8080/addRecord',
				method: 'POST',
				headers: {
					"Content-Type": "application/json",
					"Accept": "application/json"
				},
				data:newRecordDetails
			}).then(function(response) {
				alert(response.data);
			});
		}

	});
});

//App for updating recordData through updateRecord.html
var app3 = angular.module('updateApp', []);
			
app3.controller('updateCtrl', function($scope, $http){

	//Returns all student names for easy selection
	$http.get("http://localhost:8080/students")
	.then(function(response) {
		$scope.students = response.data;
		$scope.names = [];
		for(i = 0; i < $scope.students.length; i++) {
			$scope.names.push($scope.students[i].client);
		}
	});

	//Retrieves incomplete records for instructors to choose from
	$http.get("http://localhost:8080/incompleteRecords")
	.then(function(response) {
		$scope.records = response.data;
		$scope.displayRecords = [];
		for(i = 0; i < $scope.records.length; i++) {
			var splitDate = $scope.records[i].startDate.split('-');

			var year = splitDate[0];
			var month = splitDate[1];
			var day = splitDate[2];

			//Formats date for readability
			var formattedDate = month + "/" + day + "/" + year;

			$scope.displayRecords.push($scope.records[i].name + ", started book " + $scope.records[i].bookTitle + " on " + formattedDate + " | RecordId: " + $scope.records[i].recordId);
		}
	});

	//Updates an incomplete record based on instructor data
	$scope.updateRecord = function() {
		console.log($scope.endDate);
		$http.get("http://localhost:8080/updateRecord?record=" + $scope.selectedRecord + "&endDate=" + $scope.endDate)
		.then(function(response) {
			window.location.href = "StudentList.html"
		})	
	}
});
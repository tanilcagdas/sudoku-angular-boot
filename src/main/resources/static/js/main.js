

var RED = "red";
var BLUE = "blue";

var stompClient = null;

//function setConnected(connected) {
//	$("#connect").prop("disabled", connected);
//	$("#disconnect").prop("disabled", !connected);
//	if (connected) {
//		$("#conversation").show();
//	} else {
//		$("#conversation").hide();
//	}
//	$("#greetings").html("");
//}



var headers = {
	"Access-Control-Allow-Origin" : "*",
	'Access-Control-Allow-Credentials' : true
};

var baseurl = document.URL.replace('8090', '8080');
if(baseurl.includes('#')){
		baseurl = baseurl.substring(0, baseurl.indexOf('#'))
	}


/**
 * Main AngularJS Web Application
 */
var app = angular.module('sudokuWebApp', [ 'ngRoute' ]);

/**
 * Configure the Routes
 */
app.config([ '$routeProvider', function($routeProvider) {
	$routeProvider
	// Home
	.when("/", {
		templateUrl : "partials/solver.html",
		controller : "sudokuCtrl"
	})
	// Pages
	.when("/game", {
		templateUrl : "partials/game.html",
		controller : "gameCtrl"
	}).when("/solver", {
		templateUrl : "partials/solver.html",
		controller : "sudokuCtrl"
	}).when("/lobby", {
      		templateUrl : "partials/loby	.html",
      		controller : "lobyCtrl"
      	})

	// Blog

	// else 404
	.otherwise("/404", {
		templateUrl : "partials/404.html",
		controller : "sudokuCtrl"
	});
} ]);

app.controller('lobyCtrl', [
		'$rootScope',
		'$scope',
		'SudokuService',
		'SudokuCreatorService',
		function($rootScope, $scope, SudokuService, SudokuCreatorService) {
			console.log("loby Controller");

			$rootScope.user = {
				name : ''
			}

			$rootScope.connectedUsers = [];

			$rootScope.disconnect = function() {
				if (stompClient != null) {
					stompClient.disconnect();
				}
				setConnected(false);
				console.log("Disconnected");
			}

			setConnected(false)

			function setConnected(connected) {
				$("#connect").prop("disabled", connected);
				$("#disconnect").prop("disabled", !connected);
				$("#start").prop("disabled", !connected);
				if (connected) {
					$("#conversation").show();
				} else {
					$("#conversation").hide();
				}
				$("#greetings").html("");
			}

			$rootScope.connect = function() {
				var socket = new SockJS(baseurl + 'websocket');
				stompClient = Stomp.over(socket);
				stompClient.connect({}, function(frame) {
					setConnected(true);
					console.log('Connected: ' + frame);
					stompClient.subscribe('/topic/moves', function(cellBody) {
						SudokuService.parseCell(cellBody.body);
					});
					stompClient.subscribe('/topic/connectedusers', function(
							cellBody) {
						parseConnectedUsers(cellBody.body);
					});
					console.log($rootScope.user.name)
					var username = $("#name").val();
					stompClient.send("/app/join", {}, JSON.stringify({
						'username' : username
					}));
				});
			}

			function parseConnectedUsers(cellBody) {
				var body = JSON.parse(cellBody)
				$rootScope.connectedUsers.push(body.username);
				$rootScope.$apply()
			}

		} ]);
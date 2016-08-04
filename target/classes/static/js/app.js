(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
'use strict';

Object.defineProperty(exports, "__esModule", {
	value: true
});

var _restSrv = require('./services/rest.srv.js');

var _restSrv2 = _interopRequireDefault(_restSrv);

var _scanSplashDir = require('./directives/scanSplash.dir.js');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

// Providers & Services
exports.default = angular.module('Common', []).config(["$stateProvider", function ($stateProvider) {}]).service('RestSrv', _restSrv2.default).component('scanSplash', _scanSplashDir.scanSplash);

// Directives

},{"./directives/scanSplash.dir.js":2,"./services/rest.srv.js":3}],2:[function(require,module,exports){
'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var scanSplash = exports.scanSplash = {
  template: '\n      <div class="splash" ng-if="splashCtrl.status">\n      <section class=\'pulse\'>\n        <img src=\'images/gettAroom.png\'/>\n        <div class=\'pulse1\'></div>\n        <div class=\'pulse2\'></div>\n      </section>\n      <div class="geet-a-room-splash-logo"></div>\n      <h2>{{:: splashCtrl.massage}}</h2>\n    </div>\n  ',
  bindings: {
    status: '<',
    massage: '@'
  },
  controllerAs: 'splashCtrl',
  controller:
  /* @ngInject */
  ["$log", "RestSrv", function Ctrl($log, RestSrv) {
    _classCallCheck(this, Ctrl);

    var splashCtrl = this;
    splashCtrl.$log = $log;
    splashCtrl.RestSrv = RestSrv;

    splashCtrl.$onInit = function () {};
  }]
};

},{}],3:[function(require,module,exports){
'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/* @ngInject */
var RestSrv = function () {
  /* @ngInject */
  RestSrv.$inject = ["$http", "$q", "CONFIG"];
  function RestSrv($http, $q, CONFIG) {
    _classCallCheck(this, RestSrv);

    // Object.assign(this, { $http, $q, CONFIG });
    this.$http = $http;
    this.$q = $q;
    this.CONFIG = CONFIG;
  }

  _createClass(RestSrv, [{
    key: 'getRoom',
    value: function getRoom(id) {
      return this.$http.get(this.CONFIG.server.url + '/api/rooms/' + id).then(function (response) {
        return response.data;
      }).catch(this.responseError);
    }
  }, {
    key: 'bookRoom',
    value: function bookRoom(id, fromDate, toDate) {
      var req = {
        method: 'POST',
        url: this.CONFIG.server.url + '/api/actions/reserve/' + id + '?from=' + fromDate + '&to=' + toDate,
        headers: {
          'Content-Type': 'application/json'
        },
        data: {
          from: fromDate,
          to: toDate
        }
      };

      return this.$http(req).then(function (response) {
        return response;
      }).catch(this.responseError);
    }
  }, {
    key: 'responseError',
    value: function responseError(error) {
      console.log(error);
    }
  }]);

  return RestSrv;
}();

exports.default = RestSrv;

},{}],4:[function(require,module,exports){
"use strict";

Object.defineProperty(exports, "__esModule", {
	value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/* @ngInject */
var NotAvailableCtrl = function () {
	/* @ngInject */
	NotAvailableCtrl.$inject = ["$scope", "$log", "RestSrv", "$stateParams", "moment", "$mdDialog", "$mdMedia"];
	function NotAvailableCtrl($scope, $log, RestSrv, $stateParams, moment, $mdDialog, $mdMedia) {
		_classCallCheck(this, NotAvailableCtrl);

		this.$scope = $scope;
		this.$log = $log;
		this.RestSrv = RestSrv;
		this.$stateParams = $stateParams;
		this.moment = moment;
		this.splashStatus = true;
		this.$mdDialog = $mdDialog;
		this.$mdMedia = $mdMedia;
		this.room = {
			id: "",
			name: "",
			steps: 15,
			nextMeeting: {},
			available: true,
			fallbacks: []
		};

		this.init();
	}

	_createClass(NotAvailableCtrl, [{
		key: "init",
		value: function init() {
			if (this.$stateParams.room) {
				this.splashStatus = false;
				this.room = this.$stateParams.room;
				this.until = this.moment(this.room.nextMeeting.to).format('LT');
			}
		}
	}, {
		key: "nextMeetingInfo",
		value: function nextMeetingInfo(ev) {
			var useFullScreen = this.$mdMedia('xs');
			this.$mdDialog.show({
				controller: function () {
					/* @ngInject */
					Ctrl.$inject = ["$mdDialog"];
					function Ctrl($mdDialog) {
						_classCallCheck(this, Ctrl);

						var infoCtrl = this;
						infoCtrl.$mdDialog = $mdDialog;
					}

					_createClass(Ctrl, [{
						key: "hide",
						value: function hide() {
							this.$mdDialog.hide();
						}
					}, {
						key: "cancel",
						value: function cancel() {
							this.$mdDialog.cancel();
						}
					}, {
						key: "answer",
						value: function answer(_answer) {
							this.$mdDialog.hide(_answer);
						}
					}]);

					return Ctrl;
				}(),
				locals: this.room,
				bindToController: true,
				controllerAs: 'infoCtrl',
				templateUrl: 'rooms/views/nextMeetingInfo.tpl.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose: true,
				fullscreen: useFullScreen
			}).then(function (answer) {});
		}
	}, {
		key: "havFallbacks",
		value: function havFallbacks() {
			return this.room.fallbacks.length > 0;
		}
	}, {
		key: "fallbacks",
		value: function fallbacks(ev) {
			this.$mdDialog.show({
				controller: function () {
					/* @ngInject */
					Ctrl.$inject = ["$mdDialog"];
					function Ctrl($mdDialog) {
						_classCallCheck(this, Ctrl);

						var fallbackCtrl = this;
						fallbackCtrl.$mdDialog = $mdDialog;
					}

					_createClass(Ctrl, [{
						key: "hide",
						value: function hide() {
							this.$mdDialog.hide();
						}
					}, {
						key: "cancel",
						value: function cancel() {
							this.$mdDialog.cancel();
						}
					}, {
						key: "answer",
						value: function answer(_answer2) {
							this.$mdDialog.hide(_answer2);
						}
					}]);

					return Ctrl;
				}(),
				locals: this.room,
				bindToController: true,
				controllerAs: 'fallbackCtrl',
				templateUrl: 'notavailable/views/fallbacksModal.tpl.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose: true,
				fullscreen: true
			}).then(function (answer) {});
		}
	}]);

	return NotAvailableCtrl;
}();

exports.default = NotAvailableCtrl;

},{}],5:[function(require,module,exports){
'use strict';

Object.defineProperty(exports, "__esModule", {
	value: true
});

var _notavailableCtrl = require('./controllers/notavailable.ctrl.js');

var _notavailableCtrl2 = _interopRequireDefault(_notavailableCtrl);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

// Directives

exports.default = angular.module('Notavailable', []).config(["$stateProvider", function ($stateProvider) {

	$stateProvider.state('notavailable', {
		url: '/notavailable',
		templateUrl: 'notavailable/views/notavailable.state.tpl.html',
		controller: 'NotAvailableCtrl as notavCtrl',
		params: {
			room: null
		}
	});
}]).controller('NotAvailableCtrl', _notavailableCtrl2.default); // Providers & Services

// Controllers

},{"./controllers/notavailable.ctrl.js":4}],6:[function(require,module,exports){
"use strict";

Object.defineProperty(exports, "__esModule", {
	value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/* @ngInject */
var RoomsCtrl = function () {
	/* @ngInject */
	RoomsCtrl.$inject = ["$scope", "$log", "RestSrv", "$stateParams", "moment", "$timeout", "$mdDialog", "$state", "$mdMedia"];
	function RoomsCtrl($scope, $log, RestSrv, $stateParams, moment, $timeout, $mdDialog, $state, $mdMedia) {
		_classCallCheck(this, RoomsCtrl);

		this.$scope = $scope;
		this.$log = $log;
		this.RestSrv = RestSrv;
		this.$timeout = $timeout;
		this.$stateParams = $stateParams;
		this.$mdDialog = $mdDialog;
		this.$state = $state;
		this.$mdMedia = $mdMedia;
		this.moment = moment;

		this.roomId = '';
		this.splashStatus = true;
		this.room = {
			id: "",
			name: "",
			steps: 15,
			nextMeeting: {},
			available: true
		};

		this.nextMeeting = {};
		this.currentTime = this.moment();
		this.currentTimeLTS = this.moment().format('LT');
		this.currentToTimeLTS = this.moment();

		this.total = '';
		this.nextMeetingFrom = '';
		this.nextMettingTo = '';
		this.steps = 5;

		this.min = 0;
		this.max = 0;
		this.endTime = new Date();
		this.minStart = {};
		this.init();
	}

	_createClass(RoomsCtrl, [{
		key: "init",
		value: function init() {
			var _this = this;

			var current = new Date(this.currentTime);
			var ceil = Math.ceil(current.getMinutes() / this.steps) * this.steps;
			var addedMinutes = ceil - current.getMinutes();
			var rounded = (Math.floor(current.getTime() / 60000) + addedMinutes) * 60000;
			this.splashStatus = false;

			this.minStart = new Date(rounded);
			this.roomId = this.$stateParams.id;

			this.min = 0;
			this.max = 100;

			this.selectedTime = {
				value: 10
			};

			this.RestSrv.getRoom(this.roomId).then(function (data) {
				_this.room = data;

				if (data.available) {
					_this.steps = data.steps || 10;
					_this.selectedTime.value = _this.steps;

					if (_this.room.nextMeeting.from === undefined) {
						_this.max = 120;
						_this.room.nextMeeting.from = moment(current).add(2, 'hours');
						_this.room.nextMeeting.to = moment(current).add(3, 'hours');
					} else {
						var diffBetween = _this.diffBetweenTimes(_this.room.nextMeeting.from, _this.currentTime);
						var step = _this.steps;
						_this.max = Math.floor(diffBetween / step) * step;
					}

					_this.nextMeetingFrom = moment(_this.room.nextMeeting.from).format('LT');
					_this.nextMettingTo = moment(_this.room.nextMeeting.to).format('LT');

					_this.currentTimeLTS = moment(_this.minStart).format('LT');

					_this.$timeout(function () {
						_this.splashStatus = false;
					}, 1000);
				} else {

					if (_this.isHappeningNow(_this.room.nextMeeting)) {
						_this.splashStatus = false;
						// this.roomNotAvilable();
						_this.$state.transitionTo('notavailable', { 'room': _this.room, 'until': _this.room.nextMeeting.to });
					} else {
						_this.splashStatus = false;
						_this.reserveCountdown();
					}
				}
			});
		}
	}, {
		key: "isHappeningNow",
		value: function isHappeningNow(nextMeeting) {
			if (nextMeeting === undefined) {
				return false;
			}

			var now = this.moment();
			return now.isAfter(nextMeeting.from) && now.isBefore(nextMeeting.to);
		}
	}, {
		key: "checkSelectedTime",
		value: function checkSelectedTime() {
			return this.selectedTime.value > 0 ? false : true;
		}
	}, {
		key: "currentTo",
		value: function currentTo() {
			var res = (Math.floor(this.minStart.getTime() / 60000) + this.selectedTime.value) * 60000;
			this.endTime = new Date(res);

			var total = (this.endTime.getTime() - this.minStart.getTime()) / 60000;
			this.total = Math.floor(total / 60) + ':' + total % 60;

			return this.moment(this.endTime).format('LT');
		}
	}, {
		key: "diffBetweenTimes",
		value: function diffBetweenTimes(_current, _start) {
			var current = new Date(_current);
			var start = new Date(_start);
			var duration = current.getTime() - start.getTime();
			var diffInMinutes = duration / (1000 * 60);
			return diffInMinutes;
		}
	}, {
		key: "bookMeeting",
		value: function bookMeeting() {
			var _this2 = this;

			this.RestSrv.bookRoom(this.roomId, this.minStart.toISOString(), this.endTime.toISOString()).then(function (response) {
				_this2.roomBookedSucess();
			});
		}
	}, {
		key: "roomBookedSucess",
		value: function roomBookedSucess(ev) {
			this.$mdDialog.show({
				controller: function () {
					/* @ngInject */
					Ctrl.$inject = ["$mdDialog"];
					function Ctrl($mdDialog) {
						_classCallCheck(this, Ctrl);

						var roomSuccessCtrl = this;
						roomSuccessCtrl.$mdDialog = $mdDialog;
					}

					_createClass(Ctrl, [{
						key: "hide",
						value: function hide() {
							this.$mdDialog.hide();
						}
					}, {
						key: "cancel",
						value: function cancel() {
							this.$mdDialog.cancel();
						}
					}, {
						key: "answer",
						value: function answer(_answer) {
							this.$mdDialog.hide(_answer);
						}
					}]);

					return Ctrl;
				}(),
				locals: {
					room: this.room,
					from: this.currentTimeLTS,
					to: this.currentTo(),
					totalMinutes: this.total
				},
				bindToController: true,
				controllerAs: 'roomSuccessCtrl',
				templateUrl: 'rooms/views/roomBookedSucess.tpl.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose: false,
				escapeToClose: false,
				fullscreen: true
			}).then(function (answer) {});
		}
	}, {
		key: "nextMeetingInfo",
		value: function nextMeetingInfo(ev) {
			var useFullScreen = this.$mdMedia('xs');
			this.$mdDialog.show({
				controller: function () {
					/* @ngInject */
					Ctrl.$inject = ["$mdDialog"];
					function Ctrl($mdDialog) {
						_classCallCheck(this, Ctrl);

						var infoCtrl = this;
						infoCtrl.$mdDialog = $mdDialog;
					}

					_createClass(Ctrl, [{
						key: "hide",
						value: function hide() {
							this.$mdDialog.hide();
						}
					}, {
						key: "cancel",
						value: function cancel() {
							this.$mdDialog.cancel();
						}
					}, {
						key: "answer",
						value: function answer(_answer2) {
							this.$mdDialog.hide(_answer2);
						}
					}]);

					return Ctrl;
				}(),
				locals: this.room,
				bindToController: true,
				controllerAs: 'infoCtrl',
				templateUrl: 'rooms/views/nextMeetingInfo.tpl.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose: true,
				fullscreen: useFullScreen
			}).then(function (answer) {});
		}
	}, {
		key: "haveInfo",
		value: function haveInfo() {
			return this.room.nextMeeting.hasOwnProperty('orgenizer');
		}
	}, {
		key: "reserveCountdown",
		value: function reserveCountdown(ev) {
			this.$mdDialog.show({
				controller:
				/* @ngInject */
				["$mdDialog", "$state", "moment", function Ctrl($mdDialog, $state, moment) {
					_classCallCheck(this, Ctrl);

					var reserveCtrl = this;
					reserveCtrl.$mdDialog = $mdDialog;
					reserveCtrl.$state = $state;
				}],
				locals: {
					room: this.room
				},
				bindToController: true,
				controllerAs: 'reserveCtrl',
				templateUrl: 'rooms/views/reserveCountdown.tpl.html',
				parent: angular.element(document.body),
				clickOutsideToClose: false,
				escapeToClose: false,
				fullscreen: true
			}).then(function (answer) {});
		}
	}, {
		key: "roomNotAvilable",
		value: function roomNotAvilable(ev) {
			this.$mdDialog.show({
				controller: function () {
					/* @ngInject */
					Ctrl.$inject = ["$mdDialog", "$state", "moment", "$mdMedia"];
					function Ctrl($mdDialog, $state, moment, $mdMedia) {
						_classCallCheck(this, Ctrl);

						var notAvilableCtrl = this;
						notAvilableCtrl.$mdDialog = $mdDialog;
						notAvilableCtrl.$state = $state;
						notAvilableCtrl.$mdMedia = $mdMedia;

						this.moment = moment;
						notAvilableCtrl.until = this.moment(notAvilableCtrl.locals.until).format('LT');
					}

					_createClass(Ctrl, [{
						key: "goOptions",
						value: function goOptions() {
							this.$state.go('notavailable', { room: this.locals.room });
							this.$mdDialog.hide();
						}
					}]);

					return Ctrl;
				}(),
				locals: {
					room: this.room,
					until: this.room.nextMeeting.to
				},
				bindToController: true,
				controllerAs: 'notAvilableCtrl',
				templateUrl: 'rooms/views/roomNotAvilable.tpl.html',
				parent: angular.element(document.body),
				clickOutsideToClose: false,
				escapeToClose: false,
				fullscreen: true
			}).then(function (answer) {});
		}
	}]);

	return RoomsCtrl;
}();

exports.default = RoomsCtrl;

},{}],7:[function(require,module,exports){
'use strict';

Object.defineProperty(exports, "__esModule", {
	value: true
});

var _roomsCtrl = require('./controllers/rooms.ctrl.js');

var _roomsCtrl2 = _interopRequireDefault(_roomsCtrl);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

// Directives

exports.default = angular.module('Rooms', []).config(["$stateProvider", function ($stateProvider) {

	function getRoomPromises($q, $stataParams, RestSrv) {
		var promises = [getRoom($stateParams, RestSrv)];

		return $q.all(promises).then(resolve, rejected);
	}

	function resolve(data) {
		return data[0];
	}

	function rejected(reason) {
		return $q.reject();
	}

	function getRoom($stateParams, RestSrv) {
		var roomId = $stateParams.id;
		return RestSrv.getRoom(roomId);
	}

	$stateProvider.state('rooms', {
		url: '/rooms/:id',
		templateUrl: 'rooms/views/rooms.state.tpl.html',
		controller: 'RoomsCtrl as roomsCtrl'
		// resolve:{
		// 	Room: getRoomPromises
		// }
	});
}]).controller('RoomsCtrl', _roomsCtrl2.default); // Providers & Services

// Controllers

},{"./controllers/rooms.ctrl.js":6}],8:[function(require,module,exports){
"use strict";

Object.defineProperty(exports, "__esModule", {
	value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/* @ngInject */
var WelcomeCtrl = function () {
	/* @ngInject */
	WelcomeCtrl.$inject = ["$scope", "$log", "RestSrv", "$stateParams"];
	function WelcomeCtrl($scope, $log, RestSrv, $stateParams) {
		_classCallCheck(this, WelcomeCtrl);

		this.$scope = $scope;
		this.$log = $log;
		this.RestSrv = RestSrv;
		this.$stateParams = $stateParams;

		this.init();
	}

	_createClass(WelcomeCtrl, [{
		key: "init",
		value: function init() {
			this.splashStatus = true;
		}
	}]);

	return WelcomeCtrl;
}();

exports.default = WelcomeCtrl;

},{}],9:[function(require,module,exports){
'use strict';

Object.defineProperty(exports, "__esModule", {
	value: true
});

var _welcomeCtrl = require('./controllers/welcome.ctrl.js');

var _welcomeCtrl2 = _interopRequireDefault(_welcomeCtrl);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

// Directives

exports.default = angular.module('Welcome', []).config(["$stateProvider", function ($stateProvider) {

	$stateProvider.state('welcome', {
		url: '/welcome',
		templateUrl: 'welcome/views/welcome.state.tpl.html',
		controller: 'WelcomeCtrl as weCtrl'
	});
}]).controller('WelcomeCtrl', _welcomeCtrl2.default); // Providers & Services

// Controllers

},{"./controllers/welcome.ctrl.js":8}],10:[function(require,module,exports){
'use strict';

// Modules

Object.defineProperty(exports, "__esModule", {
	value: true
});

var _commonMdl = require('./components/common/scripts/common.mdl.js');

var _commonMdl2 = _interopRequireDefault(_commonMdl);

var _roomsStateMdl = require('./components/rooms/scripts/rooms.state.mdl.js');

var _roomsStateMdl2 = _interopRequireDefault(_roomsStateMdl);

var _notavailableStateMdl = require('./components/notavailable/scripts/notavailable.state.mdl.js');

var _notavailableStateMdl2 = _interopRequireDefault(_notavailableStateMdl);

var _welcomeStateMdl = require('./components/welcome/scripts/welcome.state.mdl.js');

var _welcomeStateMdl2 = _interopRequireDefault(_welcomeStateMdl);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

exports.default = angular.module('gettAroom', ['ngAnimate', 'ngSanitize', 'ui.router', 'angular-loading-bar', 'ngMaterial', _commonMdl2.default.name, _roomsStateMdl2.default.name, _notavailableStateMdl2.default.name, _welcomeStateMdl2.default.name]).config(["$stateProvider", "$locationProvider", "$urlRouterProvider", "$httpProvider", "$logProvider", "cfpLoadingBarProvider", "$mdThemingProvider", function ($stateProvider, $locationProvider, $urlRouterProvider, $httpProvider, $logProvider, cfpLoadingBarProvider, $mdThemingProvider) {
	cfpLoadingBarProvider.includeBar = false;
	cfpLoadingBarProvider.includeSpinner = true;
	cfpLoadingBarProvider.latencyThreshold = 50;
	// cfpLoadingBarProvider.spinnerTemplate = '<div><span class="fa fa-spinner">Loading...</div>';

	$mdThemingProvider.theme('default').primaryPalette('green').accentPalette('orange');

	$logProvider.debugEnabled(true);

	$urlRouterProvider.otherwise(function ($injector, $location) {
		$location.path('/welcome');
	});

	$locationProvider.html5Mode(true);
}]).run(["$rootScope", "$location", "$state", "$log", "$window", function ($rootScope, $location, $state, $log, $window) {
	// $rootScope.scanSplash = true;
	if ($location.search().id) {
		$state.transitionTo('rooms', { 'id': $location.search().id });
	}

	$rootScope.$on('$stateChangeSuccess', function (event) {
		$window.ga('send', 'pageview', $location.path());
		$log.info('google-send-pageview', $location.path());
	});

	$rootScope.safeApply = function (fn) {
		var phase = this.$root.$$phase;
		if (phase === '$apply' || phase === '$digest') {
			if (fn && typeof fn === 'function') {
				fn();
			}
		} else {
			this.$apply(fn);
		}
	};
}]).constant('CONFIG', {
	server: {
		url: localStorage && localStorage.getItem('serverUrl') || window.location.protocol + '//' + window.location.host,
		timeout: 3000
		// http://52.34.124.68:9090
	}
}).constant('moment', window.moment).constant('_', window._);

},{"./components/common/scripts/common.mdl.js":1,"./components/notavailable/scripts/notavailable.state.mdl.js":5,"./components/rooms/scripts/rooms.state.mdl.js":7,"./components/welcome/scripts/welcome.state.mdl.js":9}]},{},[10]);

angular.module("gettAroom").run(["$templateCache",function(o){o.put("notavailable/views/fallbacksModal.tpl.html",'<md-dialog class=fallbacks-modal aria-label ng-cloak><md-toolbar><div class=md-toolbar-tools><h2>rooms available</h2><span flex></span><md-button class=md-icon-button ng-click=fallbackCtrl.cancel()><i class=material-icons>close</i></md-button></div></md-toolbar><md-dialog-content><md-list flex><md-list-item class=md-3-line ng-repeat="room in fallbackCtrl.locals.fallbacks" ng-click=null><div class=md-list-item-text layout=column><h3>{{ room.id }} <span>[ {{room.name}} ]</span></h3></div></md-list-item></md-list></md-dialog-content></md-dialog>'),o.put("notavailable/views/notavailable.state.tpl.html",'<scan-splash status=notavCtrl.splashStatus massage="go to room & scan or tap"></scan-splash><div class="notavailable-state sliderdemoVertical" layout-fill><md-content layout=column layout-fill class="room-state-content alert-modal"><h1>Sorry!</h1><md-icon md-svg-src=./images/sad.svg class=s24 aria-label=Funny></md-icon><p>The current meeting in this room ends at</p><bold class=time>{{:: notavCtrl.until}}</bold><md-button class="next-metting-info md-icon-button" aria-label=More ng-click=notavCtrl.nextMeetingInfo($event)><i class=material-icons>info_outline</i></md-button></md-content></div>'),o.put("rooms/views/nextMeetingInfo.tpl.html",'<md-dialog class=next-meeting-modal aria-label ng-cloak><md-toolbar><div class=md-toolbar-tools><h2><i class=material-icons>perm_contact_calendar</i>{{infoCtrl.locals.nextMeeting.orgenizer}}</h2><span flex></span><md-button class=md-icon-button ng-click=infoCtrl.cancel()><i class=material-icons>close</i></md-button></div></md-toolbar><md-dialog-content><p>{{:: infoCtrl.locals.nextMeeting.subject}}</p><a ng-href="tel: {{:: infoCtrl.locals.nextMeeting.phone}}"><i class=material-icons>phone</i> Call {{:: infoCtrl.locals.nextMeeting.phone}}</a></md-dialog-content></md-dialog>'),o.put("rooms/views/reserveCountdown.tpl.html","<md-dialog class=reserve-countdown-modal aria-label ng-cloak><md-dialog-content class=alert-modal><h1>Sorry!</h1><md-icon md-svg-src=./images/angry.svg class=s24 aria-label=Funny></md-icon><p>This room has just been booked.Please try again soon</p></md-dialog-content></md-dialog>"),o.put("rooms/views/roomBookedSucess.tpl.html","<md-dialog class=booked-success-modal aria-label ng-cloak><md-dialog-content class=alert-modal><h1>Yes!</h1><md-icon md-svg-src=./images/funny.svg class=s24 aria-label=Funny></md-icon><p>You've just booked {{::roomSuccessCtrl.locals.room.id}} until</p><bold class=time>{{roomSuccessCtrl.locals.to}}</bold></md-dialog-content></md-dialog>"),o.put("rooms/views/roomNotAvilable.tpl.html","<md-dialog class=not-avilable-modal aria-label ng-cloak><md-dialog-content><bold>{{:: notAvilableCtrl.locals.room.id}} is</bold><h1>UNAVILABLE!</h1><i class=material-icons>sentiment_dissatisfied</i><h2>THIS ROOM IS BOOKED UNTIL</h2><bold>{{:: notAvilableCtrl.until}}</bold></md-dialog-content></md-dialog>"),o.put("rooms/views/rooms.state.tpl.html",'<scan-splash status=roomsCtrl.splashStatus massage="fetching data..."></scan-splash><div class="rooms-state sliderdemoVertical" layout-fill><md-toolbar class=app-header layout=row layout-align="space-between center"><div class=header-title layout=row layout-align="space-around center"><md-icon md-svg-src=./images/logo-text.svg aria-label="gett a room" class=logo-text></md-icon><h1>: {{ roomsCtrl.room.id }}</h1></div></md-toolbar><md-content layout=column layout-fill class=room-state-content><div class=next-metting flex=100><div class=circle><span class=pointer></span></div><md-button class="next-metting-info md-icon-button" aria-label=More ng-click=roomsCtrl.nextMeetingInfo($event) ng-if=roomsCtrl.haveInfo()><i class=material-icons>info_outline</i></md-button><div class=your-next-meeting ng-if=!roomsCtrl.room.nextMeeting><h3>YOUR MEETING</h3></div><div class=time-content ng-class="{\'show-next\' : !roomsCtrl.haveInfo()}" ng-if=roomsCtrl.room.nextMeeting><div ng-if=roomsCtrl.room.nextMeeting.orgenizer><bold>Next</bold><h3>From {{roomsCtrl.nextMeetingFrom}} to {{roomsCtrl.nextMettingTo}}</h3></div><bold class=reserve-bold ng-if=!roomsCtrl.room.nextMeeting.orgenizer>Reserve the room up to 2 hours from now</bold></div></div><div class=time-line layout=row layout-wrap flex=100><md-slider-container flex=30><md-slider md-discrete ng-model=roomsCtrl.selectedTime.value min={{roomsCtrl.min}} max={{roomsCtrl.max}} step={{roomsCtrl.steps}} aria-label=time class=md-primary md-vertical></md-slider></md-slider-container><div class=current-book flex=65><div class=details><i class="material-icons arrow-left">arrow_drop_down</i><div class=from-to-time><bold>FROM</bold><span>{{roomsCtrl.currentTimeLTS}}</span><bold>TO</bold><span>{{roomsCtrl.currentTo()}}</span><bold>TOTAL</bold><span>{{roomsCtrl.total}}</span><md-button class="md-fab md-primary" ng-disabled=roomsCtrl.checkSelectedTime() ng-click=roomsCtrl.bookMeeting() aria-label="Use Android"><i class=material-icons>check</i></md-button></div></div></div></div><div class=current-time flex=100><div class=circle><span class=pointer></span></div><div class=time-content><bold>Now</bold><h3>{{ roomsCtrl.currentTimeLTS}}</h3></div></div></md-content></div>'),o.put("welcome/views/welcome.state.tpl.html",'<div class=notavailable-state layout-fill><scan-splash status=weCtrl.splashStatus massage="scan or tap to gett a room !"></scan-splash></div>')}]);
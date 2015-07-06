(function () {
    'use strict';

    /* App Module */

    angular.module('ODKHelloWorld', ['motech-dashboard', 'ODKHelloWorld.controllers', 'ODKHelloWorld.directives', 'ODKHelloWorld.services', 'ngCookies'])
        .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/helloWorld', {templateUrl: '../ODK/resources/partials/say-hello.html', controller: 'HelloWorldController'});
    }]);
}());

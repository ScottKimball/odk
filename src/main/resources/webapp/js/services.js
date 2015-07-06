(function () {
    'use strict';

    /* Services */

    var services = angular.module('ODKHelloWorld.services', ['ngResource']);

    services.factory('HelloWorld', function($resource) {
        return $resource('../ODK/sayHello');
    });
}());

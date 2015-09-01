(function () {
    'use strict';

    /* Services */

    var services = angular.module('odk.services', ['ngResource']);

    services.factory('Settings', function($resource) {
        return $resource('../odk/settings');
    });
}());

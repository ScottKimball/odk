(function () {
    'use strict';

    /* Services */

    var services = angular.module('odk.services', ['ngResource']);

    services.factory('Configs', function($resource) {
        return $resource('../odk/configs');
    });
}());

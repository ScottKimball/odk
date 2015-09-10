(function () {
    'use strict';

    /* Services */

    var services = angular.module('odk.services', ['ngResource']);

    services.factory('Config', function($resource) {
        return $resource('../odk/configs/:name');
    });

    services.factory('Import', function($resource) {
        return $resource('../odk/import/:name');
    });

}());

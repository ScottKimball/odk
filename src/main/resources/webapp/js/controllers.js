(function() {
    'use strict';

    /* Controllers */
    var controllers = angular.module('odk.controllers', []);


    controllers.controller('SettingsCtrl', function($scope, Configs) {
        $scope.message = "Hello";
        Configs.query (function(configs) {
            $scope.configs = configs;

        },function (err) {
            //TODO notify user
            console.log(err);
        })

    });
}());

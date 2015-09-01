(function() {
    'use strict';

    /* Controllers */
    var controllers = angular.module('odk.controllers', []);


    controllers.controller('SettingsCtrl', function($scope, Settings) {
       $scope.message = "Hello";
    });
}());

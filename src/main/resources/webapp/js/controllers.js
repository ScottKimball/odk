(function() {
    'use strict';

    /* Controllers */
    var controllers = angular.module('odk.controllers', []);


    controllers.controller('SettingsCtrl', function($scope, Config) {

        $scope.saveSuccess = false;
        $scope.saveError = false;
        $scope.deleteSuccess = false;
        $scope.deleteError = false;
        $scope.getSuccess = false;
        $scope.configTypes = [{name : "ODK", type : "ODK "}, {name : "Ona", type : "ONA"}, {name : "Kobo", type : "KOBO"}];


        $scope.getConfigs = function () {
            Config.query (function(configs) {
                $scope.configs = configs;

            },function (err) {
                //TODO notify user
                console.log(err);
            });
        };

        $scope.getConfigs();


        $scope.saveConfig = function() {
            $scope.selectedConfig.$save();
            $scope.getConfigs();
        };

        $scope.addConfig = function() {
            $scope.selectedConfig = new Config({});
        };

        $scope.deleteConfig = function() {

            Config.delete({ name: $scope.selectedConfig.name}, function() {
                $scope.selectedConfig = null;
                $scope.getConfigs();
            }, function(error) {
                console.log(error);
            });
        };






    });
}());

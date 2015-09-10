(function() {
    'use strict';

    /* Controllers */
    var controllers = angular.module('odk.controllers', []);


    controllers.controller('SettingsCtrl', function($scope, $timeout, Config) {

        $scope.saveSuccess = false;
        $scope.saveError = false;
        $scope.deleteSuccess = false;
        $scope.deleteError = false;
        $scope.getConfigError = false;
        $scope.configTypes = [{name : "ODK", type : "ODK"}, {name : "Ona", type : "ONA"}, {name : "Kobo", type : "KOBO"}];


        $scope.getConfigs = function () {
            Config.query (function(configs) {
                $scope.configs = configs;
            },function (err) {
                console.log(err);
                $scope.getConfigError = true;
                $timeout(function() {
                    $scope.getConfigError = false;
                }, 5000)

            });
        };

        $scope.getConfigs();

        $scope.saveConfig = function() {
            Config.save($scope.selectedConfig, function(){
                $scope.saveSuccess = true;
                $scope.getConfigs();
                $timeout(function() {
                    $scope.saveSuccess = false;
                }, 5000);

            }, function(err) {
                console.log(err);
                $scope.saveError = true;
                $timeout(function() {
                    $scope.saveError = false;
                }, 5000);

            });
        };

        $scope.addConfig = function() {
            $scope.selectedConfig = new Config({});
        };

        $scope.deleteConfig = function() {

            Config.delete({ name: $scope.selectedConfig.name}, function() {
                $scope.selectedConfig = null;
                $scope.getConfigs();
                $scope.deleteSuccess = true;
                $timeout(function() {
                    $scope.deleteSuccess = false;
                }, 5000);
            }, function(error) {
                console.log(error);
                $scope.deleteError = true;
                $timeout(function() {
                    $scope.deleteError = false;
                }, 5000);
            });
        };
    });

    controllers.controller('ImportCtrl', function($scope, $timeout, Config, Import) {
        $scope.importSuccess = false;
        $scope.importFail = false;

        var importFail = function() {
            $scope.importFail = true;
            $timeout(function() {
                $scope.importSuccess = false;
            }, 5000);
        };

        Config.query (function(configs) {
            $scope.configs = configs;
        },function (err) {
            console.log(err);
        });


        $scope.import = function() {
            Import.get({name: $scope.selectedConfig.name}, function(success) {

                if (success) {
                    $scope.importSuccess = true;
                    $timeout(function() {
                        $scope.importSuccess = false;
                    }, 5000);
                } else {
                  importFail();
                }

            }, function(err) {
                console.log(err);
                importFail();
            });
        };
    });

}());

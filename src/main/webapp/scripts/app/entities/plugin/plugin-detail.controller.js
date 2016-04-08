'use strict';

angular.module('samApp')
    .controller('PluginDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Plugin, User) {
        $scope.plugin = entity;
        $scope.load = function (id) {
            Plugin.get({id: id}, function(result) {
                $scope.plugin = result;
            });
        };
        var unsubscribe = $rootScope.$on('samApp:pluginUpdate', function(event, result) {
            $scope.plugin = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });

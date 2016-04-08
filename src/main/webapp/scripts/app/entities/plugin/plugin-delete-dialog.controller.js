'use strict';

angular.module('samApp')
	.controller('PluginDeleteController', function($scope, $uibModalInstance, entity, Plugin) {

        $scope.plugin = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Plugin.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });

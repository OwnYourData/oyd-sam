'use strict';

angular.module('samApp').controller('PluginDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Plugin', 'User',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Plugin, User) {

        $scope.plugin = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Plugin.get({id : id}, function(result) {
                $scope.plugin = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('samApp:pluginUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.plugin.id != null) {
                Plugin.update($scope.plugin, onSaveSuccess, onSaveError);
            } else {
                Plugin.save($scope.plugin, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;

        $scope.setZip = function ($file, plugin) {
            if ($file) {
                var fileReader = new FileReader();
                fileReader.readAsDataURL($file);
                fileReader.onload = function (e) {
                    var base64Data = e.target.result.substr(e.target.result.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function() {
                        plugin.zip = base64Data;
                        plugin.zipContentType = $file.type;
                    });
                };
            }
        };
}]);

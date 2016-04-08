'use strict';

angular.module('samApp')
    .factory('PluginSearch', function ($resource) {
        return $resource('api/_search/plugins/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

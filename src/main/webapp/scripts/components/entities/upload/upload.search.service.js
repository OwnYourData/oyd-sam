'use strict';

angular.module('samApp')
    .factory('UploadSearch', function ($resource) {
        return $resource('api/_search/uploads/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

'use strict';

angular.module('samApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });



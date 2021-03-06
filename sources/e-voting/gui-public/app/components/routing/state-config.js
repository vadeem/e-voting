/******************************************************************************
 * e-voting system                                                            *
 * Copyright (C) 2016 DSX Technologies Limited.                               *
 *                                                                            *
 * This program is free software; you can redistribute it and/or modify       *
 * it under the terms of the GNU General Public License as published by       *
 * the Free Software Foundation; either version 2 of the License, or          *
 * (at your option) any later version.                                        *
 *                                                                            *
 * This program is distributed in the hope that it will be useful,            *
 * but WITHOUT ANY WARRANTY; without even the implied                         *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 *                                                                            *
 * You can find copy of the GNU General Public License in LICENSE.txt file    *
 * at the top-level directory of this distribution.                           *
 *                                                                            *
 * Removal or modification of this copyright notice is prohibited.            *
 *                                                                            *
 ******************************************************************************/

'use strict';

angular
  .module('e-voting.routing', [
    'ui.router',
    'ngResource',
    'e-voting.routing.404-view'
  ])
  .config(['$stateProvider', '$urlRouterProvider', '$locationProvider', '$resourceProvider',
    function ($stateProvider, $urlRouterProvider, $locationProvider, $resourceProvider) {

      var injector = angular.injector(['ng', 'e-voting.role-management']),
        roleEnums = injector.get('roleEnums');

      $urlRouterProvider.rule(function ($injector, $location) {
        var path = $location.path(), search = $location.search();
        if (path[path.length - 1] !== '/') {
          if (angular.equals(search, {})) {
            return path + '/';
          } else {
            var params = [];
            angular.forEach(search, function (v, k) {
              params.push(k + '=' + v);
            });
            return path + '/?' + params.join('&');
          }
        }
      });

      $urlRouterProvider.otherwise(function ($injector) {
        $state.go('404', {locale: $injector.get("$sessionStorage").locale});
      });

      $stateProvider
        .state('votingList', {
          url: '/votingList/:locale/',
          templateUrl: 'views/votingList/votingList.html',
          controller: 'VotingListController',
          controllerAs: 'vlc',
          params: {
            locale: {
              value: 'en-gb',
              squash: true
            }
          },
          access: {
            loginRequired: true
          }
        })
        .state('decodeTransaction', {
          url: '/decodeTransaction/:locale/',
          templateUrl: 'views/decodeTransaction/decodeTransaction.html',
          controller: 'DecodeTransactionController',
          controllerAs: 'dtc',
          params: {
            locale: {
              value: 'en-gb',
              squash: true
            }
          },
          access: {
            loginRequired: false
          }
        })
        .state('votingResult', {
          url: '/votingResult/:locale/?id',
          templateUrl: 'views/votingResult/votingResult.html',
          controller: 'VotingResultController',
          controllerAs: 'vrc',
          params: {
            locale: {
              value: 'en-gb',
              squash: true
            },
            id: undefined
          },
          access: {
            loginRequired: true
          }
        })
        .state('confirmedVotes', {
          url: '/confirmedVotes/:locale/?id',
          templateUrl: 'views/votes/votesList.html',
          controller: 'VotesListController',
          controllerAs: 'volc',
          params: {
            locale: {
              value: 'en-gb',
              squash: true
            },
            id: undefined
          },
          access: {
            loginRequired: true,
            permissionsRequired: [roleEnums.roles.admin]
          }
        })
        .state('voting', {
          url: '/voting/:locale/?id',
          templateUrl: 'views/voting/voting.html',
          controller: 'VotingController',
          controllerAs: 'vc',
          params: {
            locale: {
              value: 'en-gb',
              squash: true
            },
            id: undefined
          },
          access: {
            loginRequired: false,
            permissionsRequired: [roleEnums.roles.user]
          }
        })
        .state('votingTotalResult', {
          url: '/votingTotalResult/:locale/?id',
          templateUrl: 'views/votingTotalResult/votingTotalResult.html',
          controller: 'VotingTotalResultController',
          controllerAs: 'vtrc',
          params: {
            isLoginRequired: false,
            locale: {
              value: 'en-gb',
              squash: true
            },
            id: undefined
          }
        })
        .state('allVotingStatuses', {
          url: '/allVotingStatuses/:locale/?id',
          templateUrl: 'views/allVotingStatuses/allVotingStatuses.html',
          controller: 'AllVotingStatusesController',
          controllerAs: 'avsc',
          params: {
            isLoginRequired: false,
            locale: {
              value: 'en-gb',
              squash: true
            },
            id: undefined
          }
        })
        .state('404', {
          url: '/404/:locale/',
          templateUrl: 'views/404/404.html',
          controller: 'NotFoundController',
          controllerAs: 'nfc',
          params: {
            locale: {
              value: 'en-gb',
              squash: true
            }
          },
          access: {
            loginRequired: false
          }
        })
        .state('signIn', {
          url: '/:locale/',
          templateUrl: 'views/signIn/signIn.html',
          controller: 'SignInController',
          controllerAs: 'sic',
          params: {
            locale: {
              value: 'en-gb',
              squash: true
            }
          },
          access: {
            loginRequired: false
          }
        });
      $locationProvider.html5Mode(true).hashPrefix('!');
      $resourceProvider.defaults.stripTrailingSlashes = false;
    }
  ]);

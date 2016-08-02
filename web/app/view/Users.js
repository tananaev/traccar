/*
 * Copyright 2015 Anton Tananaev (anton.tananaev@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

Ext.define('Traccar.view.Users', {
    extend: 'Ext.grid.Panel',
    xtype: 'usersView',

    requires: [
        'Traccar.view.UsersController',
        'Traccar.view.EditToolbar'
    ],

    controller: 'users',
    store: 'Users',

    selType: 'rowmodel',

    tbar: {
        xtype: 'editToolbar',
        items: [{
            text: getString('deviceTitle'),
            disabled: true,
            handler: 'onDevicesClick',
            reference: 'userDevicesButton'
        }, {
            text: getString('settingsGroups'),
            disabled: true,
            handler: 'onGroupsClick',
            reference: 'userGroupsButton'
        }, {
            text: getString('sharedGeofences'),
            disabled: true,
            handler: 'onGeofencesClick',
            reference: 'userGeofencesButton'
        }, {
            text: getString('sharedNotifications'),
            disabled: true,
            handler: 'onNotificationsClick',
            reference: 'userNotificationsButton'
        }]
    },

    listeners: {
        selectionchange: 'onSelectionChange'
    },

    columns: [{
        text: getString('sharedName'),
        dataIndex: 'name',
        flex: 1
    }, {
        text: getString('userEmail'),
        dataIndex: 'email',
        flex: 1
    }, {
        text: getString('userAdmin'),
        dataIndex: 'admin',
        flex: 1
    }]
});

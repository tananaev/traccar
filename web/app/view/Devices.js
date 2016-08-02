/*
 * Copyright 2015 - 2016 Anton Tananaev (anton.tananaev@gmail.com)
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

Ext.define('Traccar.view.Devices', {
    extend: 'Ext.grid.Panel',
    xtype: 'devicesView',

    requires: [
        'Traccar.view.DevicesController',
        'Traccar.view.EditToolbar',
        'Traccar.view.SettingsMenu'
    ],

    controller: 'devices',
    rootVisible: false,

    initComponent: function () {
        this.store = Ext.create('Ext.data.ChainedStore', {
            source: 'Devices',
            groupField: 'groupId'
        });
        this.callParent();
    },

    title: getString('deviceTitle'),
    selType: 'rowmodel',

    tbar: {
        xtype: 'editToolbar',
        items: [{
            xtype: 'button',
            disabled: true,
            handler: 'onGeofencesClick',
            reference: 'toolbarGeofencesButton',
            glyph: 'xf21d@FontAwesome',
            tooltip: getString('sharedGeofences'),
            tooltipType: 'title'
        }, {
            disabled: true,
            handler: 'onCommandClick',
            reference: 'deviceCommandButton',
            glyph: 'xf093@FontAwesome',
            tooltip: getString('deviceCommand'),
            tooltipType: 'title'
        }, {
            xtype: 'tbfill'
        }, {
            id: 'muteButton',
            glyph: 'xf1f7@FontAwesome',
            tooltip: getString('sharedMute'),
            tooltipType: 'title',
            pressed : true,
            enableToggle: true,
            listeners: {
                toggle: function (button, pressed) {
                    if (pressed) {
                        button.setGlyph('xf1f7@FontAwesome');
                    } else {
                        button.setGlyph('xf0a2@FontAwesome');
                    }
                },
                scope: this
            }
        }, {
            id: 'deviceFollowButton',
            glyph: 'xf05b@FontAwesome',
            tooltip: getString('deviceFollow'),
            tooltipType: 'title',
            enableToggle: true,
            toggleHandler: 'onFollowClick'
        }, {
            xtype: 'settingsMenu'
        }]
    },

    bbar: [{
        xtype: 'tbtext',
        html: getString('groupParent')
    }, {
        xtype: 'combobox',
        store: 'Groups',
        queryMode: 'local',
        displayField: 'name',
        valueField: 'id',
        flex: 1,
        listeners: {
            change: function () {
                if (Ext.isNumber(this.getValue())) {
                    this.up('grid').store.filter({
                        id: 'groupFilter',
                        filterFn: function (item) {
                            var groupId, group, groupStore, filter = true;
                            groupId = item.get('groupId');
                            groupStore = Ext.getStore('Groups');

                            while (groupId) {
                                group = groupStore.getById(groupId);
                                if (group) {
                                    if (group.get('id') === this.getValue()) {
                                        filter = false;
                                        break;
                                    }
                                    groupId = group.get('groupId');
                                } else {
                                    groupId = 0;
                                }
                            }

                            return !filter;
                        },
                        scope: this
                    });
                } else {
                    this.up('grid').store.removeFilter('groupFilter');
                }
            }
        }
    }, {
        xtype: 'tbtext',
        html: getString('sharedSearch')
    }, {
        xtype: 'textfield',
        flex: 1,
        listeners: {
            change: function () {
                this.up('grid').store.filter('name', this.getValue());
            }
        }
    }],

    listeners: {
        selectionchange: 'onSelectionChange'
    },

    columns: [{
        text: getString('sharedName'),
        dataIndex: 'name',
        flex: 1
    }, {
        text: getString('deviceLastUpdate'),
        dataIndex: 'lastUpdate',
        flex: 1,
        renderer: function (value, metaData, record) {
            switch (record.get('status')) {
                case 'online':
                    metaData.tdCls = 'view-color-green';
                    break;
                case 'offline':
                    metaData.tdCls = 'view-color-red';
                    break;
                default:
                    metaData.tdCls = 'view-color-yellow';
                    break;
            }
            if (Traccar.app.getPreference('twelveHourFormat', false)) {
                return Ext.Date.format(value, Traccar.Style.dateTimeFormat12);
            } else {
                return Ext.Date.format(value, Traccar.Style.dateTimeFormat24);
            }
        }
    }]

});

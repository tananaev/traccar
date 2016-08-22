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

Ext.define('Traccar.view.MapController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.map',

    config: {
        listen: {
            controller: {
                '*': {
                    selectDevice: 'selectDevice',
                    selectReport: 'selectReport',
                    clickDevice: 'clickDevice'
                }
            },
            store: {
                '#Devices': {
                    add: 'updateDevice',
                    update: 'updateDevice'
                },
                '#LatestPositions': {
                    add: 'updateLatest',
                    update: 'updateLatest'
                },
                '#ReportRoute': {
                    load: 'loadReport',
                    clear: 'clearReport'
                }
            },
            component: {
                '#': {
                    selectFeature: 'selectFeature',
                    clickMap: 'clickMap'
                }
            }
        }
    },

    init: function () {
        this.latestMarkers = {};
        this.reportMarkers = {};
    },

    getDeviceColor: function (device) {
        switch (device.get('status')) {
            case 'online':
                return Traccar.Style.mapColorOnline;
            case 'offline':
                return Traccar.Style.mapColorOffline;
            default:
                return Traccar.Style.mapColorUnknown;
        }
    },

    changeMarkerColor: function (style, color) {
        return new ol.style.Style({
            image: new ol.style.Arrow({
                radius: style.getImage().getRadius(),
                fill: new ol.style.Fill({
                    color: color
                }),
                stroke: style.getImage().getStroke(),
                rotation: style.getImage().getRotation()
            }),
            text: style.getText()
        });
    },

    updateDevice: function (store, data) {
        var i, device, deviceId, marker;

        if (!Ext.isArray(data)) {
            data = [data];
        }

        for (i = 0; i < data.length; i++) {
            device = data[i];
            deviceId = device.get('id');

            if (deviceId in this.latestMarkers) {
                marker = this.latestMarkers[deviceId];
                marker.setStyle(
                    this.changeMarkerColor(marker.getStyle(), this.getDeviceColor(device)));
            }
        }
    },

    followSelected: function () {
        return Ext.getCmp('deviceFollowButton') && Ext.getCmp('deviceFollowButton').pressed;
    },

    updateLatest: function (store, data) {
        var i, position, geometry, device, deviceId, marker, style;

        if (!Ext.isArray(data)) {
            data = [data];
        }

        for (i = 0; i < data.length; i++) {
            position = data[i];
            deviceId = position.get('deviceId');
            device = Ext.getStore('Devices').findRecord('id', deviceId, 0, false, false, true);

            if (device) {
                geometry = new ol.geom.Point(ol.proj.fromLonLat([
                    position.get('longitude'),
                    position.get('latitude')
                ]));

                if (deviceId in this.latestMarkers) {
                    marker = this.latestMarkers[deviceId];
                    marker.setGeometry(geometry);
                } else {
                    marker = new ol.Feature(geometry);
                    marker.set('record', device);
                    this.latestMarkers[deviceId] = marker;
                    this.getView().getLatestSource().addFeature(marker);

                    style = this.getLatestMarker(this.getDeviceColor(device));
                    style.getText().setText(device.get('name'));
                    marker.setStyle(style);
                }

                marker.getStyle().getImage().setRotation(position.get('course') * Math.PI / 180);

                if (marker === this.selectedMarker && this.followSelected()) {
                    this.getView().getMapView().setCenter(marker.getGeometry().getCoordinates());
                }
            }
        }
    },

    loadReport: function (store, data) {
        var i, position, point, geometry, marker, style;

        this.clearReport(store);

        if (data.length > 0) {
            this.reportRoute = new ol.Feature({
                geometry: new ol.geom.LineString([])
            });
            this.reportRoute.setStyle(this.getRouteStyle());
            this.getView().getRouteSource().addFeature(this.reportRoute);

            for (i = 0; i < data.length; i++) {
                position = data[i];

                point = ol.proj.fromLonLat([
                    position.get('longitude'),
                    position.get('latitude')
                ]);
                geometry = new ol.geom.Point(point);

                marker = new ol.Feature(geometry);
                marker.set('record', position);
                this.reportMarkers[position.get('id')] = marker;
                this.getView().getReportSource().addFeature(marker);

                style = this.getReportMarker();
                style.getImage().setRotation(position.get('course') * Math.PI / 180);
                /*style.getText().setText(
                    Ext.Date.format(position.get('fixTime'), Traccar.Style.dateTimeFormat24));*/

                marker.setStyle(style);

                this.reportRoute.getGeometry().appendCoordinate(point);
            }

            this.getView().getMapView().fit(this.reportRoute.getGeometry(), this.getView().getMap().getSize());
        }
    },

    clearReport: function (store) {
        var key;

        if (this.reportRoute) {
            this.getView().getRouteSource().removeFeature(this.reportRoute);
            this.reportRoute = null;
        }

        if (this.reportMarkers) {
            for (key in this.reportMarkers) {
                if (this.reportMarkers.hasOwnProperty(key)) {
                    this.getView().getReportSource().removeFeature(this.reportMarkers[key]);
                }
            }
            this.reportMarkers = {};
        }
    },

    getRouteStyle: function () {
        return new ol.style.Style({
            stroke: new ol.style.Stroke({
                color: Traccar.Style.mapRouteColor,
                width: Traccar.Style.mapRouteWidth
            })
        });
    },

    getMarkerStyle: function (radius, color) {
        return new ol.style.Style({
            image: new ol.style.Arrow({
                radius: radius,
                fill: new ol.style.Fill({
                    color: color
                }),
                stroke: new ol.style.Stroke({
                    color: Traccar.Style.mapArrowStrokeColor,
                    width: Traccar.Style.mapArrowStrokeWidth
                })
            }),
            text: new ol.style.Text({
                textBaseline: 'bottom',
                fill: new ol.style.Fill({
                    color: Traccar.Style.mapTextColor
                }),
                stroke: new ol.style.Stroke({
                    color: Traccar.Style.mapTextStrokeColor,
                    width: Traccar.Style.mapTextStrokeWidth
                }),
                offsetY: -radius / 2 - Traccar.Style.mapTextOffset,
                font : Traccar.Style.mapTextFont
            })
        });
    },

    getLatestMarker: function (color) {
        return this.getMarkerStyle(
            Traccar.Style.mapRadiusNormal, color);
    },

    getReportMarker: function () {
        return this.getMarkerStyle(
            Traccar.Style.mapRadiusNormal, Traccar.Style.mapColorReport);
    },

    resizeMarker: function (style, radius) {
        return new ol.style.Style({
            image: new ol.style.Arrow({
                radius: radius,
                fill: style.getImage().getFill(),
                stroke: style.getImage().getStroke(),
                rotation: style.getImage().getRotation()
            }),
            text: style.getText()
        });
    },

    selectMarker: function (marker, center) {
        if (this.selectedMarker) {
            this.selectedMarker.setStyle(
                this.resizeMarker(this.selectedMarker.getStyle(), Traccar.Style.mapRadiusNormal));
        }

        if (marker) {
            marker.setStyle(
                this.resizeMarker(marker.getStyle(), Traccar.Style.mapRadiusSelected));
            if (center) {
                this.getView().getMapView().setCenter(marker.getGeometry().getCoordinates());
            }
        }

        this.selectedMarker = marker;
    },

    selectDevice: function (device, center) {
        this.hidePopup();
        this.selectMarker(this.latestMarkers[device.get('id')], center);
    },

    selectReport: function (position, center) {
        this.selectMarker(this.reportMarkers[position.get('id')], center);
    },

    clickDevice: function (feature) {
        var record, attributes;

        record = feature.get('record');
        attributes = record.get('attributes');
        if (Object.getOwnPropertyNames(attributes).length) {
            this.showAttributesView(feature);
        }
    },

    selectFeature: function (feature) {
        var record = feature.get('record');
        if (record) {
            if (record instanceof Traccar.model.Device) {
                this.fireEvent('selectDevice', record, false);
                this.fireEvent('clickDevice', feature);
            } else {
                this.fireEvent('selectReport', record, false);
            }
        }
    },

    clickMap: function () {
        this.hidePopup();
    },

    showAttributesView: function (feature) {
        var coordinates, popupOverlay, record, that;

        coordinates = feature.getGeometry().getCoordinates();
        popupOverlay = this.getView().getPopupOverlay();
        record = feature.get('record');
        that = this;

        popupOverlay.setPosition(coordinates);

        Ext.create('Traccar.view.PopupWindow', {
            title: record.get('name') + ' ' + Strings.sharedAttributes,
            renderTo: Ext.get('popup'),
            items: {
                xtype: 'attributesView',
                record: record,
                tbar: {
                    xtype: 'editToolbar',
                    items: [{
                        xtype: 'tbfill'
                    }, {
                        glyph: 'xf00e@FontAwesome',
                        tooltip: Strings.deviceZoomIn,
                        tooltipType: 'title',
                        listeners: {
                            click: 'onZoomInClick',
                            scope: that
                        }
                    }]
                }
            }
        }).show();
    },

    hidePopup: function () {
        var popupOverlay = this.getView().getPopupOverlay();
        popupOverlay.setPosition(undefined);
    },

    onZoomInClick: function () {
        var marker, mapView;
        marker = this.selectedMarker;
        if (marker) {
            mapView = this.getView().getMapView();
            mapView.setCenter(marker.getGeometry().getCoordinates());
            mapView.setZoom(Traccar.Style.mapMaxZoom);
        }
    }
});

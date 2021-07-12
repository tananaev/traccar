#!/bin/sh

PRESERVECONFIG=0
if [ -f /opt/traccar/conf/traccar.xml ]
then
    cp /opt/traccar/conf/traccar.xml /opt/traccar/conf/traccar.xml.saved
    PRESERVECONFIG=1
fi

ISACTIVE=`systemctl is-active traccar.service`
if [ "${ISACTIVE}" == "active" ]
then
    systemctl stop traccar
fi

mkdir -p /opt/traccar
cp -r -f * /opt/traccar
chmod -R go+rX /opt/traccar

if [ ${PRESERVECONFIG} -eq 1 ] && [ -f /opt/traccar/conf/traccar.xml.saved ]
then
    mv -f /opt/traccar/conf/traccar.xml.saved /opt/traccar/conf/traccar.xml
fi

mv /opt/traccar/traccar.service /etc/systemd/system
chmod 664 /etc/systemd/system/traccar.service

systemctl daemon-reload
systemctl enable traccar

rm /opt/traccar/setup.sh
rm -r ../out

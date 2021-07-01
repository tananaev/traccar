--------------------------------------------------------
--  DDL for Table DATABASECHANGELOG
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."DATABASECHANGELOG" 
   (	"ID" VARCHAR2(255), 
	"AUTHOR" VARCHAR2(255), 
	"FILENAME" VARCHAR2(255), 
	"DATEEXECUTED" TIMESTAMP (6), 
	"ORDEREXECUTED" NUMBER(*,0), 
	"EXECTYPE" VARCHAR2(10), 
	"MD5SUM" VARCHAR2(35), 
	"DESCRIPTION" VARCHAR2(255), 
	"COMMENTS" VARCHAR2(255), 
	"TAG" VARCHAR2(255), 
	"LIQUIBASE" VARCHAR2(20), 
	"CONTEXTS" VARCHAR2(255), 
	"LABELS" VARCHAR2(255), 
	"DEPLOYMENT_ID" VARCHAR2(10)
   ) ;
--------------------------------------------------------
--  DDL for Table DATABASECHANGELOGLOCK
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."DATABASECHANGELOGLOCK" 
   (	"ID" NUMBER(*,0), 
	"LOCKED" NUMBER(1,0), 
	"LOCKGRANTED" TIMESTAMP (6), 
	"LOCKEDBY" VARCHAR2(255)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_ATTRIBUTES
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_ATTRIBUTES" 
   (	"ID" NUMBER(*,0), 
	"DESCRIPTION" VARCHAR2(4000), 
	"TYPE" VARCHAR2(128), 
	"ATTRIBUTE" VARCHAR2(128), 
	"EXPRESSION" VARCHAR2(4000)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_CALENDARS
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_CALENDARS" 
   (	"ID" NUMBER(*,0), 
	"NAME" VARCHAR2(128), 
	"DATA" BLOB, 
	"ATTRIBUTES" VARCHAR2(4000)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_COMMANDS
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_COMMANDS" 
   (	"ID" NUMBER(*,0), 
	"DESCRIPTION" VARCHAR2(4000), 
	"TYPE" VARCHAR2(128), 
	"TEXTCHANNEL" NUMBER(1,0) DEFAULT 0, 
	"ATTRIBUTES" VARCHAR2(4000)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_DEVICES
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_DEVICES" 
   (	"ID" NUMBER(*,0), 
	"NAME" VARCHAR2(128), 
	"UNIQUEID" VARCHAR2(128), 
	"LASTUPDATE" TIMESTAMP (6), 
	"POSITIONID" NUMBER(*,0), 
	"GROUPID" NUMBER(*,0), 
	"ATTRIBUTES" VARCHAR2(4000), 
	"PHONE" VARCHAR2(128), 
	"MODEL" VARCHAR2(128), 
	"CONTACT" VARCHAR2(512), 
	"CATEGORY" VARCHAR2(128), 
	"DISABLED" NUMBER(1,0) DEFAULT 0
   ) ;
--------------------------------------------------------
--  DDL for Table TC_DEVICE_ATTRIBUTE
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_DEVICE_ATTRIBUTE" 
   (	"DEVICEID" NUMBER(*,0), 
	"ATTRIBUTEID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_DEVICE_COMMAND
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_DEVICE_COMMAND" 
   (	"DEVICEID" NUMBER(*,0), 
	"COMMANDID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_DEVICE_DRIVER
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_DEVICE_DRIVER" 
   (	"DEVICEID" NUMBER(*,0), 
	"DRIVERID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_DEVICE_GEOFENCE
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_DEVICE_GEOFENCE" 
   (	"DEVICEID" NUMBER(*,0), 
	"GEOFENCEID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_DEVICE_MAINTENANCE
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_DEVICE_MAINTENANCE" 
   (	"DEVICEID" NUMBER(*,0), 
	"MAINTENANCEID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_DEVICE_NOTIFICATION
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_DEVICE_NOTIFICATION" 
   (	"DEVICEID" NUMBER(*,0), 
	"NOTIFICATIONID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_DRIVERS
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_DRIVERS" 
   (	"ID" NUMBER(*,0), 
	"NAME" VARCHAR2(128), 
	"UNIQUEID" VARCHAR2(128), 
	"ATTRIBUTES" VARCHAR2(4000)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_EVENTS
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_EVENTS" 
   (	"ID" NUMBER(*,0), 
	"TYPE" VARCHAR2(128), 
	"SERVERTIME" TIMESTAMP (6), 
	"DEVICEID" NUMBER(*,0), 
	"POSITIONID" NUMBER(*,0), 
	"GEOFENCEID" NUMBER(*,0), 
	"ATTRIBUTES" VARCHAR2(4000), 
	"MAINTENANCEID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_GEOFENCES
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_GEOFENCES" 
   (	"ID" NUMBER(*,0), 
	"NAME" VARCHAR2(128), 
	"DESCRIPTION" VARCHAR2(128), 
	"AREA" VARCHAR2(4000), 
	"ATTRIBUTES" VARCHAR2(4000), 
	"CALENDARID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_GROUPS
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_GROUPS" 
   (	"ID" NUMBER(*,0), 
	"NAME" VARCHAR2(128), 
	"GROUPID" NUMBER(*,0), 
	"ATTRIBUTES" VARCHAR2(4000)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_GROUP_ATTRIBUTE
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_GROUP_ATTRIBUTE" 
   (	"GROUPID" NUMBER(*,0), 
	"ATTRIBUTEID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_GROUP_COMMAND
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_GROUP_COMMAND" 
   (	"GROUPID" NUMBER(*,0), 
	"COMMANDID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_GROUP_DRIVER
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_GROUP_DRIVER" 
   (	"GROUPID" NUMBER(*,0), 
	"DRIVERID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_GROUP_GEOFENCE
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_GROUP_GEOFENCE" 
   (	"GROUPID" NUMBER(*,0), 
	"GEOFENCEID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_GROUP_MAINTENANCE
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_GROUP_MAINTENANCE" 
   (	"GROUPID" NUMBER(*,0), 
	"MAINTENANCEID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_GROUP_NOTIFICATION
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_GROUP_NOTIFICATION" 
   (	"GROUPID" NUMBER(*,0), 
	"NOTIFICATIONID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_MAINTENANCES
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_MAINTENANCES" 
   (	"ID" NUMBER(*,0), 
	"NAME" VARCHAR2(4000), 
	"TYPE" VARCHAR2(128), 
	"start" FLOAT(24) DEFAULT 0, 
	"PERIOD" FLOAT(24) DEFAULT 0, 
	"ATTRIBUTES" VARCHAR2(4000)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_NOTIFICATIONS
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_NOTIFICATIONS" 
   (	"ID" NUMBER(*,0), 
	"TYPE" VARCHAR2(128), 
	"ATTRIBUTES" VARCHAR2(4000), 
	"ALWAYS" NUMBER(1,0) DEFAULT 0, 
	"CALENDARID" NUMBER(*,0), 
	"NOTIFICATORS" VARCHAR2(128)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_POSITIONS
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_POSITIONS" 
   (	"ID" NUMBER(*,0), 
	"PROTOCOL" VARCHAR2(128), 
	"DEVICEID" NUMBER(*,0), 
	"SERVERTIME" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
	"DEVICETIME" TIMESTAMP (6), 
	"FIXTIME" TIMESTAMP (6), 
	"VALID" NUMBER(1,0), 
	"LATITUDE" FLOAT(24), 
	"LONGITUDE" FLOAT(24), 
	"ALTITUDE" FLOAT(126), 
	"SPEED" FLOAT(126), 
	"COURSE" FLOAT(126), 
	"ADDRESS" VARCHAR2(512), 
	"ATTRIBUTES" VARCHAR2(4000), 
	"ACCURACY" FLOAT(24) DEFAULT 0, 
	"NETWORK" VARCHAR2(4000)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_SERVERS
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_SERVERS" 
   (	"ID" NUMBER(*,0), 
	"REGISTRATION" NUMBER(1,0) DEFAULT 1, 
	"LATITUDE" FLOAT(24) DEFAULT 0, 
	"LONGITUDE" FLOAT(24) DEFAULT 0, 
	"ZOOM" NUMBER(*,0) DEFAULT 0, 
	"MAP" VARCHAR2(128), 
	"BINGKEY" VARCHAR2(128), 
	"MAPURL" VARCHAR2(512), 
	"READONLY" NUMBER(1,0) DEFAULT 0, 
	"TWELVEHOURFORMAT" NUMBER(1,0) DEFAULT 0, 
	"ATTRIBUTES" VARCHAR2(4000), 
	"FORCESETTINGS" NUMBER(1,0) DEFAULT 0, 
	"COORDINATEFORMAT" VARCHAR2(128), 
	"DEVICEREADONLY" NUMBER(1,0) DEFAULT 0, 
	"LIMITCOMMANDS" NUMBER(1,0) DEFAULT 0, 
	"POILAYER" VARCHAR2(512)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_STATISTICS
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_STATISTICS" 
   (	"ID" NUMBER(*,0), 
	"CAPTURETIME" TIMESTAMP (6), 
	"ACTIVEUSERS" NUMBER(*,0) DEFAULT 0, 
	"ACTIVEDEVICES" NUMBER(*,0) DEFAULT 0, 
	"REQUESTS" NUMBER(*,0) DEFAULT 0, 
	"MESSAGESRECEIVED" NUMBER(*,0) DEFAULT 0, 
	"MESSAGESSTORED" NUMBER(*,0) DEFAULT 0, 
	"ATTRIBUTES" VARCHAR2(4000), 
	"MAILSENT" NUMBER(*,0) DEFAULT 0, 
	"SMSSENT" NUMBER(*,0) DEFAULT 0, 
	"GEOCODERREQUESTS" NUMBER(*,0) DEFAULT 0, 
	"GEOLOCATIONREQUESTS" NUMBER(*,0) DEFAULT 0
   ) ;
--------------------------------------------------------
--  DDL for Table TC_USERS
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_USERS" 
   (	"ID" NUMBER(*,0), 
	"NAME" VARCHAR2(128), 
	"EMAIL" VARCHAR2(128), 
	"HASHEDPASSWORD" VARCHAR2(128), 
	"SALT" VARCHAR2(128), 
	"READONLY" NUMBER(1,0) DEFAULT 0, 
	"ADMINISTRATOR" NUMBER(1,0), 
	"MAP" VARCHAR2(128), 
	"LATITUDE" FLOAT(24) DEFAULT 0, 
	"LONGITUDE" FLOAT(24) DEFAULT 0, 
	"ZOOM" NUMBER(*,0) DEFAULT 0, 
	"TWELVEHOURFORMAT" NUMBER(1,0) DEFAULT 0, 
	"ATTRIBUTES" VARCHAR2(4000), 
	"COORDINATEFORMAT" VARCHAR2(128), 
	"DISABLED" NUMBER(1,0) DEFAULT 0, 
	"EXPIRATIONTIME" TIMESTAMP (6), 
	"DEVICELIMIT" NUMBER(*,0) DEFAULT -1, 
	"TOKEN" VARCHAR2(128), 
	"USERLIMIT" NUMBER(*,0) DEFAULT 0, 
	"DEVICEREADONLY" NUMBER(1,0) DEFAULT 0, 
	"PHONE" VARCHAR2(128), 
	"LIMITCOMMANDS" NUMBER(1,0) DEFAULT 0, 
	"LOGIN" VARCHAR2(128), 
	"POILAYER" VARCHAR2(512)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_USER_ATTRIBUTE
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_USER_ATTRIBUTE" 
   (	"USERID" NUMBER(*,0), 
	"ATTRIBUTEID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_USER_CALENDAR
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_USER_CALENDAR" 
   (	"USERID" NUMBER(*,0), 
	"CALENDARID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_USER_COMMAND
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_USER_COMMAND" 
   (	"USERID" NUMBER(*,0), 
	"COMMANDID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_USER_DEVICE
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_USER_DEVICE" 
   (	"USERID" NUMBER(*,0), 
	"DEVICEID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_USER_DRIVER
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_USER_DRIVER" 
   (	"USERID" NUMBER(*,0), 
	"DRIVERID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_USER_GEOFENCE
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_USER_GEOFENCE" 
   (	"USERID" NUMBER(*,0), 
	"GEOFENCEID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_USER_GROUP
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_USER_GROUP" 
   (	"USERID" NUMBER(*,0), 
	"GROUPID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_USER_MAINTENANCE
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_USER_MAINTENANCE" 
   (	"USERID" NUMBER(*,0), 
	"MAINTENANCEID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_USER_NOTIFICATION
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_USER_NOTIFICATION" 
   (	"USERID" NUMBER(*,0), 
	"NOTIFICATIONID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table TC_USER_USER
--------------------------------------------------------

  CREATE TABLE "TRACCAR"."TC_USER_USER" 
   (	"USERID" NUMBER(*,0), 
	"MANAGEDUSERID" NUMBER(*,0)
   ) ;
REM INSERTING into TRACCAR.DATABASECHANGELOG
SET DEFINE OFF;
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-4.0-clean','author','changelog-4.0-clean',to_timestamp('11-DEC-19 05.13.16.038000000 PM','DD-MON-RR HH.MI.SSXFF AM'),1,'EXECUTED','8:878bfa4af9e7d6a5d754999728cfcdc2','createTable tableName=tc_attributes; createTable tableName=tc_calendars; createTable tableName=tc_commands; createTable tableName=tc_device_attribute; createTable tableName=tc_device_command; createTable tableName=tc_device_driver; createTable tab...',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-4.0-clean-common','author','changelog-4.0-clean',to_timestamp('11-DEC-19 05.13.16.097000000 PM','DD-MON-RR HH.MI.SSXFF AM'),2,'EXECUTED','8:1adabae3238e02ccc97cc422359a58a0','addForeignKeyConstraint baseTableName=tc_groups, constraintName=fk_groups_groupid, referencedTableName=tc_groups; addForeignKeyConstraint baseTableName=tc_user_user, constraintName=fk_user_user_manageduserid, referencedTableName=tc_users',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.3','author','changelog-3.3',to_timestamp('11-DEC-19 05.13.16.246000000 PM','DD-MON-RR HH.MI.SSXFF AM'),3,'MARK_RAN','8:269670d943b42badfeb36fdb27fad123','createTable tableName=users; addUniqueConstraint constraintName=uk_user_email, tableName=users; createTable tableName=devices; addUniqueConstraint constraintName=uk_device_uniqueid, tableName=devices; createTable tableName=user_device; addForeignK...',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.5','author','changelog-3.5',to_timestamp('11-DEC-19 05.13.16.316000000 PM','DD-MON-RR HH.MI.SSXFF AM'),4,'MARK_RAN','8:194e3ae7ec6d201917136e04a26e237b','createTable tableName=groups; createTable tableName=user_group; addForeignKeyConstraint baseTableName=user_group, constraintName=fk_user_group_userid, referencedTableName=users; addForeignKeyConstraint baseTableName=user_group, constraintName=fk_u...',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.6','author','changelog-3.6',to_timestamp('11-DEC-19 05.13.16.327000000 PM','DD-MON-RR HH.MI.SSXFF AM'),5,'MARK_RAN','8:c4575460ee92da59c306157dca031bee','createTable tableName=events; addForeignKeyConstraint baseTableName=events, constraintName=fk_event_deviceid, referencedTableName=devices; addColumn tableName=devices; createTable tableName=geofences; createTable tableName=user_geofence; addForeig...',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.7','author','changelog-3.7',to_timestamp('11-DEC-19 05.13.16.341000000 PM','DD-MON-RR HH.MI.SSXFF AM'),6,'MARK_RAN','8:9b6fc0a0e57527272e2aea93b08416ce','update tableName=devices; addForeignKeyConstraint baseTableName=devices, constraintName=fk_device_group_groupid, referencedTableName=groups; update tableName=groups; addColumn tableName=devices; dropColumn columnName=motion, tableName=devices; dro...',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.7-notmssql','author','changelog-3.7',to_timestamp('11-DEC-19 05.13.16.369000000 PM','DD-MON-RR HH.MI.SSXFF AM'),7,'MARK_RAN','8:974c33d2fb399ef6477c3897450fb078','addForeignKeyConstraint baseTableName=groups, constraintName=fk_group_group_groupid, referencedTableName=groups',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.8','author','changelog-3.8',to_timestamp('11-DEC-19 05.13.16.387000000 PM','DD-MON-RR HH.MI.SSXFF AM'),8,'MARK_RAN','8:b0dd5e21bde2540e95e5f0e14a2c333e','createTable tableName=attribute_aliases; addForeignKeyConstraint baseTableName=attribute_aliases, constraintName=fk_attribute_aliases_deviceid, referencedTableName=devices; addUniqueConstraint constraintName=uk_deviceid_attribute, tableName=attrib...',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.9','author','changelog-3.9',to_timestamp('11-DEC-19 05.13.16.410000000 PM','DD-MON-RR HH.MI.SSXFF AM'),9,'MARK_RAN','8:34822842d65deb843a7d16f857d52ecc','addColumn tableName=notifications; update tableName=notifications; update tableName=notifications; update tableName=notifications; update tableName=notifications',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.10','author','changelog-3.10',to_timestamp('11-DEC-19 05.13.16.431000000 PM','DD-MON-RR HH.MI.SSXFF AM'),10,'MARK_RAN','8:e1ddbe83e1ecf856a912755fc118f82e','createTable tableName=calendars; createTable tableName=user_calendar; addForeignKeyConstraint baseTableName=user_calendar, constraintName=fk_user_calendar_userid, referencedTableName=users; addForeignKeyConstraint baseTableName=user_calendar, cons...',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.10-notmssql','author','changelog-3.10',to_timestamp('11-DEC-19 05.13.16.461000000 PM','DD-MON-RR HH.MI.SSXFF AM'),11,'MARK_RAN','8:191c21d8f0f921845cf93bbc9d0639b9','addForeignKeyConstraint baseTableName=user_user, constraintName=fk_user_user_manageduserid, referencedTableName=users',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.10-mssql','author','changelog-3.10',to_timestamp('11-DEC-19 05.13.16.487000000 PM','DD-MON-RR HH.MI.SSXFF AM'),12,'MARK_RAN','8:ad1f63566e8d08812fbf0b93a118ef6e','sql',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.7-mssql','author','changelog-3.10',to_timestamp('11-DEC-19 05.13.16.510000000 PM','DD-MON-RR HH.MI.SSXFF AM'),13,'MARK_RAN','8:127b36b9d32a9d236df51d19b18c3766','sql',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.11','author','changelog-3.11',to_timestamp('11-DEC-19 05.13.16.528000000 PM','DD-MON-RR HH.MI.SSXFF AM'),14,'MARK_RAN','8:7800f890b9706a480bd5a79b591b6ca7','addColumn tableName=users; addColumn tableName=notifications; addColumn tableName=server; addColumn tableName=server; addColumn tableName=users',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.12','author','changelog-3.12',to_timestamp('11-DEC-19 05.13.16.546000000 PM','DD-MON-RR HH.MI.SSXFF AM'),15,'MARK_RAN','8:5ce520811d626ad325a014b9fcbb1a13','addColumn tableName=statistics; createTable tableName=attributes; createTable tableName=user_attribute; addForeignKeyConstraint baseTableName=user_attribute, constraintName=fk_user_attribute_userid, referencedTableName=users; addForeignKeyConstrai...',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.12-notmssql','author','changelog-3.12',to_timestamp('11-DEC-19 05.13.16.560000000 PM','DD-MON-RR HH.MI.SSXFF AM'),16,'MARK_RAN','8:a3bf7fabcde29e106fe2f89829a76a84','dropForeignKeyConstraint baseTableName=groups, constraintName=fk_group_group_groupid; addForeignKeyConstraint baseTableName=groups, constraintName=fk_groups_groupid, referencedTableName=groups',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.12-pgsql','author','changelog-3.12',to_timestamp('11-DEC-19 05.13.16.573000000 PM','DD-MON-RR HH.MI.SSXFF AM'),17,'MARK_RAN','8:cfc881bd2dadb561aa9c1a467bc8cc1c','dropColumn columnName=data, tableName=calendars; addColumn tableName=calendars',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.14','author','changelog-3.14',to_timestamp('11-DEC-19 05.13.16.585000000 PM','DD-MON-RR HH.MI.SSXFF AM'),18,'MARK_RAN','8:1be7e6c0520f8be53ef1b099d96afba5','createTable tableName=drivers; addUniqueConstraint constraintName=uk_driver_uniqueid, tableName=drivers; createTable tableName=user_driver; addForeignKeyConstraint baseTableName=user_driver, constraintName=fk_user_driver_userid, referencedTableNam...',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.15','author','changelog-3.15',to_timestamp('11-DEC-19 05.13.16.600000000 PM','DD-MON-RR HH.MI.SSXFF AM'),19,'MARK_RAN','8:6a4d9d8ec2dde03e23604e7d2b57cbce','dropForeignKeyConstraint baseTableName=attribute_aliases, constraintName=fk_attribute_aliases_deviceid; dropUniqueConstraint constraintName=uk_deviceid_attribute, tableName=attribute_aliases; dropTable tableName=attribute_aliases; dropColumn colum...',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.16','author','changelog-3.16',to_timestamp('11-DEC-19 05.13.16.617000000 PM','DD-MON-RR HH.MI.SSXFF AM'),20,'MARK_RAN','8:b6afba2ed1432c434e77a521e9a1bc24','addColumn tableName=devices; addColumn tableName=users; addColumn tableName=servers; addColumn tableName=notifications; addForeignKeyConstraint baseTableName=notifications, constraintName=fk_notification_calendarid, referencedTableName=calendars',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.3-admin','author','changelog-3.17',to_timestamp('11-DEC-19 05.13.16.641000000 PM','DD-MON-RR HH.MI.SSXFF AM'),21,'MARK_RAN','8:3f14c3b08068eb7628d0d3e2941eb2d3','renameColumn newColumnName=administrator, oldColumnName=admin, tableName=users',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-3.17','author','changelog-3.17',to_timestamp('11-DEC-19 05.13.16.656000000 PM','DD-MON-RR HH.MI.SSXFF AM'),22,'MARK_RAN','8:6eedb12278a305f013f1f7aa0f657718','addColumn tableName=events; createTable tableName=maintenances; createTable tableName=user_maintenance; addForeignKeyConstraint baseTableName=user_maintenance, constraintName=fk_user_maintenance_userid, referencedTableName=users; addForeignKeyCons...',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-4.0-pre','author','changelog-4.0',to_timestamp('11-DEC-19 05.13.16.681000000 PM','DD-MON-RR HH.MI.SSXFF AM'),23,'MARK_RAN','8:3974bfe5a2e962c0cd663433c832c16a','addColumn tableName=notifications',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-4.0-common','author','changelog-4.0',to_timestamp('11-DEC-19 05.13.16.697000000 PM','DD-MON-RR HH.MI.SSXFF AM'),24,'MARK_RAN','8:65fb49c5be37693183708351c507dd50','update tableName=notifications; update tableName=notifications; update tableName=notifications; update tableName=notifications; update tableName=notifications; update tableName=notifications; update tableName=notifications',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-4.0-pg','author','changelog-4.0',to_timestamp('11-DEC-19 05.13.16.712000000 PM','DD-MON-RR HH.MI.SSXFF AM'),25,'MARK_RAN','8:9831511507d8ae1d6759c8ccf506a27a','update tableName=notifications; update tableName=notifications; update tableName=notifications; update tableName=notifications; update tableName=notifications; update tableName=notifications; update tableName=notifications',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-4.0','author','changelog-4.0',to_timestamp('11-DEC-19 05.13.16.727000000 PM','DD-MON-RR HH.MI.SSXFF AM'),26,'MARK_RAN','8:ac63c4153f5b2ee5c7a07056da269571','dropDefaultValue columnName=web, tableName=notifications; dropColumn columnName=web, tableName=notifications; dropDefaultValue columnName=mail, tableName=notifications; dropColumn columnName=mail, tableName=notifications; dropDefaultValue columnNa...',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-4.0-renaming','author','changelog-4.0',to_timestamp('11-DEC-19 05.13.16.752000000 PM','DD-MON-RR HH.MI.SSXFF AM'),27,'MARK_RAN','8:90aedfa378aa717f8d8ae541f97b87b2','renameTable newTableName=tc_attributes, oldTableName=attributes; renameTable newTableName=tc_calendars, oldTableName=calendars; renameTable newTableName=tc_commands, oldTableName=commands; renameTable newTableName=tc_device_attribute, oldTableName...',null,null,'3.8.0',null,null,'6066393072');
Insert into TRACCAR.DATABASECHANGELOG (ID,AUTHOR,FILENAME,DATEEXECUTED,ORDEREXECUTED,EXECTYPE,MD5SUM,DESCRIPTION,COMMENTS,TAG,LIQUIBASE,CONTEXTS,LABELS,DEPLOYMENT_ID) values ('changelog-4.1-mssql','author','changelog-4.1',to_timestamp('11-DEC-19 05.13.16.762000000 PM','DD-MON-RR HH.MI.SSXFF AM'),28,'MARK_RAN','8:b148f52efe9c6a3e74a56e33e257a3e2','sql; sql; sql; sql',null,null,'3.8.0',null,null,'6066393072');
REM INSERTING into TRACCAR.DATABASECHANGELOGLOCK
SET DEFINE OFF;
Insert into TRACCAR.DATABASECHANGELOGLOCK (ID,LOCKED,LOCKGRANTED,LOCKEDBY) values (1,0,null,null);
REM INSERTING into TRACCAR.TC_ATTRIBUTES
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_CALENDARS
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_COMMANDS
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_DEVICES
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_DEVICE_ATTRIBUTE
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_DEVICE_COMMAND
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_DEVICE_DRIVER
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_DEVICE_GEOFENCE
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_DEVICE_MAINTENANCE
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_DEVICE_NOTIFICATION
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_DRIVERS
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_EVENTS
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_GEOFENCES
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_GROUPS
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_GROUP_ATTRIBUTE
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_GROUP_COMMAND
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_GROUP_DRIVER
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_GROUP_GEOFENCE
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_GROUP_MAINTENANCE
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_GROUP_NOTIFICATION
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_MAINTENANCES
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_NOTIFICATIONS
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_POSITIONS
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_SERVERS
SET DEFINE OFF;
Insert into TRACCAR.TC_SERVERS (ID,REGISTRATION,LATITUDE,LONGITUDE,ZOOM,MAP,BINGKEY,MAPURL,READONLY,TWELVEHOURFORMAT,ATTRIBUTES,FORCESETTINGS,COORDINATEFORMAT,DEVICEREADONLY,LIMITCOMMANDS,POILAYER) values (0,1,0,0,0,null,null,null,0,0,null,0,null,0,0,null);
REM INSERTING into TRACCAR.TC_STATISTICS
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_USERS
SET DEFINE OFF;
Insert into TRACCAR.TC_USERS (ID,NAME,EMAIL,HASHEDPASSWORD,SALT,READONLY,ADMINISTRATOR,MAP,LATITUDE,LONGITUDE,ZOOM,TWELVEHOURFORMAT,ATTRIBUTES,COORDINATEFORMAT,DISABLED,EXPIRATIONTIME,DEVICELIMIT,TOKEN,USERLIMIT,DEVICEREADONLY,PHONE,LIMITCOMMANDS,LOGIN,POILAYER) values (1,'admin','admin','D33DCA55ABD4CC5BC76F2BC0B4E603FE2C6F61F4C1EF2D47','000000000000000000000000000000000000000000000000',1,1,null,0,0,0,0,null,null,0,null,-1,null,0,0,null,0,null,null);
REM INSERTING into TRACCAR.TC_USER_ATTRIBUTE
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_USER_CALENDAR
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_USER_COMMAND
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_USER_DEVICE
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_USER_DRIVER
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_USER_GEOFENCE
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_USER_GROUP
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_USER_MAINTENANCE
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_USER_NOTIFICATION
SET DEFINE OFF;
REM INSERTING into TRACCAR.TC_USER_USER
SET DEFINE OFF;
--------------------------------------------------------
--  DDL for Index PK_DATABASECHANGELOGLOCK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."PK_DATABASECHANGELOGLOCK" ON "TRACCAR"."DATABASECHANGELOGLOCK" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_TC_ATTRIBUTES
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."PK_TC_ATTRIBUTES" ON "TRACCAR"."TC_ATTRIBUTES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_TC_CALENDARS
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."PK_TC_CALENDARS" ON "TRACCAR"."TC_CALENDARS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_TC_COMMANDS
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."PK_TC_COMMANDS" ON "TRACCAR"."TC_COMMANDS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_TC_DEVICES
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."PK_TC_DEVICES" ON "TRACCAR"."TC_DEVICES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_TC_DRIVERS
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."PK_TC_DRIVERS" ON "TRACCAR"."TC_DRIVERS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_TC_EVENTS
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."PK_TC_EVENTS" ON "TRACCAR"."TC_EVENTS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_TC_GEOFENCES
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."PK_TC_GEOFENCES" ON "TRACCAR"."TC_GEOFENCES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_TC_GROUPS
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."PK_TC_GROUPS" ON "TRACCAR"."TC_GROUPS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_TC_MAINTENANCES
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."PK_TC_MAINTENANCES" ON "TRACCAR"."TC_MAINTENANCES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_TC_NOTIFICATIONS
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."PK_TC_NOTIFICATIONS" ON "TRACCAR"."TC_NOTIFICATIONS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_TC_POSITIONS
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."PK_TC_POSITIONS" ON "TRACCAR"."TC_POSITIONS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_TC_SERVERS
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."PK_TC_SERVERS" ON "TRACCAR"."TC_SERVERS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_TC_STATISTICS
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."PK_TC_STATISTICS" ON "TRACCAR"."TC_STATISTICS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_TC_USERS
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."PK_TC_USERS" ON "TRACCAR"."TC_USERS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008532
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."SYS_C008532" ON "TRACCAR"."TC_DEVICES" ("UNIQUEID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008538
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."SYS_C008538" ON "TRACCAR"."TC_DRIVERS" ("UNIQUEID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008637
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRACCAR"."SYS_C008637" ON "TRACCAR"."TC_USERS" ("EMAIL") 
  ;
--------------------------------------------------------
--  Constraints for Table DATABASECHANGELOG
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."DATABASECHANGELOG" MODIFY ("EXECTYPE" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."DATABASECHANGELOG" MODIFY ("ORDEREXECUTED" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."DATABASECHANGELOG" MODIFY ("DATEEXECUTED" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."DATABASECHANGELOG" MODIFY ("FILENAME" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."DATABASECHANGELOG" MODIFY ("AUTHOR" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."DATABASECHANGELOG" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DATABASECHANGELOGLOCK
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."DATABASECHANGELOGLOCK" ADD CONSTRAINT "PK_DATABASECHANGELOGLOCK" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TRACCAR"."DATABASECHANGELOGLOCK" MODIFY ("LOCKED" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."DATABASECHANGELOGLOCK" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_ATTRIBUTES
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_ATTRIBUTES" ADD CONSTRAINT "PK_TC_ATTRIBUTES" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_ATTRIBUTES" MODIFY ("EXPRESSION" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_ATTRIBUTES" MODIFY ("ATTRIBUTE" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_ATTRIBUTES" MODIFY ("TYPE" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_ATTRIBUTES" MODIFY ("DESCRIPTION" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_ATTRIBUTES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_CALENDARS
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_CALENDARS" ADD CONSTRAINT "PK_TC_CALENDARS" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_CALENDARS" MODIFY ("ATTRIBUTES" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_CALENDARS" MODIFY ("DATA" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_CALENDARS" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_CALENDARS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_COMMANDS
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_COMMANDS" ADD CONSTRAINT "PK_TC_COMMANDS" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_COMMANDS" MODIFY ("ATTRIBUTES" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_COMMANDS" MODIFY ("TEXTCHANNEL" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_COMMANDS" MODIFY ("TYPE" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_COMMANDS" MODIFY ("DESCRIPTION" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_COMMANDS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_DEVICES
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_DEVICES" ADD UNIQUE ("UNIQUEID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_DEVICES" ADD CONSTRAINT "PK_TC_DEVICES" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_DEVICES" MODIFY ("UNIQUEID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_DEVICES" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_DEVICES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_DEVICE_ATTRIBUTE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_DEVICE_ATTRIBUTE" MODIFY ("ATTRIBUTEID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_DEVICE_ATTRIBUTE" MODIFY ("DEVICEID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_DEVICE_COMMAND
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_DEVICE_COMMAND" MODIFY ("COMMANDID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_DEVICE_COMMAND" MODIFY ("DEVICEID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_DEVICE_DRIVER
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_DEVICE_DRIVER" MODIFY ("DRIVERID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_DEVICE_DRIVER" MODIFY ("DEVICEID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_DEVICE_GEOFENCE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_DEVICE_GEOFENCE" MODIFY ("GEOFENCEID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_DEVICE_GEOFENCE" MODIFY ("DEVICEID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_DEVICE_MAINTENANCE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_DEVICE_MAINTENANCE" MODIFY ("MAINTENANCEID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_DEVICE_MAINTENANCE" MODIFY ("DEVICEID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_DEVICE_NOTIFICATION
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_DEVICE_NOTIFICATION" MODIFY ("NOTIFICATIONID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_DEVICE_NOTIFICATION" MODIFY ("DEVICEID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_DRIVERS
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_DRIVERS" ADD UNIQUE ("UNIQUEID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_DRIVERS" ADD CONSTRAINT "PK_TC_DRIVERS" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_DRIVERS" MODIFY ("ATTRIBUTES" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_DRIVERS" MODIFY ("UNIQUEID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_DRIVERS" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_DRIVERS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_EVENTS
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_EVENTS" ADD CONSTRAINT "PK_TC_EVENTS" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_EVENTS" MODIFY ("SERVERTIME" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_EVENTS" MODIFY ("TYPE" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_EVENTS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_GEOFENCES
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GEOFENCES" ADD CONSTRAINT "PK_TC_GEOFENCES" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_GEOFENCES" MODIFY ("AREA" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_GEOFENCES" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_GEOFENCES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_GROUPS
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GROUPS" ADD CONSTRAINT "PK_TC_GROUPS" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_GROUPS" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_GROUPS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_GROUP_ATTRIBUTE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GROUP_ATTRIBUTE" MODIFY ("ATTRIBUTEID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_GROUP_ATTRIBUTE" MODIFY ("GROUPID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_GROUP_COMMAND
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GROUP_COMMAND" MODIFY ("COMMANDID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_GROUP_COMMAND" MODIFY ("GROUPID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_GROUP_DRIVER
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GROUP_DRIVER" MODIFY ("DRIVERID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_GROUP_DRIVER" MODIFY ("GROUPID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_GROUP_GEOFENCE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GROUP_GEOFENCE" MODIFY ("GEOFENCEID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_GROUP_GEOFENCE" MODIFY ("GROUPID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_GROUP_MAINTENANCE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GROUP_MAINTENANCE" MODIFY ("MAINTENANCEID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_GROUP_MAINTENANCE" MODIFY ("GROUPID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_GROUP_NOTIFICATION
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GROUP_NOTIFICATION" MODIFY ("NOTIFICATIONID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_GROUP_NOTIFICATION" MODIFY ("GROUPID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_MAINTENANCES
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_MAINTENANCES" ADD CONSTRAINT "PK_TC_MAINTENANCES" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_MAINTENANCES" MODIFY ("ATTRIBUTES" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_MAINTENANCES" MODIFY ("PERIOD" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_MAINTENANCES" MODIFY ("start" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_MAINTENANCES" MODIFY ("TYPE" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_MAINTENANCES" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_MAINTENANCES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_NOTIFICATIONS
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_NOTIFICATIONS" ADD CONSTRAINT "PK_TC_NOTIFICATIONS" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_NOTIFICATIONS" MODIFY ("ALWAYS" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_NOTIFICATIONS" MODIFY ("TYPE" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_NOTIFICATIONS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_POSITIONS
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_POSITIONS" ADD CONSTRAINT "PK_TC_POSITIONS" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_POSITIONS" MODIFY ("ACCURACY" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_POSITIONS" MODIFY ("COURSE" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_POSITIONS" MODIFY ("SPEED" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_POSITIONS" MODIFY ("ALTITUDE" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_POSITIONS" MODIFY ("LONGITUDE" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_POSITIONS" MODIFY ("LATITUDE" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_POSITIONS" MODIFY ("VALID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_POSITIONS" MODIFY ("FIXTIME" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_POSITIONS" MODIFY ("DEVICETIME" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_POSITIONS" MODIFY ("SERVERTIME" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_POSITIONS" MODIFY ("DEVICEID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_POSITIONS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_SERVERS
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_SERVERS" ADD CONSTRAINT "PK_TC_SERVERS" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_SERVERS" MODIFY ("FORCESETTINGS" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_SERVERS" MODIFY ("TWELVEHOURFORMAT" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_SERVERS" MODIFY ("READONLY" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_SERVERS" MODIFY ("ZOOM" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_SERVERS" MODIFY ("LONGITUDE" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_SERVERS" MODIFY ("LATITUDE" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_SERVERS" MODIFY ("REGISTRATION" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_SERVERS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_STATISTICS
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_STATISTICS" ADD CONSTRAINT "PK_TC_STATISTICS" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_STATISTICS" MODIFY ("GEOLOCATIONREQUESTS" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_STATISTICS" MODIFY ("GEOCODERREQUESTS" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_STATISTICS" MODIFY ("SMSSENT" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_STATISTICS" MODIFY ("MAILSENT" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_STATISTICS" MODIFY ("ATTRIBUTES" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_STATISTICS" MODIFY ("MESSAGESSTORED" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_STATISTICS" MODIFY ("MESSAGESRECEIVED" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_STATISTICS" MODIFY ("REQUESTS" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_STATISTICS" MODIFY ("ACTIVEDEVICES" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_STATISTICS" MODIFY ("ACTIVEUSERS" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_STATISTICS" MODIFY ("CAPTURETIME" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_STATISTICS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_USERS
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USERS" ADD UNIQUE ("EMAIL") ENABLE;
  ALTER TABLE "TRACCAR"."TC_USERS" ADD CONSTRAINT "PK_TC_USERS" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TRACCAR"."TC_USERS" MODIFY ("TWELVEHOURFORMAT" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USERS" MODIFY ("ZOOM" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USERS" MODIFY ("LONGITUDE" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USERS" MODIFY ("LATITUDE" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USERS" MODIFY ("READONLY" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USERS" MODIFY ("EMAIL" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USERS" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USERS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_USER_ATTRIBUTE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_ATTRIBUTE" MODIFY ("ATTRIBUTEID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USER_ATTRIBUTE" MODIFY ("USERID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_USER_CALENDAR
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_CALENDAR" MODIFY ("CALENDARID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USER_CALENDAR" MODIFY ("USERID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_USER_COMMAND
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_COMMAND" MODIFY ("COMMANDID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USER_COMMAND" MODIFY ("USERID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_USER_DEVICE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_DEVICE" MODIFY ("DEVICEID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USER_DEVICE" MODIFY ("USERID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_USER_DRIVER
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_DRIVER" MODIFY ("DRIVERID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USER_DRIVER" MODIFY ("USERID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_USER_GEOFENCE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_GEOFENCE" MODIFY ("GEOFENCEID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USER_GEOFENCE" MODIFY ("USERID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_USER_GROUP
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_GROUP" MODIFY ("GROUPID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USER_GROUP" MODIFY ("USERID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_USER_MAINTENANCE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_MAINTENANCE" MODIFY ("MAINTENANCEID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USER_MAINTENANCE" MODIFY ("USERID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_USER_NOTIFICATION
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_NOTIFICATION" MODIFY ("NOTIFICATIONID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USER_NOTIFICATION" MODIFY ("USERID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TC_USER_USER
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_USER" MODIFY ("MANAGEDUSERID" NOT NULL ENABLE);
  ALTER TABLE "TRACCAR"."TC_USER_USER" MODIFY ("USERID" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table TC_DEVICES
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_DEVICES" ADD CONSTRAINT "FK_DEVICES_GROUPID" FOREIGN KEY ("GROUPID")
	  REFERENCES "TRACCAR"."TC_GROUPS" ("ID") ON DELETE SET NULL ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_DEVICE_ATTRIBUTE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_DEVICE_ATTRIBUTE" ADD CONSTRAINT "FK_USER_DEVICE_ATTRIBUTE" FOREIGN KEY ("DEVICEID")
	  REFERENCES "TRACCAR"."TC_DEVICES" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_DEVICE_ATTRIBUTE" ADD CONSTRAINT "FK_USER_DEVICE_ATTRIBUTEID" FOREIGN KEY ("ATTRIBUTEID")
	  REFERENCES "TRACCAR"."TC_ATTRIBUTES" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_DEVICE_COMMAND
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_DEVICE_COMMAND" ADD CONSTRAINT "FK_DEVICE_COMMAND_COMMANDID" FOREIGN KEY ("COMMANDID")
	  REFERENCES "TRACCAR"."TC_COMMANDS" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_DEVICE_COMMAND" ADD CONSTRAINT "FK_DEVICE_COMMAND_DEVICEID" FOREIGN KEY ("DEVICEID")
	  REFERENCES "TRACCAR"."TC_DEVICES" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_DEVICE_DRIVER
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_DEVICE_DRIVER" ADD CONSTRAINT "FK_DEVICE_DRIVER_DEVICEID" FOREIGN KEY ("DEVICEID")
	  REFERENCES "TRACCAR"."TC_DEVICES" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_DEVICE_DRIVER" ADD CONSTRAINT "FK_DEVICE_DRIVER_DRIVERID" FOREIGN KEY ("DRIVERID")
	  REFERENCES "TRACCAR"."TC_DRIVERS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_DEVICE_GEOFENCE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_DEVICE_GEOFENCE" ADD CONSTRAINT "FK_DEVICE_GEOFENCE_DEVICEID" FOREIGN KEY ("DEVICEID")
	  REFERENCES "TRACCAR"."TC_DEVICES" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_DEVICE_GEOFENCE" ADD CONSTRAINT "FK_DEVICE_GEOFENCE_GEOFENCEID" FOREIGN KEY ("GEOFENCEID")
	  REFERENCES "TRACCAR"."TC_GEOFENCES" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_DEVICE_MAINTENANCE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_DEVICE_MAINTENANCE" ADD CONSTRAINT "FK_DEVICE_MAINTENANCEID" FOREIGN KEY ("MAINTENANCEID")
	  REFERENCES "TRACCAR"."TC_MAINTENANCES" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_DEVICE_MAINTENANCE" ADD CONSTRAINT "FK_DEVICE_MAINTENANCE_DEVICEID" FOREIGN KEY ("DEVICEID")
	  REFERENCES "TRACCAR"."TC_DEVICES" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_DEVICE_NOTIFICATION
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_DEVICE_NOTIFICATION" ADD CONSTRAINT "FK_DEVICE_NOTIFICATION" FOREIGN KEY ("DEVICEID")
	  REFERENCES "TRACCAR"."TC_DEVICES" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_DEVICE_NOTIFICATION" ADD CONSTRAINT "FK_DEVICE_NOTIFICATION_" FOREIGN KEY ("NOTIFICATIONID")
	  REFERENCES "TRACCAR"."TC_NOTIFICATIONS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_EVENTS
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_EVENTS" ADD CONSTRAINT "FK_EVENTS_DEVICEID" FOREIGN KEY ("DEVICEID")
	  REFERENCES "TRACCAR"."TC_DEVICES" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_GEOFENCES
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GEOFENCES" ADD CONSTRAINT "FK_GEOFENCE_CALENDAR_" FOREIGN KEY ("CALENDARID")
	  REFERENCES "TRACCAR"."TC_CALENDARS" ("ID") ON DELETE SET NULL ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_GROUPS
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GROUPS" ADD CONSTRAINT "FK_GROUPS_GROUPID" FOREIGN KEY ("GROUPID")
	  REFERENCES "TRACCAR"."TC_GROUPS" ("ID") ON DELETE SET NULL ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_GROUP_ATTRIBUTE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GROUP_ATTRIBUTE" ADD CONSTRAINT "FK_GROUP_ATTRIBUTE_" FOREIGN KEY ("ATTRIBUTEID")
	  REFERENCES "TRACCAR"."TC_ATTRIBUTES" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_GROUP_ATTRIBUTE" ADD CONSTRAINT "FK_GROUP_ATTRIBUTE_GROUPID" FOREIGN KEY ("GROUPID")
	  REFERENCES "TRACCAR"."TC_GROUPS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_GROUP_COMMAND
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GROUP_COMMAND" ADD CONSTRAINT "FK_GROUP_COMMAND_COMMANDID" FOREIGN KEY ("COMMANDID")
	  REFERENCES "TRACCAR"."TC_COMMANDS" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_GROUP_COMMAND" ADD CONSTRAINT "FK_GROUP_COMMAND_GROUPID" FOREIGN KEY ("GROUPID")
	  REFERENCES "TRACCAR"."TC_GROUPS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_GROUP_DRIVER
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GROUP_DRIVER" ADD CONSTRAINT "FK_GROUP_DRIVER_DRIVERID" FOREIGN KEY ("DRIVERID")
	  REFERENCES "TRACCAR"."TC_DRIVERS" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_GROUP_DRIVER" ADD CONSTRAINT "FK_GROUP_DRIVER_GROUPID" FOREIGN KEY ("GROUPID")
	  REFERENCES "TRACCAR"."TC_GROUPS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_GROUP_GEOFENCE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GROUP_GEOFENCE" ADD CONSTRAINT "FK_GROUP_GEOFENCEID" FOREIGN KEY ("GEOFENCEID")
	  REFERENCES "TRACCAR"."TC_GEOFENCES" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_GROUP_GEOFENCE" ADD CONSTRAINT "FK_GROUP_GEOFENCE_GROUPID" FOREIGN KEY ("GROUPID")
	  REFERENCES "TRACCAR"."TC_GROUPS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_GROUP_MAINTENANCE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GROUP_MAINTENANCE" ADD CONSTRAINT "FK_GROUP_MAINTENANCE_" FOREIGN KEY ("MAINTENANCEID")
	  REFERENCES "TRACCAR"."TC_MAINTENANCES" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_GROUP_MAINTENANCE" ADD CONSTRAINT "FK_GROUP_MAINTENANCE_GROUPID" FOREIGN KEY ("GROUPID")
	  REFERENCES "TRACCAR"."TC_GROUPS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_GROUP_NOTIFICATION
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_GROUP_NOTIFICATION" ADD CONSTRAINT "FK_GROUP_NOTIFICATION_" FOREIGN KEY ("NOTIFICATIONID")
	  REFERENCES "TRACCAR"."TC_NOTIFICATIONS" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_GROUP_NOTIFICATION" ADD CONSTRAINT "FK_GROUP_NOTIFICATION_GROUPID" FOREIGN KEY ("GROUPID")
	  REFERENCES "TRACCAR"."TC_GROUPS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_NOTIFICATIONS
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_NOTIFICATIONS" ADD CONSTRAINT "FK_NOTIFICATION_CALENDARID" FOREIGN KEY ("CALENDARID")
	  REFERENCES "TRACCAR"."TC_CALENDARS" ("ID") ON DELETE SET NULL ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_POSITIONS
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_POSITIONS" ADD CONSTRAINT "FK_POSITIONS_DEVICEID" FOREIGN KEY ("DEVICEID")
	  REFERENCES "TRACCAR"."TC_DEVICES" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_USER_ATTRIBUTE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_ATTRIBUTE" ADD CONSTRAINT "FK_USER_ATTRIBUTE_ATTRIBUTEID" FOREIGN KEY ("ATTRIBUTEID")
	  REFERENCES "TRACCAR"."TC_ATTRIBUTES" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_USER_ATTRIBUTE" ADD CONSTRAINT "FK_USER_ATTRIBUTE_USERID" FOREIGN KEY ("USERID")
	  REFERENCES "TRACCAR"."TC_USERS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_USER_CALENDAR
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_CALENDAR" ADD CONSTRAINT "FK_USER_CALENDAR_CALENDARID" FOREIGN KEY ("CALENDARID")
	  REFERENCES "TRACCAR"."TC_CALENDARS" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_USER_CALENDAR" ADD CONSTRAINT "FK_USER_CALENDAR_USERID" FOREIGN KEY ("USERID")
	  REFERENCES "TRACCAR"."TC_USERS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_USER_COMMAND
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_COMMAND" ADD CONSTRAINT "FK_USER_COMMAND_COMMANDID" FOREIGN KEY ("COMMANDID")
	  REFERENCES "TRACCAR"."TC_COMMANDS" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_USER_COMMAND" ADD CONSTRAINT "FK_USER_COMMAND_USERID" FOREIGN KEY ("USERID")
	  REFERENCES "TRACCAR"."TC_USERS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_USER_DEVICE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_DEVICE" ADD CONSTRAINT "FK_USER_DEVICE_DEVICEID" FOREIGN KEY ("DEVICEID")
	  REFERENCES "TRACCAR"."TC_DEVICES" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_USER_DEVICE" ADD CONSTRAINT "FK_USER_DEVICE_USERID" FOREIGN KEY ("USERID")
	  REFERENCES "TRACCAR"."TC_USERS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_USER_DRIVER
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_DRIVER" ADD CONSTRAINT "FK_USER_DRIVER_DRIVERID" FOREIGN KEY ("DRIVERID")
	  REFERENCES "TRACCAR"."TC_DRIVERS" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_USER_DRIVER" ADD CONSTRAINT "FK_USER_DRIVER_USERID" FOREIGN KEY ("USERID")
	  REFERENCES "TRACCAR"."TC_USERS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_USER_GEOFENCE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_GEOFENCE" ADD CONSTRAINT "FK_USER_GEOFENCEID" FOREIGN KEY ("GEOFENCEID")
	  REFERENCES "TRACCAR"."TC_GEOFENCES" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_USER_GEOFENCE" ADD CONSTRAINT "FK_USER_GEOFENCE_USERID" FOREIGN KEY ("USERID")
	  REFERENCES "TRACCAR"."TC_USERS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_USER_GROUP
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_GROUP" ADD CONSTRAINT "FK_USER_GROUP_GROUPID" FOREIGN KEY ("GROUPID")
	  REFERENCES "TRACCAR"."TC_GROUPS" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_USER_GROUP" ADD CONSTRAINT "FK_USER_GROUP_USERID" FOREIGN KEY ("USERID")
	  REFERENCES "TRACCAR"."TC_USERS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_USER_MAINTENANCE
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_MAINTENANCE" ADD CONSTRAINT "FK_USER_MAINTENANCEID" FOREIGN KEY ("MAINTENANCEID")
	  REFERENCES "TRACCAR"."TC_MAINTENANCES" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_USER_MAINTENANCE" ADD CONSTRAINT "FK_USER_MAINT_ID" FOREIGN KEY ("USERID")
	  REFERENCES "TRACCAR"."TC_USERS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_USER_NOTIFICATION
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_NOTIFICATION" ADD CONSTRAINT "FK_USER_NOTIFICATIONID1" FOREIGN KEY ("NOTIFICATIONID")
	  REFERENCES "TRACCAR"."TC_NOTIFICATIONS" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_USER_NOTIFICATION" ADD CONSTRAINT "FK_USER_NOTIFICATIONID2" FOREIGN KEY ("USERID")
	  REFERENCES "TRACCAR"."TC_USERS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TC_USER_USER
--------------------------------------------------------

  ALTER TABLE "TRACCAR"."TC_USER_USER" ADD CONSTRAINT "FK_USER_USER_MANAGEDUSERID" FOREIGN KEY ("MANAGEDUSERID")
	  REFERENCES "TRACCAR"."TC_USERS" ("ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRACCAR"."TC_USER_USER" ADD CONSTRAINT "FK_USER_USER_USERID" FOREIGN KEY ("USERID")
	  REFERENCES "TRACCAR"."TC_USERS" ("ID") ON DELETE CASCADE ENABLE;


--------------------------------------------------------
--  Ref AUTO KEY GENERATIONS
--------------------------------------------------------
---# AUTO INCREAMENT OF ID FIELD IN DIFFERENT TABLES like 

--DATABASECHANGELOG,DATABASECHANGELOGLOCK,TC_ATTRIBUTES,TC_CALENDARS
----TC_COMMANDS,TC_DEVICES,TC_DRIVERS,TC_EVENTS,TC_GEOFENCES,TC_GROUPS
---TC_MAINTENANCES,TC_NOTIFICATIONS,TC_POSITIONS,TC_SERVERS,TC_STATISTICS
---TC_USERS
-------------------------------
-- SEQUENCE CREATION ---

CREATE SEQUENCE SEQ_DATABASECHANGELOG  START WITH 1  INCREMENT BY 1  CACHE 100;
CREATE SEQUENCE SEQ_DATABASECHANGELOGLOCK  START WITH 1  INCREMENT BY 1  CACHE 100;
CREATE SEQUENCE  SEQ_TC_ATTRIBUTES  START WITH 1  INCREMENT BY 1  CACHE 100;
CREATE SEQUENCE  SEQ_TC_CALENDARS  START WITH 1  INCREMENT BY 1  CACHE 100;
CREATE SEQUENCE  SEQ_TC_COMMANDS  START WITH 1  INCREMENT BY 1  CACHE 100;
CREATE SEQUENCE  SEQ_TC_DEVICES  START WITH 1  INCREMENT BY 1  CACHE 100;
CREATE SEQUENCE  SEQ_TC_DRIVERS  START WITH 1  INCREMENT BY 1  CACHE 100;
CREATE SEQUENCE  SEQ_TC_EVENTS  START WITH 1  INCREMENT BY 1  CACHE 500;
CREATE SEQUENCE  SEQ_TC_GEOFENCES  START WITH 1  INCREMENT BY 1  CACHE 100;
CREATE SEQUENCE  SEQ_TC_GROUPS  START WITH 1  INCREMENT BY 1  CACHE 100;
CREATE SEQUENCE  SEQ_TC_MAINTENANCES  START WITH 1  INCREMENT BY 1  CACHE 100;
CREATE SEQUENCE  SEQ_TC_NOTIFICATIONS  START WITH 1  INCREMENT BY 1  CACHE 500;
CREATE SEQUENCE  SEQ_TC_POSITIONS  START WITH 1  INCREMENT BY 1  CACHE 1000;
CREATE SEQUENCE  SEQ_TC_SERVERS  START WITH 1  INCREMENT BY 1  CACHE 100;
CREATE SEQUENCE  SEQ_TC_STATISTICS  START WITH 1  INCREMENT BY 1  CACHE 300;
CREATE SEQUENCE  SEQ_TC_USERS  START WITH 1  INCREMENT BY 1  CACHE 100;

--# TRIGGERS CREATION ---
--------------------------------------------------------
--  DDL for Trigger TRIG_DATABASECHANGELOG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRACCAR"."TRIG_DATABASECHANGELOG" 
BEFORE INSERT ON DATABASECHANGELOG
 FOR EACH ROW
DECLARE
DbMax number;
BEGIN
  IF( :new.id IS NULL )
  THEN
    :new.id := SEQ_DATABASECHANGELOG.nextval;
  END IF;
  
  EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
 Select nvl(max(id),0) into DbMax from DATABASECHANGELOG;
    while (DbMax > SEQ_DATABASECHANGELOG.nextval)
        loop
        null;
        end loop;
         :new.id := SEQ_DATABASECHANGELOG.nextval;
         
END;

/
ALTER TRIGGER "TRACCAR"."TRIG_DATABASECHANGELOG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRIG_DATABASECHANGELOGLOCK
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRACCAR"."TRIG_DATABASECHANGELOGLOCK" 
 BEFORE INSERT ON DATABASECHANGELOGLOCK
 FOR EACH ROW
DECLARE
DbMax number;
BEGIN
  IF( :new.id IS NULL )
  THEN
    :new.id := SEQ_DATABASECHANGELOGLOCK.nextval;
  END IF;
  
   EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
 Select nvl(max(id),0) into DbMax from DATABASECHANGELOGLOCK;
    while (DbMax > SEQ_DATABASECHANGELOGLOCK.nextval)
        loop
        null;
        end loop;
         :new.id := SEQ_DATABASECHANGELOGLOCK.nextval;
END;

/
ALTER TRIGGER "TRACCAR"."TRIG_DATABASECHANGELOGLOCK" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRIG_TC_ATTRIBUTES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRACCAR"."TRIG_TC_ATTRIBUTES" 
BEFORE INSERT ON TC_ATTRIBUTES
  FOR EACH ROW
DECLARE
DbMax number;
BEGIN
  IF( :new.id IS NULL )
  THEN
    :new.id := SEQ_TC_ATTRIBUTES.nextval;
  END IF;
  
 EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
 Select nvl(max(id),0) into DbMax from TC_ATTRIBUTES;
    while (DbMax > SEQ_TC_ATTRIBUTES.nextval)
        loop
        null;
        end loop;
         :new.id := SEQ_TC_ATTRIBUTES.nextval;
END;

/
ALTER TRIGGER "TRACCAR"."TRIG_TC_ATTRIBUTES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRIG_TC_CALENDARS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRACCAR"."TRIG_TC_CALENDARS" 
BEFORE INSERT ON TC_CALENDARS
  FOR EACH ROW
DECLARE
DbMax number;
BEGIN
  IF( :new.id IS NULL )
  THEN
    :new.id := SEQ_TC_CALENDARS.nextval;
  END IF;
  
   EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
 Select nvl(max(id),0) into DbMax from TC_CALENDARS;
    while (DbMax > SEQ_TC_CALENDARS.nextval)
        loop
        null;
        end loop;
         :new.id := SEQ_TC_CALENDARS.nextval;
END;

/
ALTER TRIGGER "TRACCAR"."TRIG_TC_CALENDARS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRIG_TC_COMMANDS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRACCAR"."TRIG_TC_COMMANDS" 
 BEFORE INSERT ON TC_COMMANDS
  FOR EACH ROW
DECLARE
DbMax number;
BEGIN
  IF( :new.id IS NULL )
  THEN
    :new.id := SEQ_TC_COMMANDS.nextval;
  END IF;
  
     EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
 Select nvl(max(id),0) into DbMax from TC_COMMANDS;
    while (DbMax > SEQ_TC_COMMANDS.nextval)
        loop
        null;
        end loop;
         :new.id := SEQ_TC_COMMANDS.nextval;
         
END;

/
ALTER TRIGGER "TRACCAR"."TRIG_TC_COMMANDS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRIG_TC_DEVICES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRACCAR"."TRIG_TC_DEVICES" 
 BEFORE INSERT ON TC_DEVICES
  FOR EACH ROW
DECLARE
DbMax number;
BEGIN
  IF( :new.id IS NULL )
  THEN
    :new.id := SEQ_TC_DEVICES.nextval;
  END IF;
       EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
 Select nvl(max(id),0) into DbMax from TC_DEVICES;
    while (DbMax > SEQ_TC_DEVICES.nextval)
        loop
        null;
        end loop;
         :new.id := SEQ_TC_DEVICES.nextval;
         
END;

/
ALTER TRIGGER "TRACCAR"."TRIG_TC_DEVICES" ENABLE;

--------------------------------------------------------
--  DDL for Trigger TRIG_TC_EVENTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRACCAR"."TRIG_TC_EVENTS" 
BEFORE INSERT ON TC_EVENTS
  FOR EACH ROW
DECLARE
DbMax number;
BEGIN
  IF( :new.id IS NULL )
  THEN
    :new.id := SEQ_TC_EVENTS.nextval;
  END IF;
  
EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
 Select nvl(max(id),0) into DbMax from TC_EVENTS;
    while (DbMax > SEQ_TC_EVENTS.nextval)
        loop
        null;
        end loop;
         :new.id := SEQ_TC_EVENTS.nextval;
END;

/
ALTER TRIGGER "TRACCAR"."TRIG_TC_EVENTS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRIG_TC_GEOFENCES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRACCAR"."TRIG_TC_GEOFENCES" 
BEFORE INSERT ON TC_GEOFENCES
  FOR EACH ROW
DECLARE
DbMax number;
BEGIN
  IF( :new.id IS NULL )
  THEN
    :new.id := SEQ_TC_GEOFENCES.nextval;
  END IF;
  
EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
 Select nvl(max(id),0) into DbMax from TC_GEOFENCES;
    while (DbMax > SEQ_TC_GEOFENCES.nextval)
        loop
        null;
        end loop;
         :new.id := SEQ_TC_GEOFENCES.nextval;  
END;

/
ALTER TRIGGER "TRACCAR"."TRIG_TC_GEOFENCES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRIG_TC_GROUPS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRACCAR"."TRIG_TC_GROUPS" 
BEFORE INSERT ON TC_GROUPS
  FOR EACH ROW
DECLARE
DbMax number;
BEGIN
  IF( :new.id IS NULL )
  THEN
    :new.id := SEQ_TC_GROUPS.nextval;
  END IF;
  
EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
 Select nvl(max(id),0) into DbMax from TC_GROUPS;
    while (DbMax > SEQ_TC_GROUPS.nextval)
        loop
        null;
        end loop;
         :new.id := SEQ_TC_GROUPS.nextval;   
END;

/
ALTER TRIGGER "TRACCAR"."TRIG_TC_GROUPS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRIG_TC_MAINTENANCES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRACCAR"."TRIG_TC_MAINTENANCES" 
BEFORE INSERT ON TC_MAINTENANCES
  FOR EACH ROW
DECLARE
DbMax number;
BEGIN
  IF( :new.id IS NULL )
  THEN
    :new.id := SEQ_TC_MAINTENANCES.nextval;
  END IF;
  
EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
 Select nvl(max(id),0) into DbMax from TC_MAINTENANCES;
    while (DbMax > SEQ_TC_MAINTENANCES.nextval)
        loop
        null;
        end loop;
         :new.id := SEQ_TC_MAINTENANCES.nextval;     
  
END;

/
ALTER TRIGGER "TRACCAR"."TRIG_TC_MAINTENANCES" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRIG_TC_NOTIFICATIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRACCAR"."TRIG_TC_NOTIFICATIONS" 
 BEFORE INSERT ON TC_NOTIFICATIONS
  FOR EACH ROW
DECLARE
DbMax number;
BEGIN
  IF( :new.id IS NULL )
  THEN
    :new.id := SEQ_TC_NOTIFICATIONS.nextval;
  END IF;

EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
 Select nvl(max(id),0) into DbMax from TC_NOTIFICATIONS;
    while (DbMax > SEQ_TC_NOTIFICATIONS.nextval)
        loop
        null;
        end loop;
         :new.id := SEQ_TC_NOTIFICATIONS.nextval;    
END;

/
ALTER TRIGGER "TRACCAR"."TRIG_TC_NOTIFICATIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRIG_TC_POSITIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRACCAR"."TRIG_TC_POSITIONS" 
 BEFORE INSERT ON TC_POSITIONS
  FOR EACH ROW
DECLARE
DbMax number;
BEGIN
  IF( :new.id IS NULL )
  THEN
    :new.id := SEQ_TC_POSITIONS.nextval;
  END IF;

EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
 Select nvl(max(id),0) into DbMax from TC_POSITIONS;
    while (DbMax > SEQ_TC_POSITIONS.nextval)
        loop
        null;
        end loop;
         :new.id := SEQ_TC_POSITIONS.nextval;    
         
END;

/
ALTER TRIGGER "TRACCAR"."TRIG_TC_POSITIONS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRIG_TC_SERVERS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRACCAR"."TRIG_TC_SERVERS" 
 BEFORE INSERT ON TC_SERVERS
  FOR EACH ROW
DECLARE
DbMax number;
BEGIN
  IF( :new.id IS NULL )
  THEN
    :new.id := SEQ_TC_SERVERS.nextval;
  END IF;

EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
 Select nvl(max(id),0) into DbMax from TC_SERVERS;
    while (DbMax > SEQ_TC_SERVERS.nextval)
        loop
        null;
        end loop;
         :new.id := SEQ_TC_SERVERS.nextval;    
END;

/
ALTER TRIGGER "TRACCAR"."TRIG_TC_SERVERS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRIG_TC_STATISTICS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRACCAR"."TRIG_TC_STATISTICS" 
 BEFORE INSERT ON TC_STATISTICS
  FOR EACH ROW
DECLARE
DbMax number;
BEGIN
  IF( :new.id IS NULL )
  THEN
    :new.id := SEQ_TC_STATISTICS.nextval;
  END IF;
  
EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
 Select nvl(max(id),0) into DbMax from TC_STATISTICS;
    while (DbMax > SEQ_TC_STATISTICS.nextval)
        loop
        null;
        end loop;
         :new.id := SEQ_TC_STATISTICS.nextval;   
END;

/
ALTER TRIGGER "TRACCAR"."TRIG_TC_STATISTICS" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRIG_TC_USERS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TRACCAR"."TRIG_TC_USERS" 
 BEFORE INSERT ON TC_USERS
  FOR EACH ROW
DECLARE
DbMax number;
BEGIN
  IF( :new.id IS NULL )
  THEN
    :new.id := SEQ_TC_USERS.nextval;
  END IF;
  
EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
 Select nvl(max(id),0) into DbMax from TC_USERS;
    while (DbMax > SEQ_TC_USERS.nextval)
        loop
        null;
        end loop;
         :new.id := SEQ_TC_USERS.nextval;    
END;

/
ALTER TRIGGER "TRACCAR"."TRIG_TC_USERS" ENABLE;


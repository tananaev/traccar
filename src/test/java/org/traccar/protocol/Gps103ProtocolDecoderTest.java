package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;
import org.traccar.model.Position;

public class Gps103ProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        Gps103ProtocolDecoder decoder = new Gps103ProtocolDecoder(null);

        verifyPosition(decoder, text(
                "imei:864035050002451,tracker,201223064947,,F,064947,A,1935.70640,N,09859.94436,W,0.025,;"));

        verifyPosition(decoder, text(
                "imei:760112011448012,001,2001151918,,F,191833.000,A,6136.6174,N,2126.9901,E,0.00,202.6,-0.1,1,,,,20;"));

        verifyAttribute(decoder, text(
                "imei:868683023212255,tracker,190205084503,,F,064459.000,A,4915.1221,N,01634.5655,E,3.91,83.95;"),
                "course", 83.95);

        verifyPosition(decoder, text(
                "imei:864180034124375,vt14,190116192753,,F,172750.000,A,3649.2186,N,00235.8411,W,0.00,0,,0,0,51.93%,,+22;"));

        verifyNull(decoder, text(
                "imei:864180034124375,vr,0c00fa011ea05a03d726977103ad0034c98ef49e6d303fffd1c8361303f2dbb0fa530d8ca3930be3e94f4110145c7029a507a0a00028f4a70514c05c500503170334b400531971cad002ab7634f001a4027949c8e541ea47f853bca2f961ba427ab1e290089197711c485e4f6e82ad0d1ee8f25573eed4af60284f6935bb7ef2307f1dc3f4355bbf3f90a607ffd2e5fcc6c60310be80f152dbca54ed53147fed3ae7fa1fe54809e45330fdedc993d073b7f2a6340a00d8cabeb9a4980c68900e5cb1f6e29aab9e00fc334012a5a5c32e5227c7b21a9a2d35dc02cdb49edb189fe545ec058fec7565e667cfa08b1fccd34686e4f12003dd7ffaf4b980957440bd595beb53269bb082a517dd570693604cb6299cb1663f5a9d608e3e42807d71cd002d206028011e50aa49381513494011349cd2092803ffd3d031f154ef11bc86d870781f8679a90302540adc545c55a00e28c8a602e47ad2e46680133cd19e6800cd1cf4a0063293ce695188e0d20265e58559305c3ffcb2948eca10d20278adb50518890c40f5ed9a5fece941ccb29cfd19ff00c2a6e860d6471c7da1cffb3b507eb9aaafa3dc4a4909b7fdf6c9fd28b88fffd4ca5d06e7b94152af87a53f7a7403d949a8e60265f0eaff0014ee7e8807f5a99340857abcadf881fd2973013a6936f1f48d4ffbc01fe95652dd63fba00fa521926d1de8e07a51600dc3d01d00;"));

        verifyPosition(decoder, text(
                "imei:868683026321020,T:+11,181217080050,,F,080047.000,A,3227.3057,N,11649.4754,W,0.00,0,,0,0,0.00%,,+11;"));

        verifyAttribute(decoder, text(
                "imei:868683026321020,tracker,181217080106,,F,080102.000,A,3227.3057,N,11649.4754,W,0.00,0,,0,0,0.00%,0,+11;"),
                Position.PREFIX_TEMP + 1, 11);

        verifyPosition(decoder, text(
                "imei:861359038609986,Equipo 1,---,------,----,214734,241018,26,1,-33.42317,-70.61930,067,229,0674,1.00,08,0,1,---,*"));

        verifyPosition(decoder, text(
                "imei:861359038609986,Equipo 1,---,------,----,214812,241018,14,0,-33.42317,-70.61930,000,000,0000,99.9,00,0,1,---,*"));

        verifyNull(decoder, text(
                "imei:123451234512345,L,*"));

        verifyAttributes(decoder, text(
                "imei:868683027758113,OBD,180905200218,,,,0,0,0.39%,70,9.41%,494,0.00,P0137,P0430,,;"));

        verifyAttribute(decoder, text(
                "imei:353451044508750,001,0809231929,13554900601,F,055403.000,A,2233.1870,N,11354.3067,E,0.00,30.1,65.43,1,0,10.5%,0.0%,28;"),
                "fuel1", 10.5);

        verifyPosition(decoder, text(
                "imei:864180036029895,acc on,180508145653,,F,065645.000,A,4729.1497,N,01904.2342,E,0.00,0,,1,,0.00%,,;"));

        verifyNotNull(decoder, text(
                "imei:864895030279986,ac alarm,180404174252,,L,,,296a,,51f7,,,"));

        verifyAttributes(decoder, text(
                "imei:359710048977327,OBD,180301094003,5000000,0.00,0.00,98,18,68.63%,55,25.10%,1368,14.24,,,,;"));

        verifyAttributes(decoder, text(
                "imei:862106025092216,OBD,170605095949,195874,,370.8,808,066,30.0%,+87,13.0%,02444,14.3,,,,;"));

        verifyAttributes(decoder, text(
                "imei:868683027825532,OBD,170613203014,,,,0,0,0.00%,0,0.00%,0,0.00,,,,;"));

        verifyAttributes(decoder, text(
                "imei:862106025092216,OBD,170612165656,196043,,145803.9,,000,0.0%,+,0.0%,00000,12.6,,,,;"));

        verifyAttributes(decoder, text(
                "imei:862106025092216,OBD,170605095949,195874,,370.8,808,066,30.0%,+87,13.0%,02444,14.3,,,,;"));

        verifyPosition(decoder, text(
                "imei:353451044508750,DTC,0809231929,,F,055403.000,A,2233.1870,N,11354.3067,E,0.00,30.1,,1,0,10.5%,P0021,;"));

        verifyPosition(decoder, text(
                "imei:353451044508750,oil1,0809231929,,F,055403.000,A,2233.1870,N,11354.3067,E,0.00,,,,51.6,41.7,;"));

        verifyPosition(decoder, text(
                "imei:353451044508750,oil2,0809231929,,F,055403.000,A,2233.1870,N,11354.3067,E,0.00,,,,51.6,41.7,;"));

        verifyPosition(decoder, text(
                "imei:353451044508750,oil 51.67,0809231929,,F,055403.000,A,2233.1870,N,11354.3067,E,0.00,,;"));

        verifyPosition(decoder, text(
                "imei:353451044508750,T:+28.0,0809231929,,F,055403.000,A,2233.1870,N,11354.3067,E,0.00,,;"));

        verifyPosition(decoder, text(
                "imei:353451044508750,bonnet alarm,0809231929,,F,055403.000,A,2233.1870,N,11354.3067,E,0.00,,;"));

        verifyPosition(decoder, text(
                "imei:353451044508750,footbrake alarm,0809231929,,F,055403.000,A,2233.1870,N,11354.3067,E,0.00,,;"));

        verifyPosition(decoder, text(
                "imei:862106021237716,ac alarm,1611291645,,F,204457.000,A,1010.2783,N,06441.0274,W,0.00,,;"));

        verifyAttributes(decoder, text(
                "imei:359710049057798,OBD,161003192752,1785,,,0,54,96.47%,75,20.00%,1892,0.00,P0134,P0571,,;"));

        verifyAttributes(decoder, text(
                "imei:359710049090138,OBD,160629022949,51442,0.00,15.88,5632,122,40.39%,95,0.00%,2101,13.80,,,,;"));

        verifyPosition(decoder, text(
                "imei:359710049090138,tracker,160629022948,,F,182949.000,A,4043.8839,N,11328.8029,W,65.26,271.82,,1,0,31.37%,51442,;"));

        verifyAttributes(decoder, text(
                "imei:359710049042014,001,160615040011,,F,040011.000,A,2833.0957,N,07711.9465,E,0.01,215.33,,0,,,,;"));

        verifyAttributes(decoder, text(
                "imei:359710049028435,OBD,160316053657,70430,,,0,49,60.00%,46,19.22%,859,0.00,U1108,,,;"));

        verifyPosition(decoder, text(
                "359769031878322imei:359769031878322,tracker,1602160718,2,F,221811.000,A,1655.2193,S,14546.6722,E,0.00,,"));

        verifyNull(decoder, text(
                "imei:865328021049167,OBD,141118115036,,,0.0,,000,0.0%,+,0.0%,00000,,,,,"));

        verifyAttributes(decoder, text(
                "imei:359710049032874,OBD,160208152900,13555,,,45,0,24.71%,35,13.73%,1230,14.13,U1108,,,"));

        verifyAttributes(decoder, text(
                "imei:359710049064398,OBD,160101035156,17887,0.00,17.06,0,0,0.00%,0,0.00%,16383,10.82,,,,"));

        verifyPosition(decoder, text(
                "imei:868683020235846,rfid,160202091347,49121185,F,011344.000,A,0447.7273,N,07538.9934,W,0.00,0,,0,0,0.00%,,"));

        verifyNotNull(decoder, text(
                "imei:359710049075097,help me,,,L,,,113b,,558f,,,,,0,0,,,"));

        verifyNotNull(decoder, text(
                "imei:359710041100000,tracker,000000000,,L,,,fa8,,c9af,,,,,0,0,0.00%,,"));

        verifyNotNull(decoder, text(
                "imei:863070016871385,tracker,0000000119,,L,,,0FB6,,CB5D,,,"));

        verifyPosition(decoder, text(
                "imei:359710045559474,tracker,151030080103,,F,000101.000,A,5443.3834,N,02512.9071,E,0.00,0;"),
                position("2015-10-30 00:01:01.000", true, 54.72306, 25.21512));

        verifyPosition(decoder, text(
                "imei:359710049092324,tracker,151027025958,,F,235957.000,A,2429.5156,N,04424.5828,E,0.01,27.91,,0,0,,,;"),
                position("2015-10-26 23:59:57.000", true, 24.49193, 44.40971));

        verifyPosition(decoder, text(
                "imei:865328021058861,tracker,151027041419,,F,011531.000,A,6020.2979,N,02506.1940,E,0.49,113.30,,0,0,0.0%,,;"),
                position("2015-10-27 01:15:31.000", true, 60.33830, 25.10323));

        // Log on request
        verifyNull(decoder, text(
                "##,imei:359586015829802,A"));

        // Heartbeat package
        verifyNull(decoder, text(
                "359586015829802"));

        // No GPS signal
        verifyNull(decoder, text(
                "imei:359586015829802,tracker,000000000,13554900601,L,;"));

        verifyPosition(decoder, text(
                "imei:869039001186913,tracker,1308282156,0,F,215630.000,A,5602.11015,N,9246.30767,E,1.4,,175.9,"));

        verifyPosition(decoder, text(
                "imei:359710040656622,tracker,13/02/27 23:40,,F,125952.000,A,3450.9430,S,13828.6753,E,0.00,0"));
        
        verifyPosition(decoder, text(
                "imei:359710040565419,tracker,13/05/25 14:23,,F,062209.000,A,0626.0411,N,10149.3904,E,0.00,0"));

        verifyPosition(decoder, text(
                "imei:353451047570260,tracker,1302110948,,F,144807.000,A,0805.6615,S,07859.9763,W,0.00,,"));
        
        verifyPosition(decoder, text(
                "imei:359587016817564,tracker,1301251602,,F,080251.000,A,3223.5832,N,11058.9449,W,0.03,"));
        
        verifyPosition(decoder, text(
                "imei:359587016817564,tracker,1301251602,,F,080251.000,A,3223.5832,N,11058.9449,W,,"));

        verifyPosition(decoder, text(
                "imei:012497000208821,tracker,1301080525,,F,212511.000,A,2228.5279,S,06855.6328,W,18.62,268.98,"));

        verifyPosition(decoder, text(
                "imei:012497000208821,tracker,1301072224,,F,142411.077,A,2227.0739,S,06855.2912,,0,0,"));

        verifyPosition(decoder, text(
                "imei:012497000431811,tracker,1210260609,,F,220925.000,A,0845.5500,N,07024.7673,W,0.00,,"));

        verifyPosition(decoder, text(
                "imei:100000000000000,help me,1004171910,,F,010203.000,A,0102.0003,N,00102.0003,E,1.02,"));

        verifyPosition(decoder, text(
                "imei:353451040164707,tracker,1105182344,+36304665439,F,214418.000,A,4804.2222,N,01916.7593,E,0.37,"));

        verifyPosition(decoder, text(
                "imei:353451042861763,tracker,1106132241,,F,144114.000,A,2301.9052,S,04909.3676,W,0.13,"));

        verifyPosition(decoder, text(
                "imei:359587010124900,tracker,0809231929,13554900601,F,112909.397,A,2234.4669,N,11354.3287,E,0.11,321.53,"));

        verifyPosition(decoder, text(
                "imei:353451049926460,tracker,1208042043,123456 99008026,F,124336.000,A,3509.8668,N,03322.7636,E,0.00,,"));

        // SOS alarm
        verifyPosition(decoder, text(
                "imei:359586015829802,help me,0809231429,13554900601,F,062947.294,A,2234.4026,N,11354.3277,E,0.00,"));

        // Low battery alarm
        verifyPosition(decoder, text(
                "imei:359586015829802,low battery,0809231429,13554900601,F,062947.294,A,2234.4026,N,11354.3277,E,0.00,"));

        // Geo-fence alarm
        verifyPosition(decoder, text(
                "imei:359586015829802,stockade,0809231429,13554900601,F,062947.294,A,2234.4026,N,11354.3277,E,0.00,"));

        // Move alarm
        verifyPosition(decoder, text(
                "imei:359586015829802,move,0809231429,13554900601,F,062947.294,A,2234.4026,N,11354.3277,E,0.00,"));

        // Over speed alarm
        verifyPosition(decoder, text(
                "imei:359586015829802,speed,0809231429,13554900601,F,062947.294,A,2234.4026,N,11354.3277,E,0.00,"));

        verifyPosition(decoder, text(
                "imei:863070010423167,tracker,1211051840,,F,104000.000,A,2220.6483,N,11407.6377,,0,0,"));

        verifyPosition(decoder, text(
                "imei:863070010423167,tracker,1211051951,63360926,F,115123.000,A,2220.6322,N,11407.5313,E,0.00,,"));

        verifyPosition(decoder, text(
                "imei:863070010423167,tracker,1211060621,,F,062152.000,A,2220.6914,N,11407.5506,E,15.85,347.84,"));
        
        verifyPosition(decoder, text(
                "imei:863070012698733,tracker,1303092334,,F,193427.000,A,5139.0369,N,03907.2791,E,0.00,,"));
        
        verifyPosition(decoder, text(
                "imei:869039001186913,tracker,130925065533,0,F,065533.000,A,5604.11015,N,9232.12238,E,0.0,,329.0,"));
        
        verifyPosition(decoder, text(
                "imei:359710041641581,acc alarm,1402231159,,F,065907.000,A,2456.2591,N,06708.8335,E,7.53,76.10,,1,0,0.03%,,"));
        
        verifyPosition(decoder, text(
                "imei:359710041641581,acc alarm,1402231159,,F,065907.000,A,2456.2591,N,06708.8335,E,7.53,76.10,,1,0,0.03%,,"));
        
        verifyPosition(decoder, text(
                "imei:313009071131684,tracker,1403211928,,F,112817.000,A,0610.1133,N,00116.5840,E,0.00,,,0,0,0.0,0.0,"));
        
        verifyPosition(decoder, text(
                "imei:866989771979791,tracker,140527055653,,F,215653.00,A,5050.33113,N,00336.98783,E,0.066,0"));
        
        verifyPosition(decoder, text(
                "imei:353552045375005,tracker,150401165832,61.0,F,31.0,A,1050.73696,N,10636.49489,E,8.0,,22.0,"));
        
        verifyPosition(decoder, text(
                "imei:353552045403597,tracker,150420050648,53.0,F,0.0,A,N,5306.64155,E,00700.77848,0.0,,1.0,;"));
        
        verifyPosition(decoder, text(
                "imei:353552045403597,tracker,150420051153,53.0,F,0.0,A,5306.64155,N,00700.77848,E,0.0,,1.0,;"));
        
        verifyPosition(decoder, text(
                "imei:359710047424644,tracker,150506224036,,F,154037.000,A,0335.2785,N,09841.1543,E,3.03,337.54,,0,0,45.16%,,;"));
        
        verifyPosition(decoder, text(
                "imei:865328023776874,acc off,150619152221,,F,072218.000,A,5439.8489,N,02518.5945,E,0.00,,,1,1,0.0,0.0,23.0,;"));

    }

}

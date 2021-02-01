package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;

public class XirgoProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecodeCustom() throws Exception {

        XirgoProtocolDecoder decoder = new XirgoProtocolDecoder(null);

        decoder.setForm("UID,EV,D,T,LT,LN,AL,GSPT,HD,SV,HP,BV,CQ,GS,SI,IG,OT");

        verifyNull(decoder, text(
                "$$184800619,6115,Y1z1.1179AA2.3.7c79d34,,,000##"));

        verifyPosition(decoder, text(
                "$$183900034,4002,03/30/2019,02:15:22,46.848577,-114.022213,978,0.0,172.3,16,1.2,13.291,20,3,2,2,1##"));

        verifyPosition(decoder, text(
                "$$184800793,4002,03/30/2019,02:10:13,46.848600,-114.022256,9723,0.0,1645,17,1.2,13.283,18,3,89011703278246523594,2,0##"));

        decoder.setForm("UID,EV,D,T,LT,LN,AL,GSPT,SV,HP,BV,CQ,MI,GS,SI,IG,OT");

        verifyPosition(decoder, text(
                "$$184800793,4002,03/15/2019,21:30:21,46.848582,-114.022237,9733,0.0,18,1.1,13.605,20,0,3,89011703278246523602,2,0##"));

        /* 2169X, vehicle off and parked. */
        decoder.setForm("UID,EV,D;T,LT,LN,AL,GSPT,HD,SV,HP,BV,CQ,MI,GS,GT,AC,DC,IN2S,IG");

        verifyPosition(decoder, text(
                "$$184801398,4006,01/26/2021;20:38:41,44262439,-88508944,265,0,1327,14,15,12461,12,31952,3,0,0,0,0,2,235##"));
    }

    @Test
    public void testDecodeNew() throws Exception {

        XirgoProtocolDecoder decoder = new XirgoProtocolDecoder(null);

        verifyPosition(decoder, text(
                "$$352054058132185,4001,2017/04/21,00:01:05,32.54659,-116.90670,143.2,0,0,0,598,0.0,12,0.9,765840,7.0,14.5,19,1,1,0011,8.5,63.2,5,21999,184,255,671,207,100,185##"));

        verifyPosition(decoder, text(
                "$$352054058132185,6011,2017/04/21,04:57:10,32.49658,-116.85957,250.9,0,0,0,602,0.0,12,0.8,765876,7.0,14.1,21,1,1,0011,10.1,0.0,5,170917890,280,255,627,0,100,167##"));

        verifyPosition(decoder, text(
                "$$355922061611345,6001,2016/08/25,20:10:51,51.13042,-114.22752,1197,44.7,0.0,0.0,2622,27,12,0.8,1,0.0,13.9,24,1,0,0.0,-70,-809,688##"));

        verifyPosition(decoder, text(
                "$$355922061611345,6001,2016/08/25,20:10:38,51.12948,-114.22637,1203,34.8,0.0,0.0,1377,215,12,0.8,1,0.0,13.8,28,1,0,0.0,-309,-566,754##"));

        verifyPosition(decoder, text(
                "$$354898045650537,6031,2015/02/26,15:47:26,33.42552,-112.30308,287.8,0,0,0,0,0.0,7,1.2,2,0.0,12.2,22,1,0,82.3"));

        verifyPosition(decoder, text(
                "$$355922060162167,6015,2016/04/21,17:26:52,39.83267,-76.66139,230,0.0,0.0,0.0,779,0,8,1.2,0,0.0,13.0,19,1,1C4BJWDG4GL191009,X0z1-1137CD1,0402,3GATT,0,83.9,-70,-715,738##"));

        verifyPosition(decoder, text(
                "$$355922060162167,4002,2016/04/21,17:04:50,39.83253,-76.66102,232,0.0,0.0,0.0,0,0,12,1.2,0,0.0,9.2,15,1,0,0.0,35,-8,1059##"));

    }

    @Test
    public void testDecodeOld() throws Exception {

        XirgoProtocolDecoder decoder = new XirgoProtocolDecoder(null);

        verifyPosition(decoder, text(
                "$$354660046140722,6001,2013/01/22,15:36:18,25.80907,-80.32531,7.1,19,165.2,11,0.8,11.1,17,1,1,3.9,2##"),
                position("2013-01-22 15:36:18.000", true, 25.80907, -80.32531));

        verifyPosition(decoder, text(
                "$$357207059646786,4003,2015/05/19,15:54:56,-20.21422,-70.14927,37.5,1.8,0.0,11,0.8,12.9,31,297,1,0,0.0,0.0,0,1,1,1##"));

        verifyPosition(decoder, text(
                "$$354898045650537,6031,2015/02/26,15:47:26,33.42552,-112.30308,287.8,0,0,0,0,0.0,7,1.2,2,0.0,12.2,22,1,0,82.3"));

        verifyPosition(decoder, text(
                "$$357207059646786,4003,2015/05/19,15:55:27,-20.21421,-70.14920,33.6,0.4,0.0,11,0.8,12.9,31,297,1,0,0.0,0.0,0,1,1,1##"));

    }

}

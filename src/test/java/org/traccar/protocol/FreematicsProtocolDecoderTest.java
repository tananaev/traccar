package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;

public class FreematicsProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        FreematicsProtocolDecoder decoder = new FreematicsProtocolDecoder(null);

        verifyPositions(decoder, text(
                "M0ZR4X0#0:204391,11:140221,10:8445000,A:49.215920,B:18.737755,C:410,D:0,E:208,24:1252,20:0;0;0,82:47*B5"));

        verifyNull(decoder, text(
                "1#EV=2,TS=1871902,ID=ESP32305C06C40A24*AC"));

        verifyNull(decoder, text(
                "0#EV=1,TS=23930,ID=ID1C6606C40A24,SK=TEST_SERVER_KEY*49"));

        verifyPositions(decoder, text(
                "1#0:102560,20:0;0;0,24:425,10:4285580,A:-35.803696,B:175.748413,C:0.22,D:0.41,F:5,0:103174,20:0;0;0,24:423,10:4285660,A:-35.803696,B:175.748413,C:0.22,D:0.41,F:5,30:88193792*21"));

        verifyPositions(decoder, text(
                "1#0:49244,20:0;0;0,24:423,0:50779,20:0;0;0,24:425,30:32924444*38"));

        verifyNotNull(decoder, text(
                "1#0:47607,20:0;0;0,24:423,0:48732,20:0;0;0,24:428,10:4280140,A:0.000000,B:0.000000,C:0.00,D:18520000.00,F:2,30:32924444*BA"));

        verifyPositions(decoder, text(
                "1#0:68338,10D:79,30:1010,105:199,10C:4375,104:56,111:62,20:0;-1;95,10:6454200,A:-32.727482,B:150.150301,C:159,D:0,F:5,24:1250*7A"));

        verifyPositions(decoder, text(
                "1#0=68338,10D=79,30=1010,105=199,10C=4375,104=56,111=62,20=0;-1;95,10=6454200,A=-32.727482,B=150.150301,C=159,D=0,F=5,24=1250*7A"));

        verifyPositions(decoder, false, text(
                "M0ZR4X0#0:566624,24:1246,20:0;0;0*D"));

        verifyNull(decoder, text(
                "M0ZR4X0#DF=4208,SSI=-71,EV=1,TS=20866,ID=M0ZR4X0*9E"));

    }

}

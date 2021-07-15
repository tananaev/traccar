package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;

public class VisiontekProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        var decoder = new VisiontekProtocolDecoder(null);

        verifyPosition(decoder, text(
                "$1,117,28,01,16,15,05,48,1725.0518N,07824.5298E,0620,11,0,185,2062,0,0,0,1,1,1,1,24,00.0000,00.3740,00.0000,VAJRA V1.00,A"),
                position("2016-01-28 15:05:48.000", true, 17.417530, 78.408830));

        verifyPosition(decoder, text(
                "$1,VMC,358072044271838,26,10,15,10,43,20,17.066418N,080.395667E,000.0,285,00.8,0074,6390,0,0,0,0,0,0,0,0,00.00,00.00,00,00,0000,12.7,4.0,24,10,0000000000000,A,0"));

        verifyNull(decoder, text(
                "$1,VMC,358072044271838,25,10,15,09,19,40,00.0000000,000.0000000,000.0,000,00.0,0000,6070,0,0,0,0,0,0,0,0,00.00,00.00,00,00,0000,12.5,4.0,99,00,0000000000000,V,0"));

        verifyPosition(decoder, text(
                "$1,AP116,05,06,15,11,48,32,1725.0460N,07824.5289E,0617,07,0,030,2091,0,0,0,1,1,1,1,20,00.0000,00.3820,00.0000,VAJRA V1.00,A"),
                position("2015-06-05 11:48:32.000", true, 17.417433, 78.408815));

        verifyPosition(decoder, text(
                "$1,AP09BU9397,861785006462448,20,06,14,15,03,28,17267339N,078279407E,060.0,073,0550,11,0,1,0,0,1,1,26,A,0000000000"),
                position("2014-06-20 15:03:28.000", true, 17.44556, 78.46567));

        verifyNull(decoder, text(
                "$1,AP09BU9397,861785006462448,20,06,14,15,03,28,000000000,0000000000,000.0,000,0000,00,0,1,0,0,1,1,24,V,0000000000"));
        
        verifyNull(decoder, text(
                "$1,1234567890,02,06,11,17,07,45,00000000,000000000,00.0,0,0,V"));

        verifyPosition(decoder, text(
                "$1,1234567890,02,06,11,17,07,45,17267690N,078279340E,060.0,113,0,A"));

    }

}

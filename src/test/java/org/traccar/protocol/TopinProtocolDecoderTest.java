package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;

public class TopinProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        TopinProtocolDecoder decoder = new TopinProtocolDecoder(null);

        verifyNull(decoder, binary(
                "787801080D0A"));

        verifyNull(decoder, binary(
                "78780d0103593390754169634d0d0a"));

        verifyPosition(decoder, binary(
                "78781510120B05030D2498038077200BE2078F0034000102030D0A"));

        verifyPosition(decoder, binary(
                "7878200813081A0733211608C8D1710DED1D1608DFFB710E06D51039050100286489000D0A"));

        verifyPosition(decoder, binary(
                "78782008140709121f36300d769f02058cfd300d771202058c6f0000000300005c99000d0a"));

        verifyPosition(decoder, binary(
                "787812100A03170F32179C026B3F3E0C22AD651F34600D0A"));

        verifyAttributes(decoder, binary(
                "78780a132827010063000000000d0a"));

        verifyNotNull(decoder, binary(
                "7878001719111120141807019456465111aa3c465111ab464651c1a550465106b150465342f750465342f65a465111a95a000d0a"));

        verifyPosition(decoder, binary(
                "787812100a03170f32179c026b3f3e0c22ad651f34600d0a"));

        verifyAttributes(decoder, binary(
                "78780713514d0819640d0a"));

        verifyNull(decoder, binary(
                "787801300d0a"));

    }

}

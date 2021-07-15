package org.traccar.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Random;

import org.junit.Test;
import org.traccar.Context;
import org.traccar.ProtocolTest;
import org.traccar.model.Position;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class FifotrackProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        var decoder = new FifotrackProtocolDecoder(null);

        verifyAttribute(decoder, buffer(
                "$$25,863003046473534,1,B03,OK*4D"),
                Position.KEY_RESULT, "OK");

        verifyPosition(decoder, buffer(
                "$$118,863003046473534,258,A01,,201007231735,V,3.067783,101.672858,0,176,96,189890,0,A0,03,0,502|19|5C1|93349F,196|4E0|6C,1,*13"));

        verifyPosition(decoder, buffer(
                "$$116,869270049149999,5,A01,4,190925080127,V,-15.804260,35.061506,0,0,1198,0,0,900000C0,02,0,650|10|12C|B24,18B|4C8|72,1,*01"));

        verifyAttribute(decoder, buffer(
                "$$123,869467049296388,B996,A01,2,190624131813,V,22.333746,113.590670,0,124,-1,26347,0,0004,00,0,460|0|2694|5A5D,174|0|0|0,B48CEB,*77"),
                Position.KEY_ALARM, Position.ALARM_SOS);

        verifyAttribute(decoder, buffer(
                "$$125,869467049296388,548,A01,38,190619025856,A,22.333905,113.590261,0,12,60,16666,0,0000,00,0,460|0|2694|13F8,1A2|4C1|0|0,B4A067,*7A"),
                Position.KEY_DRIVER_UNIQUE_ID, "11837543");

        byte[] expectedImage = new byte[13152];
        new Random().nextBytes(expectedImage);
        int imageSize = expectedImage.length;
        int chunkSize = 1024;

        verifyNull(decoder, buffer(
                "$$79,868345037864709,382,D05,190220085833,22.643210,114.018176,1,1,1,13152,23FFD339*25"));

        for (int i = 0; i < imageSize; i += chunkSize) {
                int end = Math.min(i + chunkSize, imageSize);
                int len = end - i;
                ByteBuf message = concatenateBuffers(
                        buffer("$$" + (39 + len) + ",868345037864709,1,D06,23FFD339,0," + len + ","),
                        Unpooled.wrappedBuffer(expectedImage, i, len),
                        buffer("*00")
                );
                if (end < imageSize) {
                        // intermediate messages
                        verifyNull(decoder, message);
                } else {
                        Position position = (Position) decoder.decode(null, null, message);
                        assertNotNull(position);
                        String imageFile = position.getString(Position.KEY_IMAGE);
                        assertEquals("868345037864709/mock.jpg", imageFile);
                        ByteBuf parsedImage = ((MockMediaManager) Context.getMediaManager()).readFile(imageFile);
                        assertEquals(Unpooled.wrappedBuffer(expectedImage), parsedImage);
                }
        }

        verifyNull(decoder, binary(
                "2424313036332c3836383334353033373836343730392c312c4430362c32343434424438362c302c313032342cffd8ffdb008400140e0f120f0d14121012171514181e32211e1c1c1e3d2c2e243249404c4b47404645505a736250556d5645466488656d777b8182814e608d978c7d96737e817c011517171e1a1e3b21213b7c5346537c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7c7cffc000110801e0028003012100021101031101ffdd0004000affc401a20000010501010101010100000000000000000102030405060708090a0b100002010303020403050504040000017d01020300041105122131410613516107227114328191a1082342b1c11552d1f02433627282090a161718191a25262728292a3435363738393a434445464748494a535455565758595a636465666768696a737475767778797a838485868788898a92939495969798999aa2a3a4a5a6a7a8a9aab2b3b4b5b6b7b8b9bac2c3c4c5c6c7c8c9cad2d3d4d5d6d7d8d9dae1e2e3e4e5e6e7e8e9eaf1f2f3f4f5f6f7f8f9fa0100030101010101010101010000000000000102030405060708090a0b1100020102040403040705040400010277000102031104052131061241510761711322328108144291a1b1c109233352f0156272d10a162434e125f11718191a262728292a35363738393a434445464748494a535455565758595a636465666768696a737475767778797a82838485868788898a92939495969798999aa2a3a4a5a6a7a8a9aab2b3b4b5b6b7b8b9bac2c3c4c5c6c7c8c9cad2d3d4d5d6d7d8d9dae2e3e4e5e6e7e8e9eaf2f3f4f5f6f7f8f9faffda000c03010002110311003f00b0148a705c8cd00479e6917ef7b5003c9ec29b90bf5a00457c366a4620806801921f41da999cf02801ebf4a4e73cf14002b153f2d4a5cb0c8506802261cf4a50b8a007053d718a4c1cf340099c526ecd007fffd07f6a55c86140126e19e69acdcd0037a9a4a002909a004eb4e030334001a4ce280141cd2138a004ed4982074e6800ed49de801698793401ffd18cf4a65002af5a4ce1a8026cf14d278a008f760d20ebf350031cf6149183bb8a009c03de901f9a801c0e78a31400b9c518a004c5140094b8a00fffd28b1462800c518a00414b400b8a00e68016814001a2800a5eb40062908cd002628a0028a00fffd3998e4734b1b7c981400c3d79a7829b7ef73e98a0069f6a4c50034a926a551b47340037a1e4d424734012c43820529001e72680060bfc34a1f6f02800618e6a3c9cd003c336304d0091d680187ad211401fffd47f34a48079a0091946327d2a173e9400a290d002f6a4c7ad00205cf4a7f3b680131c52639a00304521140098a42c68010138a00e28014034d391401fffd58c9a69e6801a3341a004dc69439140085b3da909cd001b69369cf14013019148cb40028229dcd0014b4005142a3739"));

        verifyPosition(decoder, buffer(
                "$$105,866104023179743,AB,A00,,161007085534,A,54.738791,25.271918,0,350,151,0,17929,0000,0,,246|1|65|96DB,936|0*0B"));

        verifyPosition(decoder, buffer(
                "$$103,866104023179743,5,A00,,161006192841,A,54.738791,25.271918,0,342,200,0,4265,0000,0,,246|1|65|96DB,9C4|0*75"));

        verifyPosition(decoder, buffer(
                "$$103,866104023179743,4,A00,,161006192810,V,54.738791,25.271918,0,158,122,0,4235,0000,0,,246|1|65|96DB,9C5|0*69"));

        verifyPosition(decoder, buffer(
                "$$135,866104023192332,29,A01,,160606093046,A,22.546430,114.079730,0,186,181,0,415322,0000,02,2,460|0|27B3|EA7,A2F|3B9|3|0,940C7E,31.76|30.98*46"));

    }

}

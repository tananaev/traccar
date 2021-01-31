package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;
import org.traccar.model.Position;

public class Tk103ProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        Tk103ProtocolDecoder decoder = new Tk103ProtocolDecoder(null);

        verifyPosition(decoder, text(
                "(086375304593BR00210119A2220.0160N11335.4073E0000014000309.84001000293L0000015FP23BS27F)"));

        verifyAttribute(decoder, text(
                "(027023361470BV005J6RW2H53HL066029)"),
                Position.KEY_VIN, "5J6RW2H53HL066029");

        verifyAttribute(decoder, text(
                "(044027395704BQ81,ALARM,1,164,151101A2238.5237N11349.4571E0.7031241010.0000,00000000)"),
                Position.KEY_ALARM, Position.ALARM_OVERSPEED);

        verifyPosition(decoder, text(
                "(027023361470BR00200617A4000.5775N 8415.4076W 46.0173725 87.3101000000L00000000)"));

        verifyPosition(decoder, text(
                "(BALLESTEROS3BR00190408A4113.5700N00140.3100E000.0162431000.0001000000L00000000)"));

        verifyPosition(decoder, text(
                "(094625928000BR00190213A1156.0431S07705.6145W000.000023521.40000000007L00000314T113)"));

        verifyPosition(decoder, text(
                "(019358704260BR00180725A2300.0957N07235.2748E032.412092187.58001100166L000D9779)"));

        verifyPosition(decoder, text(
                "(358511020000026,DW5B,310,6,29876,30393,0,041217,102211)"));

        verifyPosition(decoder, text(
                "(007611121184BR00170816A2401.5217N07447.0788E000.0221352232.340000004FL0030F14F)"));

        verifyNull(decoder, text(
                "(027044702512BP00027044702512HSO01A4)"));

        verifyPosition(decoder, text(
                "(864768011069660,ZC11,250517,V,0000.0000N,00000.0000E,000.0,114725,000.0,0.00,11)"));

        verifyPosition(decoder, text(
                "(864768011069660,ZC17,250517,A,3211.7118N,03452.8086E,0.68,115525,208.19,64.50,9)"));

        verifyPosition(decoder, text(
                "(357593060760397BP02,G,2,170304A6015.7466N01101.8460E001.609445591.048,7)"));

        verifyPosition(decoder, text(
                "(325031693849BR00170228A5750.8012N02700.7476E000.2154529000.0000000200L00000000,170228,194530)"));

        verifyAttribute(decoder, text(
                "(087073803649BR00170221A6142.0334N02712.2197E000.3203149000.00,00000000L00000000)"),
                Position.KEY_FUEL_LEVEL, 0);

        verifyPosition(decoder, text(
                "(864768010869060,DW30,050117,A,5135.82713N,00001.17918E,0.089,154745,000.0,43.40,12)"));

        verifyNotNull(decoder, text(
                "(087073104337BZ00,740,000,3bf7,0425,3bf7,0bf5,3bf7,09e7,3bf7,cbad,3bf7,0dcf,3bf7,c7b2,01000000)"));

        verifyNull(decoder, text(
                "(087073005534BP00HSO)"));

        verifyNull(decoder, text(
                "(027028258309BQ86,0,05550c21b10d1d0f431008bd114c0ea5078400010007a100423932,161117005322,01000001)"));

        verifyNull(decoder, text(
                "(027028258309BQ86,0,05470c0eb20d040f4410022911360e92077e00010007a1004237c7,161117005232,01000001)"));

        verifyPosition(decoder, text(
                "(01602009983BR00160830V1855.7022S4817.8731W000.0002729000.0010000000L00000000)"));

        verifyPosition(decoder, text(
                "(088046338039BR00160727A3354.7768N03540.7258E000.0140832068.4700000000L00BEB0D4+017.7)"));

        verifyPosition(decoder, text(
                "(088046338039BP05000088046338039160727A3354.7768N03540.7258E000.0140309065.1000000000L00BEB0D4+017.3)"));

        verifyAttributes(decoder, text(
                "(013632651491,ZC20,180716,144222,6,392,65535,255)"));

        verifyAttributes(decoder, text(
                "(087072009461BR00000007V0000.0000N00000.0000E000.00014039900000000L00000000)"));

        verifyPosition(decoder, text(
                "(013612345678BO012061830A2934.0133N10627.2544E040.0080331309.6200000000L000770AD)"));

        verifyNotNull(decoder, text(
                "(088047194605BZ00,510,010,36e6,932c,43,36e6,766b,36,36e6,7668,32)"));

        verifyAttributes(decoder, text(
                "(013632651491,ZC20,040613,040137,6,421,112,0)"));

        verifyAttributes(decoder, text(
                "(864768010159785,ZC20,291015,030413,3,362,65535,255)"));

        verifyPosition(decoder, text(
                "(088047365460BR00151024A2555.3531S02855.3329E004.7055148276.1701000000L00009AA3)"),
                position("2015-10-24 05:51:48.000", true, -25.92255, 28.92222));

        verifyPosition(decoder, text(
                "(088047365460BP05354188047365460150929A3258.1754S02755.4323E009.4193927301.9000000000L00000000)"));

        verifyPosition(decoder, text(
                "(088048003342BP05354188048003342150917A1352.9801N10030.9050E000.0103115265.5600010000L000003F9)"));

        verifyPosition(decoder, text(
                "(088048003342BR00150917A1352.9801N10030.9050E000.0103224000.0000010000L000003F9)"));
        
        verifyPosition(decoder, text(
                "(088048003342BR00150807A1352.9871N10030.9084E000.0110718000.0001010000L00000000)"));

        verifyNull(decoder, text(
                "(090411121854BP0000001234567890HSO)"));

        verifyPosition(decoder, text(
                "(01029131573BR00150428A3801.6382N02351.0159E000.0080729278.7800000000LEF9ECB9C)"));

        verifyPosition(decoder, text(
                "(035988863964BP05000035988863964110524A4241.7977N02318.7561E000.0123536356.5100000000L000946BB)"));

        verifyPosition(decoder, text(
                "(013632782450BP05000013632782450120803V0000.0000N00000.0000E000.0174654000.0000000000L00000000)"));

        verifyPosition(decoder, text(
                "(013666666666BP05000013666666666110925A1234.5678N01234.5678W000.002033490.00000000000L000024DE)"));
        
        verifyPosition(decoder, text(
                "(013666666666BO012110925A1234.5678N01234.5678W000.0025948118.7200000000L000024DE)"));

        verifyPosition(decoder, text(
                "(088045133878BR00130228A5124.5526N00117.7152W000.0233614352.2200000000L01B0CF1C)"));
        
        verifyPosition(decoder, text(
                "(008600410203BP05000008600410203130721A4152.5790N01239.2770E000.0145238173.870100000AL0000000)"));
        
        verifyPosition(decoder, text(
                "(013012345678BR00130515A4843.9703N01907.6211E000.019232800000000000000L00009239)"));
        
        verifyPosition(decoder, text(
                "(012345678901BP05000012345678901130520A3439.9629S05826.3504W000.1175622323.8700000000L000450AC)"));
        
        verifyPosition(decoder, text(
                "(012345678901BR00130520A3439.9629S05826.3504W000.1175622323.8700000000L000450AC)"));
        
        verifyPosition(decoder, text(
                "(352606090042050,BP05,240414,V,0000.0000N,00000.0000E,000.0,193133,000.0)"));
        
        verifyPosition(decoder, text(
                "(352606090042050,BP05,240414,A,4527.3513N,00909.9758E,4.80,112825,155.49)"),
                position("2014-04-24 11:28:25.000", true, 45.45586, 9.16626));

        verifyPosition(decoder, text(
                "(013632782450,BP05,101201,A,2234.0297N,11405.9101E,000.0,040137,178.48,00000000,L00000000)"));
        
        verifyPosition(decoder, text(
                "(864768010009188,BP05,271114,V,4012.19376N,00824.05638E,000.0,154436,000.0)"));

        verifyPosition(decoder, text(
                "(013632651491,BP05,040613,A,2234.0297N,11405.9101E,000.0,040137,178.48)"));

        verifyPosition(decoder, text(
                "(013632651491,ZC07,040613,A,2234.0297N,11405.9101E,000.0,040137,178.48)"));

        verifyPosition(decoder, text(
                "(013632651491,ZC11,040613,A,2234.0297N,11405.9101E,000.0,040137,178.48)"));

        verifyPosition(decoder, text(
                "(013632651491,ZC12,040613,A,2234.0297N,11405.9101E,000.0,040137,178.48)"));

        verifyPosition(decoder, text(
                "(013632651491,ZC13,040613,A,2234.0297N,11405.9101E,000.0,040137,178.48)"));

        verifyPosition(decoder, text(
                "(013632651491,ZC17,040613,A,2234.0297N,11405.9101E,000.0,040137,178.48)"));

        verifyPosition(decoder, text(
                "(094050000111BP05000094050000111150808A3804.2418N04616.7468E000.0201447133.3501000011L0028019DT000)"));

        verifyPosition(decoder, text(
                "(864555555555555,DW3B,131117,A,5544.02870N,01315.08194E,1.597,223707,291.65,-0.10,4)"));

        verifyPosition(decoder, text(
                "(864555555555555,DW3B,131117,A,5544.02870N,01315.08194E,1.597,223707,291.65,0.10,8)"));

        verifyPosition(decoder, text(
                "(013632651491,ZC07,040613,A,2234.0297N,11405.9101E,000.0,040137,178.48)"));

        verifyAttributes(decoder, text(
                "(013632651491,ZC20,040613,040137,6,42,112,0)"));

        verifyNotNull(decoder, text(
                "(864555555555555,DW51,200,1,3215,43370,2,58:F3:BB:3B:AA:82*-65*1,1C:6A:BB:AA:81:95*-78*1,151117,154419)"));

        verifyNotNull(decoder, text(
                "(864555555555555,DW5B,210,6,5995,47701,5,30:EE:CC:E7:86:DD*-59*11,4C:60:CC:EA:BB:EE*-68*1,42:AA:DE:EA:BB:00*-69*1,32:CD:BB:C3:4F:CC*-86*3,10:00:43:BA:22:15*-88*1,151117,163722)"));

        verifyNotNull(decoder, text(
                "(013632651491,DW50,460,0,0,6,2,aa:bb:cc:dd:ee:ff*-8*0,aa:bb:cc:dd:ee:ff*-8*0,040613,040137)"));

        verifyNotNull(decoder, text(
                "(013632651491,DW50,460,0,0,6,0,040613,040137)"));

        verifyNotNull(decoder, text(
                "(864555555555555,ZC03,191117,234207,$Notice: Device version: 1.0$)"));

        verifyNotNull(decoder, text(
                "(864555555555555,ZC03,191117,234207,$1 .Sensor sensitivity: 1\r\n2 .Alert status: Off\r\n3 .Check interval is set to 240 minute(s).\r\n4 .Checkgsm interval is set to 60 minute(s).\r\n5 .SOS SMS Alert: On\r\n6 .SOS Call Alert: On\r\n7 . Power: 95%$)"));

    }

}

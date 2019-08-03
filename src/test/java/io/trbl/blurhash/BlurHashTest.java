package io.trbl.blurhash;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class BlurHashTest {

    @Test
    public void testBlack() throws Exception {
        String blurHash = BlurHash.encode(getBufferedImage("/black.png"));
        assertEquals("U00000fQfQfQfQfQfQfQfQfQfQfQfQfQfQfQ", blurHash);
    }

    @Test
    public void test1x1() throws Exception {
        String blurHash = BlurHash.encode(getBufferedImage("/1x1.png"));
        assertEquals("U~TSUA~q~q~q~q~q~q~q~q~q~q~q~q~q~q~q", blurHash);
    }

    @Test
    public void testWhite() throws Exception {
        String blurHash = BlurHash.encode(getBufferedImage("/white.png"));
        assertEquals("U2TSUA~qfQ~q~qj[fQj[fQfQfQfQ~qj[fQj[", blurHash);
    }

    @Test
    public void testLorikeet() throws Exception {
        String blurHash = BlurHash.encode(getBufferedImage("/lorikeet.jpg"));
        assertEquals("UFDcT@_LNs#pVrIVX6Vu9]RRw[OXOZxaxWNH", blurHash);
        //    Python: UFDcT@_LNr#pVrIVX6Vu9]RRw[OYOZxaxWNH
        //                     ^                 ^
    }

    private BufferedImage getBufferedImage(String filename) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(filename)) {
            return ImageIO.read(inputStream);
        }
    }
}

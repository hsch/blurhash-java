package io.trbl.blurhash;

import java.awt.image.BufferedImage;

import static io.trbl.blurhash.Utils.*;

/**
 * Utility methods to calculate blur hashes.
 */
public final class BlurHash {

    private static void applyBasisFunction(int[] pixels, int width, int height,
                                           double normalisation, int i, int j,
                                           double[][] factors, int index) {
        double r = 0, g = 0, b = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double basis = normalisation
                               * Math.cos((Math.PI * i * x) / width)
                               * Math.cos((Math.PI * j * y) / height);
                int pixel = pixels[y * width + x];
                r += basis * sRGBToLinear((pixel >> 16) & 0xff);
                g += basis * sRGBToLinear((pixel >> 8)  & 0xff);
                b += basis * sRGBToLinear( pixel        & 0xff);
            }
        }
        double scale = 1.0 / (width * height);
        factors[index][0] = r * scale;
        factors[index][1] = g * scale;
        factors[index][2] = b * scale;
    }

    private static long encodeDC(double[] value) {
        long r = linearTosRGB(value[0]);
        long g = linearTosRGB(value[1]);
        long b = linearTosRGB(value[2]);
        return (r << 16) + (g << 8) + b;
    }

    private static long encodeAC(double[] value, double maximumValue) {
        double quantR = Math.floor(Math.max(0, Math.min(18, Math.floor(signPow(value[0] / maximumValue, 0.5) * 9 + 9.5))));
        double quantG = Math.floor(Math.max(0, Math.min(18, Math.floor(signPow(value[1] / maximumValue, 0.5) * 9 + 9.5))));
        double quantB = Math.floor(Math.max(0, Math.min(18, Math.floor(signPow(value[2] / maximumValue, 0.5) * 9 + 9.5))));
        return Math.round(quantR * 19 * 19 + quantG * 19 + quantB);
    }

    /**
     * Calculates the blur hash from the given image with 4x4 components.
     *
     * @param bufferedImage the image
     * @return the blur hash
     */
    public static String encode(BufferedImage bufferedImage) {
        return encode(bufferedImage, 4, 4);
    }

    /**
     * Calculates the blur hash from the given image.
     * @param bufferedImage the image
     * @param componentX number of components in the x dimension
     * @param componentY number of components in the y dimension
     * @return the blur hash
     */
    public static String encode(BufferedImage bufferedImage, int componentX, int componentY) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[] pixels = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
        return encode(pixels, width, height, componentX, componentY);
    }

    /**
     * Calculates the blur hash from the given pixels.
     *
     * @param pixels width * height pixels, encoded as RGB integers (0xAARRGGBB)
     * @param width width of the bitmap
     * @param height height of the bitmap
     * @param componentX number of components in the x dimension
     * @param componentY number of components in the y dimension
     * @return the blur hash
     */
    public static String encode(int[] pixels, int width, int height, int componentX, int componentY) {

        if (componentX < 1 || componentX > 9 || componentY < 1 || componentY > 9) {
            throw new IllegalArgumentException("Blur hash must have between 1 and 9 components");
        }
        if (width * height != pixels.length) {
            throw new IllegalArgumentException("Width and height must match the pixels array");
        }

        double[][] factors = new double[componentX * componentY][3];
        for (int j = 0; j < componentY; j++) {
            for (int i = 0; i < componentX; i++) {
                double normalisation = i == 0 && j == 0 ? 1 : 2;
                applyBasisFunction(pixels, width, height,
                        normalisation, i, j,
                        factors, j * componentX + i);
            }
        }

        char[] hash = new char[1 + 1 + 4 + 2 * (factors.length - 1)]; // size flag + max AC + DC + 2 * AC components

        long sizeFlag = componentX - 1 + (componentY - 1) * 9;
        Base83.encode(sizeFlag, 1, hash, 0);

        double maximumValue;
        if (factors.length > 1) {
            double actualMaximumValue = max(factors, 1, factors.length);
            double quantisedMaximumValue = Math.floor(Math.max(0, Math.min(82, Math.floor(actualMaximumValue * 166 - 0.5))));
            maximumValue = (quantisedMaximumValue + 1) / 166;
            Base83.encode(Math.round(quantisedMaximumValue), 1, hash, 1);
        } else {
            maximumValue = 1;
            Base83.encode(0, 1, hash, 1);
        }

        double[] dc = factors[0];
        Base83.encode(encodeDC(dc), 4, hash, 2);

        for (int i = 1; i < factors.length; i++) {
            Base83.encode(encodeAC(factors[i], maximumValue), 2, hash, 6 + 2 * (i - 1));
        }
        return new String(hash);
    }

    private BlurHash() {
    }
}

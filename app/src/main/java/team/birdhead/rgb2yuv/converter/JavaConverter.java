package team.birdhead.rgb2yuv.converter;

public class JavaConverter implements Converter {

    @Override
    public String getName() {
        return "Java";
    }

    @Override
    public void rgbToYuv(byte[] rgb, int width, int height, byte[] yuv) {
        int rgbIndex = 0;
        int yIndex = 0;
        int uvIndex = width * height;
        for (int j = 0; j < height; ++j) {
            for (int i = 0; i < width; ++i) {
                final int r = rgb[rgbIndex] & 0xFF;
                final int g = rgb[rgbIndex + 1] & 0xFF;
                final int b = rgb[rgbIndex + 2] & 0xFF;

                final int y = (int) (0.257 * r + 0.504 * g + 0.098 * b + 16);
                final int u = (int) (-0.148 * r - 0.291 * g + 0.439 * b + 128);
                final int v = (int) (0.439 * r - 0.368 * g - 0.071 * b + 128);

                yuv[yIndex++] = (byte) Math.max(0, Math.min(255, y));
                if ((i & 0x01) == 0 && (j & 0x01) == 0) {
                    yuv[uvIndex++] = (byte) Math.max(0, Math.min(255, v));
                    yuv[uvIndex++] = (byte) Math.max(0, Math.min(255, u));
                }

                rgbIndex += 3;
            }
        }
    }
}

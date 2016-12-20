package team.birdhead.rgb2yuv.converter;

public interface Converter {

    String getName();
    void rgbToYuv(byte[] rgb, int width, int height, byte[] yuv);
}

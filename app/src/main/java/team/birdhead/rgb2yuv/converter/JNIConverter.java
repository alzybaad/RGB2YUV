package team.birdhead.rgb2yuv.converter;

public class JNIConverter implements Converter {

    static {
        System.loadLibrary("rgb2yuv");
    }

    @Override
    public String getName() {
        return "JNI";
    }

    @Override
    public void rgbToYuv(byte[] rgb, int width, int height, byte[] yuv) {
        rgbToYuvInternal(rgb, width, height, yuv);
    }

    private native void rgbToYuvInternal(byte[] rgb, int width, int height, byte[] yuv);
}

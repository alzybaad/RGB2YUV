package team.birdhead.rgb2yuv.converter;

public class LibYuvConverter implements Converter {

    static {
        System.loadLibrary("rgb2yuv");
    }

    private byte[] mBgr;

    @Override
    public String getName() {
        return "libyuv";
    }

    @Override
    public void rgbToYuv(byte[] rgb, int width, int height, byte[] yuv) {
        if (mBgr == null || mBgr.length < rgb.length) {
            mBgr = new byte[rgb.length];
        }

        rgbToBgrInternal(rgb, width, height, mBgr);
        bgrToYuvInternal(mBgr, width, height, yuv);
    }

    private native void rgbToBgrInternal(byte[] rgb, int width, int height, byte[] bgr);
    private native void bgrToYuvInternal(byte[] bgr, int width, int height, byte[] yuv);
}

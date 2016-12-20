package team.birdhead.rgb2yuv.converter;

import android.content.Context;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.Type;

import team.birdhead.rgb2yuv.ScriptC_rgb2yuv;

public class RsConverter implements Converter {

    private final RenderScript mRenderScript;

    public RsConverter(Context context) {
        mRenderScript = RenderScript.create(context);
    }

    @Override
    public String getName() {
        return "RenderScript";
    }

    @Override
    public void rgbToYuv(byte[] rgb, int width, int height, byte[] nv21) {
        final Type.Builder inType = new Type.Builder(mRenderScript, Element.RGBA_8888(mRenderScript))
                .setX(width)
                .setY(height);

        final Type.Builder outType = new Type.Builder(mRenderScript, Element.U8(mRenderScript))
                .setX(width * height * 3 / 2);

        final Allocation inAllocation = Allocation.createTyped(mRenderScript, inType.create(), Allocation.USAGE_SCRIPT);
        final Allocation outAllocation = Allocation.createTyped(mRenderScript, outType.create(), Allocation.USAGE_SCRIPT);

        inAllocation.copyFrom(rgb);

        final ScriptC_rgb2yuv script = new ScriptC_rgb2yuv(mRenderScript);
        script.set_gOut(outAllocation);
        script.set_width(width);
        script.set_height(height);
        script.set_frameSize(width * height);
        script.forEach_convert(inAllocation);

        outAllocation.copyTo(nv21);
    }
}

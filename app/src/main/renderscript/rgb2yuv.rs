#pragma version(1)
#pragma rs java_package_name (team.birdhead.rgb2yuv)

rs_allocation gOut;
int width;
int height;
int frameSize;

void RS_KERNEL convert(uchar4 in, uint32_t x, uint32_t y) {
    uchar r = in.r;
    uchar g = in.g;
    uchar b = in.b;

    int yInt = (int) (  0.257f * r + 0.504f * g + 0.098f * b) + 16;
    int uInt = (int) (- 0.148f * r - 0.291f * g + 0.439f * b) + 128;
    int vInt = (int) (  0.439f * r - 0.368f * g - 0.071f * b) + 128;

    uchar yChar = (uchar) (yInt < 0 ? 0 : yInt > 255 ? 255 : yInt);
    uchar uChar = (uchar) (uInt < 0 ? 0 : uInt > 255 ? 255 : uInt);
    uchar vChar = (uchar) (vInt < 0 ? 0 : vInt > 255 ? 255 : vInt);

    rsSetElementAt_uchar(gOut, yChar, width * y + x);

    if ((x & 0x01) == 0 && (y & 0x01) == 0) {
        uint32_t offset = frameSize + width * (y >> 1) + x;
        rsSetElementAt_uchar(gOut, vChar, offset);
        rsSetElementAt_uchar(gOut, uChar, offset + 1);
    }
}

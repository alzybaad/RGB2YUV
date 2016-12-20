package team.birdhead.rgb2yuv.executor;

import android.util.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import team.birdhead.rgb2yuv.converter.Converter;

public class ConverterExecutor {

    private static final int COUNT = 10;

    private static final Size[] SIZES = {
            new Size(320, 180), new Size(640, 360), new Size(1280, 720), new Size(1920, 1080), new Size(3840, 2160)
    };

    public Results execute(Converter converter) {
        final Results results = new Results(converter.getName());

        for (Size size : SIZES) {
            final byte[] rgb = new byte[size.getWidth() * size.getHeight() * 4];
            final byte[] yuv = new byte[size.getWidth() * size.getHeight() * 3 / 2];

            results.setSize(size);
            for (int j = 0; j < COUNT; ++j) {
                results.onStart();
                converter.rgbToYuv(rgb, size.getWidth(), size.getHeight(), yuv);
                results.onStop();
            }
        }

        return results;
    }

    public static class Results {

        private final String mName;
        private final Map<Size, List<Long>> mTimes;
        private List<Long> mCurrentTimes;
        private long mStartMillis;

        Results(String name) {
            mName = name;
            mTimes = new HashMap<>();
        }

        void setSize(Size size) {
            mCurrentTimes = new ArrayList<>();
            mTimes.put(size, mCurrentTimes);
        }

        void onStart() {
            mStartMillis = System.currentTimeMillis();
        }

        void onStop() {
            mCurrentTimes.add(System.currentTimeMillis() - mStartMillis);
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder("[").append(mName).append("]\n");
            for (Size size : SIZES) {
                builder.append(size.toString()).append(": ").append(toString(mTimes.get(size))).append("\n");
            }
            return builder.toString();
        }

        private String toString(List<Long> times) {
            long total = 0;
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;

            for (long time : times) {
                total += time;
                min = Math.min(time, min);
                max = Math.max(time, max);
            }

            return String.format(Locale.US, "min=%d, max=%d, avg=%d", min, max, (total - (min + max)) / (times.size() - 2));
        }
    }
}

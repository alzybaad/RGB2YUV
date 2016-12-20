package team.birdhead.rgb2yuv;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import team.birdhead.rgb2yuv.converter.JNIConverter;
import team.birdhead.rgb2yuv.converter.JavaConverter;
import team.birdhead.rgb2yuv.converter.RsConverter;
import team.birdhead.rgb2yuv.executor.ConverterExecutor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        execute();
    }

    private void execute() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                ((TextView) findViewById(R.id.text)).setText(R.string.executing);
            }

            @Override
            protected String doInBackground(Void... voids) {
                final ConverterExecutor executor = new ConverterExecutor();
                final ConverterExecutor.Results java = executor.execute(new JavaConverter());
                final ConverterExecutor.Results jni = executor.execute(new JNIConverter());
                final ConverterExecutor.Results rs = executor.execute(new RsConverter(getApplicationContext()));

                return java.toString() + "\n" + jni.toString() + "\n" + rs.toString();
            }

            @Override
            protected void onPostExecute(String result) {
                ((TextView) findViewById(R.id.text)).setText(result);
            }
        }.execute();
    }
}

package yhb.dc.demo.network.webview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yhb.dc.R;
import yhb.dc.common.Demo;

public class WebViewMainActivity extends AppCompatActivity  {

    public static final String EXTRA_RECORD = "extra_record";
    private static final String TAG = "WebViewClient#Record";
    private List<Record> mRecords = new ArrayList<>();
    private boolean mReuseWebView = false;
    private boolean mStop = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private TextView mTextView;
    private volatile static boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_main_view);
        mTextView = findViewById(R.id.text_view);
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                boolean old = mReuseWebView;
                if (checkedId == R.id.radio_new) {
                    mReuseWebView = false;
                    mStop = false;
                } else if (checkedId == R.id.radio_reuse) {
                    mReuseWebView = true;
                    mStop = false;
                } else {
                    mStop = true;
                }

                if (old != mReuseWebView) {
                    mTextView.setText(
                            new StringBuilder(mTextView.getText())
                                    .append("\n")
                                    .append(getAverage(mRecords, old))
                                    .append("\n")
                    );
                    Log.i(TAG, "\n" + getAverage(mRecords, old) + "\n");
                    mRecords.clear();
                }
            }
        });

    }

    List<Object> mObjects = new ArrayList<>();

    private String getAverage(List<Record> records, boolean reuseWebView) {
        int count = records.size();
        int total = 0;
        for (Record record : records) {
            total += record.duration;
        }

        if (total == 0) {
            return "";
        }

        return "Average duration(" + (reuseWebView ? "reuse" : "new") + "): " + +(total / count) + ".";
    }


    public void onClick(View view) {
        Intent intent = WebViewActivity.newIntent(this, mReuseWebView);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            Record record = data.getParcelableExtra(EXTRA_RECORD);
            Log.i(TAG, record.toString());
            mTextView.setText(new StringBuilder(mTextView.getText()).append("\n").append(record.toString()));
            mRecords.add(record);

            if (!mStop)
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onClick(null);
                    }
                }, 1500);

        }

    }

    public void onClick0(View view) {
        if (view != null)
            flag = !flag;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flag) {
                    mObjects.add(new String("454458666666666666666666666666666646874638"));
                    onClick0(null);
                }
            }
        }, 0);
    }


    static class Record implements Parcelable {
        long start, end, duration;
        String url;

        Record(long start, long end, long duration, String url) {
            this.start = start;
            this.end = end;
            this.duration = duration;
            this.url = url;
        }

        Record(Parcel in) {
            start = in.readLong();
            end = in.readLong();
            duration = in.readLong();
            url = in.readString();
        }

        public static final Creator<Record> CREATOR = new Creator<Record>() {
            @Override
            public Record createFromParcel(Parcel in) {
                return new Record(in);
            }

            @Override
            public Record[] newArray(int size) {
                return new Record[size];
            }
        };

        @Override
        public String toString() {
            return "Record{" +
                    "start=" + start +
                    ", end=" + end +
                    ", duration=" + duration +
                    ", url='" + url + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(start);
            dest.writeLong(end);
            dest.writeLong(duration);
            dest.writeString(url);
        }


    }
}

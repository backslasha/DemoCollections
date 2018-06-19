package yhb.dc.demo.demo_view.view_fields;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import yhb.dc.R;

/**
 * Created by yhb on 18-5-6.
 */

public class FieldView extends FrameLayout {

    private Field mField;
    private TextView mTextView;
    private OnValueChangeListener mOnValueChangeListener;

    public FieldView(Context context) {
        this(context, null);
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        mOnValueChangeListener = onValueChangeListener;
        mOnValueChangeListener.onValueChange(mField);
    }

    public void setField(Field field) {
        mField = field;
    }

    public interface OnValueChangeListener {
        void onValueChange(Field field);
    }


    public FieldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.item_field_content, this, true);

        Button add = findViewById(R.id.button_add);
        Button minus = findViewById(R.id.button_minus);

        mTextView = findViewById(R.id.text_view_value);

        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mField.mValue += 10;
                invalidateValue(mField.mValue);
            }
        });

        minus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mField.mValue -= 10;
                invalidateValue(mField.mValue);
            }
        });

    }


    static class Field {
        public float mValue;

        public Field(String name, float value) {
            mValue = value;
            mName = name;
        }

        final public String mName;

    }

    private void invalidateValue(float newValue) {
        mTextView.setText(String.format("%s: %s", mField.mName, newValue));
        if (mOnValueChangeListener != null) {
            mOnValueChangeListener.onValueChange(mField);
        }
    }
}
package yhb.dc.demo.customview.view_properties;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

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

        add.setOnClickListener(v -> {
            mField.mValue += 10;
            invalidateValue(mField.mValue);
        });

        minus.setOnClickListener(v -> {
            mField.mValue -= 10;
            invalidateValue(mField.mValue);
        });

    }


    static class Field {
        float mValue;
        List<Field> mRelativeFields;

        Field(String name, float value, Field... relativeFields) {
            mValue = value;
            mName = name;
            mRelativeFields = Arrays.asList(relativeFields);
        }

        final String mName;

    }

    private void invalidateValue(float newValue) {
        mTextView.setText(String.format("%s: %s", mField.mName, newValue));
        if (mOnValueChangeListener != null) {
            mOnValueChangeListener.onValueChange(mField);
        }
    }
}

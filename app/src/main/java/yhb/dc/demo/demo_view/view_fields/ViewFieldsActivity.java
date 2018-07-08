package yhb.dc.demo.demo_view.view_fields;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import yhb.dc.R;
import yhb.dc.common.CommonAdapter;
import yhb.dc.common.CommonViewHolder;
import yhb.dc.common.Demo;

public class ViewFieldsActivity extends AppCompatActivity implements FieldView.OnValueChangeListener, Demo {

    private RecyclerView mRecyclerView;
    private ImageView mImageView;
    private RecyclerView.Adapter commonAdapter;
    private List<FieldView.Field> fields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fields);

        mImageView = findViewById(R.id.image_view);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewFieldsActivity.this, "you click me!", Toast.LENGTH_SHORT).show();
            }
        });

        fields = new ArrayList<>();

        commonAdapter = new CommonAdapter<FieldView.Field>(R.layout.item_field, fields) {
            @Override
            public void convert(CommonViewHolder holder, FieldView.Field entity) {
                FieldView fieldView = (FieldView) holder.itemView;
                fieldView.setField(entity);
                fieldView.setOnValueChangeListener(ViewFieldsActivity.this);
                ((TextView) fieldView.findViewById(R.id.text_view_value)).setText(String.format("%s: %s", entity.mName, entity.mValue));
            }
        };

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(commonAdapter);


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (fields.size() == 0) {
            fields.add(new FieldView.Field("x", mImageView.getX()));
            fields.add(new FieldView.Field("y", mImageView.getY()));
            fields.add(new FieldView.Field("scrollX", mImageView.getScrollX()));
            fields.add(new FieldView.Field("scrollY", mImageView.getScrollY()));
            fields.add(new FieldView.Field("translationX", mImageView.getTranslationX()));
            fields.add(new FieldView.Field("translationY", mImageView.getTranslationY()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fields.add(new FieldView.Field("translationZ", mImageView.getTranslationZ()));
            }
            fields.add(new FieldView.Field("left", mImageView.getLeft()));
            fields.add(new FieldView.Field("right", mImageView.getRight()));
            fields.add(new FieldView.Field("bottom", mImageView.getBottom()));
            fields.add(new FieldView.Field("top", mImageView.getTop()));
        }
        commonAdapter.notifyDataSetChanged();
    }

    @Override
    public void onValueChange(FieldView.Field field) {
        Method method = null;

        boolean isFloat = true;

        try {
            method = mImageView.getClass().getMethod(getFieldSetterName(field.mName), Float.TYPE);

        } catch (NoSuchMethodException e) {

            try {
                method = mImageView.getClass().getMethod(getFieldSetterName(field.mName), Integer.TYPE);
                isFloat = false;
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
                return;
            }
            e.printStackTrace();
        }


        if (method != null) {
            try {
                if (isFloat)
                    method.invoke(mImageView, field.mValue);
                else
                    method.invoke(mImageView, ((int) field.mValue));

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }

    }

    private String getFieldSetterName(String name) {
        if (name == null || name.length() == 0) {
            return "";
        }
        return "set" + String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1);
    }

}

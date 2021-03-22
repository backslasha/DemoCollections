package yhb.dc.demo.customview.view_properties;

import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import yhb.dc.R;
import yhb.dc.common.CommonAdapter;
import yhb.dc.common.CommonViewHolder;
import yhb.dc.common.Demo;
import yhb.dc.common.DemoBaseActivity;

@Demo(id = Demo.DEMO_ID_VIEW_POSITION_FIELD, name = "View 的位置属性影响")
public class ViewFieldsActivity extends DemoBaseActivity implements FieldView.OnValueChangeListener {

    private RecyclerView mRecyclerView;
    private ImageView mImageView;
    private CommonAdapter<FieldView.Field> commonAdapter;
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
            fields.add(new FieldView.Field("translationX", mImageView.getTranslationX(), fields.get(0)));
            fields.add(new FieldView.Field("translationY", mImageView.getTranslationY(), fields.get(1)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fields.add(new FieldView.Field("translationZ", mImageView.getTranslationZ()));
            }
            fields.add(new FieldView.Field("left", mImageView.getLeft(), fields.get(0)));
            fields.add(new FieldView.Field("right", mImageView.getRight(), fields.get(0)));
            fields.add(new FieldView.Field("bottom", mImageView.getBottom(), fields.get(1)));
            fields.add(new FieldView.Field("top", mImageView.getTop(), fields.get(1)));
        }
        commonAdapter.notifyDataSetChanged();
    }

    @Override
    public void onValueChange(FieldView.Field field) {
        invokeSetter(mImageView, field.mName, field.mValue);
        checkExpiredValues();
    }

    private void invokeSetter(Object executor, String name, float value) {
        Method method = null;
        boolean isFloat = true;
        try {
            method = executor.getClass().getMethod(getFieldSetterName(name), Float.TYPE);
        } catch (NoSuchMethodException e) {
            isFloat = false;
            try {
                method = executor.getClass().getMethod(getFieldSetterName(name), Integer.TYPE);
            } catch (NoSuchMethodException e1) {
                e.printStackTrace();
            }
        }
        if (method != null) {
            try {
                final int valueInt = (int) value;
                if (isFloat) {
                    method.invoke(mImageView, value);
                } else {
                    method.invoke(mImageView, valueInt);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private @Nullable
    Object invokeGetter(Object executor, String name) {
        Method method = null;
        try {
            method = executor.getClass().getMethod(getFieldGetterName(name));
        } catch (NoSuchMethodException e) {
            try {
                method = executor.getClass().getMethod(getFieldGetterName(name));
            } catch (NoSuchMethodException e1) {
                e.printStackTrace();
            }
        }
        if (method != null) {
            try {
                return method.invoke(mImageView);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void checkExpiredValues() {
        for (FieldView.Field f : fields) {
            final float oldValue = f.mValue;
            final Object newValue = invokeGetter(mImageView, f.mName);
            if (newValue instanceof Integer) {
                f.mValue = (int) newValue;
            } else if (newValue instanceof Float) {
                f.mValue = (float) newValue;
            }
            if (oldValue != f.mValue) {
                commonAdapter.notifyItemChanged(fields.indexOf(f));
            }
        }
    }

    private String getFieldSetterName(String name) {
        if (name == null || name.length() == 0) {
            return "";
        }
        return "set" + String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1);
    }

    private String getFieldGetterName(String name) {
        if (name == null || name.length() == 0) {
            return "";
        }
        return "get" + String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1);
    }

    @Nullable
    @Override
    public String descriptionData() {
        return "document/viewBasic.md";
    }

    @Override
    public int descriptionType() {
        return PARSE_TYPE_ASSET;
    }
}

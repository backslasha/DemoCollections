package yhb.dc.demo.fragment.fragment_nest;

import android.content.Context;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

public class LogRecyclerView extends RecyclerView {
    public LogRecyclerView(Context context) {
        super(context);
    }

    public LogRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LogRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d("yaohaibiao", "onSaveInstanceState");
        return super.onSaveInstanceState();
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        Log.d("yaohaibiao", "onRestoreInstanceState");
    }
}

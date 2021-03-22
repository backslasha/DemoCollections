package yhb.dc.common;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CommonViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mSparseArray;
    private LayoutInflater mLayoutInflater;

    CommonViewHolder(View itemView) {
        super(itemView);

        mSparseArray = new SparseArray<>();
        mLayoutInflater = LayoutInflater.from(itemView.getContext());

    }

    View getView(int id) {
        View candidate = mSparseArray.get(id);
        if (null == candidate) {
            candidate = itemView.findViewById(id);
            mSparseArray.put(id, candidate);
        }
        return candidate;
    }


    public static CommonViewHolder createViewHolder(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new CommonViewHolder(itemView);
    }

    public void setText(int id, String entity) {
        ((TextView) getView(id)).setText(entity);
    }

    public void setImage(int id, int resId) {
        ((ImageView) getView(id)).setImageResource(resId);
    }

    public void setImage(int id, Bitmap bitmap) {
        ((ImageView) getView(id)).setImageBitmap(bitmap);
    }

    public void bindOnClickListener(View.OnClickListener onClickListener) {
        itemView.setOnClickListener(onClickListener);
    }
}

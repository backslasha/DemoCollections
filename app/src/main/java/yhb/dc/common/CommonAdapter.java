package yhb.dc.common;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {
    private int mResId;
    private List<T> mEntities;

    public CommonAdapter(int resId, List<T> entities) {
        mResId = resId;
        mEntities = entities;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CommonViewHolder.createViewHolder(parent.getContext(), parent, mResId);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        convert(holder, mEntities.get(position));
    }

    @Override
    public int getItemCount() {
        return mEntities.size();
    }

    public abstract void convert(CommonViewHolder holder, T entity);

    public enum Op {
        APPEND, REPLACE, REMOVE
    }

    public void notifyDataSetChanged(List<T> entities, Op op) {

        if (op == Op.REPLACE) {
            mEntities = entities;
        }

        if (mEntities == null || mEntities.isEmpty()) {
            mEntities = new ArrayList<>();
        }

        if (op == Op.APPEND) {
            mEntities.addAll(entities);
        } else if (op == Op.REMOVE) {
            mEntities.removeAll(entities);
        }

        notifyDataSetChanged();
    }

}

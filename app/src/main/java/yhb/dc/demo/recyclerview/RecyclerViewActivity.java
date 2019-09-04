package yhb.dc.demo.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yhb.dc.R;
import yhb.dc.common.CommonAdapter;
import yhb.dc.common.CommonViewHolder;
import yhb.dc.common.Demo;
import yhb.dc.common.HomeAsUpActivity;

public class RecyclerViewActivity extends HomeAsUpActivity implements View.OnClickListener, Demo {


    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = outRect.right = dp2px(RecyclerViewActivity.this, 5);
            }
        });
        recyclerView.setAdapter(new ParentRecyclerAdapter());

    }

    @Override
    public void onClick(View v) {

    }

    static abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        BaseViewHolder(View itemView) {
            super(itemView);
        }

        abstract void bindData(Object object);


    }

    static class ViewHolder1 extends BaseViewHolder {

        ViewHolder1(View itemView) {
            super(itemView);
        }

        @Override
        void bindData(Object object) {
            ((TextView) itemView).setText(String.valueOf(object));
            itemView.setBackgroundColor(Color.RED);
        }

    }

    static class ViewHolder2 extends BaseViewHolder {
        ViewHolder2(View itemView) {
            super(itemView);
        }

        @Override
        void bindData(Object object) {

            object = getStringList(20);

            if (object instanceof List) {
                RecyclerView recyclerView = (RecyclerView) itemView;
                recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(new CommonAdapter<String>(android.R.layout.simple_list_item_1, (List<String>) object) {
                    @Override
                    public void convert(CommonViewHolder holder, String entity) {
                        ((TextView) holder.itemView).setText(entity);
                    }
                });
            }
        }


    }

    static class ViewHolder3 extends BaseViewHolder {
        ViewHolder3(View itemView) {
            super(itemView);
        }

        @Override
        void bindData(Object object) {
            ((TextView) itemView).setText(String.valueOf(object));
            itemView.setBackgroundColor(Color.YELLOW);
        }
    }

    static class ViewHolder4 extends BaseViewHolder {

        static int mCellSize = -1;

        ViewHolder4(View itemView) {
            super(itemView);
        }

        @Override
        void bindData(Object object) {
            ((TextView) itemView).setText(String.valueOf(object));
            if (mCellSize <= 0) {
                ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                if (layoutParams == null) {
                    layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                itemView.setBackgroundColor(Color.BLUE);
                itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mCellSize = itemView.getWidth();
                        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                        if (lp != null) {
                            lp.height = mCellSize;
                            itemView.setLayoutParams(lp);
                        }
                        itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

            } else {
                ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                if (lp != null) {
                    lp.height = mCellSize;
                    itemView.setLayoutParams(lp);
                }
            }

        }
    }

    class ParentRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

        private List<String> mStrings;

        ParentRecyclerAdapter() {
            mStrings = getStringList(50);
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            BaseViewHolder viewHolder;
            switch (viewType) {
                case 0:
                    viewHolder = new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));
                    break;
                case 1:
                    viewHolder = new ViewHolder2(new RecyclerView(parent.getContext()) {
                        // Save state
                        private Parcelable recyclerViewState;

                        @Override
                        protected void onDetachedFromWindow() {
                            super.onDetachedFromWindow();
                            recyclerViewState = getLayoutManager().onSaveInstanceState();
                        }

                        @Override
                        protected void onAttachedToWindow() {
                            super.onAttachedToWindow();
                            if (recyclerViewState != null) {
                                getLayoutManager().onRestoreInstanceState(recyclerViewState);
                            }
                        }
                    });
                    break;
                case 2:
                    viewHolder = new ViewHolder3(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));
                    break;
                case 3:
                default:
                    viewHolder = new ViewHolder4(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));
                    break;
            }

            if (viewType <= 2) {
                StaggeredGridLayoutManager.LayoutParams clp = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                if (clp == null) {
                    clp = (StaggeredGridLayoutManager.LayoutParams) mStaggeredGridLayoutManager.generateDefaultLayoutParams();
                }
                clp.setFullSpan(true);
                viewHolder.itemView.setLayoutParams(clp);
            }

            return viewHolder;
        }


        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
            if (payloads != null && !payloads.isEmpty()) {
                Object o = payloads.get(0);
                if (o instanceof List) {
                    holder.bindData(o);
                }
            } else {
                super.onBindViewHolder(holder, position, payloads);
            }

        }

        @Override
        public void onBindViewHolder(final BaseViewHolder holder, int position) {
            if (position >= 3) {
                holder.bindData(mStrings.get(position - 3));
            } else {
                holder.bindData(position);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position <= 3) {
                return position;
            }
            return 4;
        }

        @Override
        public int getItemCount() {
            return 50;
        }
    }

    private static List<String> getStringList(int count) {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            strings.add(String.valueOf(i));
        }
        return strings;
    }


}

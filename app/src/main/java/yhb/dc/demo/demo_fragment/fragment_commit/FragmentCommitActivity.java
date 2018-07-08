package yhb.dc.demo.demo_fragment.fragment_commit;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Toast;

import yhb.dc.R;
import yhb.dc.common.Demo;
import yhb.dc.common.LifeCycleActivity;

public class FragmentCommitActivity extends LifeCycleActivity implements Demo {

    private Fragment mFragment1, mFragment2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_commit);

        if (null == getSupportFragmentManager().findFragmentById(R.id.container)) {
            mFragment1 = createFragment("mFragment1");
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mFragment1)
                    .commitNow();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState); // 保存当前所有状态，包括 Fragment1
        doTransactionAfterSaveInstanceState();
    }

    @Override
    protected String getName() {
        return FragmentCommitActivity.class.getSimpleName();
    }

    /**
     * 当发生 Activity kill 时，系统调用 onSaveInstanceState 保存状态，以便重新进入 Activity 恢复原状态
     * <p>
     * 而这个方法发生在 onSaveInstanceState 之后，因此，发生 Activity kill 之后的恢复的 Activity，将直接失去以下改变（关于此次事务引起的改变）
     * 这也就是 “commitAllowingStateLoss” 之中 StateLoss 一词的含义
     * 倘若这里调用的不是 commitAllowingStateLoss 而是 commit()，程序将抛出异常
     */
    private void doTransactionAfterSaveInstanceState() {
        if (mFragment2 == null) {
            mFragment2 = createFragment("mFragment2");
        }
        getSupportFragmentManager() // 偷偷把 Fragment1 换成 Fragment2
                .beginTransaction()
                .replace(R.id.container, mFragment2)
                .commitAllowingStateLoss();
    }

    private Fragment createFragment(String content) {
        return StateFragment.newInstance(content);
    }
}

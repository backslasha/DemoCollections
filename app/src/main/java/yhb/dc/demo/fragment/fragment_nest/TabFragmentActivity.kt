package yhb.dc.demo.fragment.fragment_nest

import android.net.Uri
import android.os.Bundle
import yhb.dc.R
import yhb.dc.common.Demo
import yhb.dc.common.LifeCycleActivity
import yhb.dc.demo.fragment.fragment_nest.TabFragment.OnFragmentInteractionListener

@Demo(id = Demo.DEMO_ID_TAB_FRAGMENT_LIFE, name = "ViewPager 下 Fragment 的生命周期观察")
class TabFragmentActivity : LifeCycleActivity(), OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_fragment)
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentById(R.id.container)
        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.container, TabFragment.newInstance(), null)
                    .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
    }

    override fun getName(): String {
        return ""
    }

    override fun descriptionData(): String? {
        return description
    }

    override fun onFragmentInteraction(uri: Uri?) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val description = """
    <h1>FragmentPagerAdapter vs FragmentStatePagerAdapter</h1>
    <li>FragmentPagerAdapter destroyItem 时，将对应的 Fragment detach，initiateItem 时从 FragmentManager 中找出来 attach
    <li>FragmentStatePagerAdapter 保存了一个 Fragment 数组，destroyItem 时，将对应的 Fragment remove，Fragment 数组对应位置置空，下次重新 initiateItem 重新使用 getItem 获取
    <li>使用 Transaction detach/attach 一个 Fragment 时，不会触发 Fragment 对应的 onAttach\onDetach 生命周期，onDetach 仅在 Fragment 被 remove 之后回调
    """
    }
}

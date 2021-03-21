package yhb.dc

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import yhb.dc.common.CommonAdapter
import yhb.dc.common.CommonViewHolder
import yhb.dc.common.Demo
import yhb.dc.common.LifeCycleActivity

class MainActivity : LifeCycleActivity() {

    private val activities: MutableList<Class<Activity>> = ArrayList()
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        activities.addAll(generateDemoList())
        recyclerView = findViewById(R.id.recycler_view)
        refreshList(activities)
    }

    private fun refreshList(demoList: List<Class<out Activity?>>) {
        val recyclerView = recyclerView ?: return
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = object : CommonAdapter<Class<out Activity>>(R.layout.item_activity, demoList) {
            override fun convert(holder: CommonViewHolder, clazz: Class<out Activity>?) {
                val entity = clazz ?: return
                holder.setText(R.id.text_view_activity_name, demoName(entity))
                holder.bindOnClickListener {
                    val intent = Intent(this@MainActivity, entity)
                    startActivity(intent)
                }
            }
        }
    }

    private fun demoName(demoClazz: Class<out Activity>): String {
        val annotation = demoClazz.getAnnotation(Demo::class.java) ?: null
        val specifiedName = annotation?.name
        return if (specifiedName.isNullOrEmpty()) demoClazz.simpleName else specifiedName
    }

    private fun generateDemoList(): List<Class<Activity>> {
        val activities: MutableList<Class<Activity>> = ArrayList()
        try {
            val packageManager = packageManager
            val packageInfo = packageManager.getPackageInfo(
                    this.packageName, PackageManager.GET_ACTIVITIES
            )
            var autoJumpTo: Class<*>? = null
            for (activityInfo in packageInfo.activities) {
                val activityClass = Class.forName(activityInfo.name) as Class<Activity>
                val annotation = activityClass.getAnnotation(Demo::class.java) ?: continue
                activities.add(activityClass)
                if (annotation.id == debuggingDemoId) {
                    autoJumpTo = activityClass
                }
            }
            if (autoJumpTo != null) {
                val intent = Intent(this, autoJumpTo)
                startActivity(intent)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return activities
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.ab_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                refreshList(filterDemoList(newText))
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            private fun filterDemoList(keyword: String?): List<Class<Activity>> {
                if (keyword.isNullOrEmpty()) {
                    return activities
                }
                return activities.filter { demoName(it).contains(keyword, ignoreCase = true) }.toCollection(ArrayList())
            }
        })
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName))
        return true
    }

    companion object {
        private const val debuggingDemoId = -1
    }
}
package yhb.dc

import android.content.Context
import yhb.dc.common.SharedPreferencesObject

/**
 * created by yaohaibiao on 2021/3/21.
 */
class MainSpObj(context: Context) : SharedPreferencesObject(context.applicationContext, SP_NAME) {
    companion object {
        private const val SP_NAME = "sp_file_name_main"
    }
}
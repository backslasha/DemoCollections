package yhb.dc.common

import android.content.Context
import android.content.SharedPreferences
import java.util.HashSet
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class SharedPreferencesObject(context: Context, spName: String) : SpGetterSetter by SharedPreferenceDelegates {

    val preferences: SharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)

    private object SharedPreferenceDelegates : SpGetterSetter {

        override fun int(defaultValue: Int) = object : ReadWriteProperty<SharedPreferencesObject, Int> {

            override fun getValue(thisRef: SharedPreferencesObject, property: KProperty<*>): Int {
                return thisRef.preferences.getInt(property.name, defaultValue)
            }

            override fun setValue(thisRef: SharedPreferencesObject, property: KProperty<*>, value: Int) {
                thisRef.preferences.edit().putInt(property.name, value).apply()
            }
        }

        override fun long(defaultValue: Long) = object : ReadWriteProperty<SharedPreferencesObject, Long> {

            override fun getValue(thisRef: SharedPreferencesObject, property: KProperty<*>): Long {
                return thisRef.preferences.getLong(property.name, defaultValue)
            }

            override fun setValue(thisRef: SharedPreferencesObject, property: KProperty<*>, value: Long) {
                thisRef.preferences.edit().putLong(property.name, value).apply()
            }
        }

        override fun boolean(defaultValue: Boolean) = object : ReadWriteProperty<SharedPreferencesObject, Boolean> {
            override fun getValue(thisRef: SharedPreferencesObject, property: KProperty<*>): Boolean {
                return thisRef.preferences.getBoolean(property.name, defaultValue)
            }

            override fun setValue(thisRef: SharedPreferencesObject, property: KProperty<*>, value: Boolean) {
                thisRef.preferences.edit().putBoolean(property.name, value).apply()
            }
        }

        override fun float(defaultValue: Float) = object : ReadWriteProperty<SharedPreferencesObject, Float> {
            override fun getValue(thisRef: SharedPreferencesObject, property: KProperty<*>): Float {
                return thisRef.preferences.getFloat(property.name, defaultValue)
            }

            override fun setValue(thisRef: SharedPreferencesObject, property: KProperty<*>, value: Float) {
                thisRef.preferences.edit().putFloat(property.name, value).apply()
            }
        }

        override fun string(defaultValue: String) = object : ReadWriteProperty<SharedPreferencesObject, String> {
            override fun getValue(thisRef: SharedPreferencesObject, property: KProperty<*>): String {
                return thisRef.preferences.getString(property.name, defaultValue) ?: defaultValue
            }

            override fun setValue(thisRef: SharedPreferencesObject, property: KProperty<*>, value: String) {
                thisRef.preferences.edit().putString(property.name, value).apply()
            }
        }

        override fun stringSet(defaultValue: Set<String>) = object : ReadWriteProperty<SharedPreferencesObject, Set<String>> {
            override fun getValue(thisRef: SharedPreferencesObject, property: KProperty<*>): Set<String> {
                return thisRef.preferences.getStringSet(property.name, defaultValue) ?: HashSet()
            }

            override fun setValue(thisRef: SharedPreferencesObject, property: KProperty<*>, value: Set<String>) {
                thisRef.preferences.edit().putStringSet(property.name, value).apply()
            }
        }
    }
}

interface SpGetterSetter {
    fun int(defaultValue: Int = 0): ReadWriteProperty<SharedPreferencesObject, Int>
    fun long(defaultValue: Long = 0L): ReadWriteProperty<SharedPreferencesObject, Long>
    fun boolean(defaultValue: Boolean = false): ReadWriteProperty<SharedPreferencesObject, Boolean>
    fun float(defaultValue: Float = 0.0f): ReadWriteProperty<SharedPreferencesObject, Float>
    fun string(defaultValue: String = ""): ReadWriteProperty<SharedPreferencesObject, String>
    fun stringSet(defaultValue: Set<String> = HashSet()): ReadWriteProperty<SharedPreferencesObject, Set<String>>
}
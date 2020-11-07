package org.koitharu.kotatsu.utils.delegates.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class EnumPreferenceDelegate<E : Enum<*>>(
	private val cls: Class<E>,
	private val key: String,
	private val defValue: E
) : ReadWriteProperty<SharedPreferences, E> {

	override fun getValue(thisRef: SharedPreferences, property: KProperty<*>): E {
		val name = thisRef.getString(key, null)
		if (name === null) {
			return defValue
		}
		return cls.enumConstants?.find { it.name == name } ?: defValue
	}

	override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: E) {
		thisRef.edit {
			putString(key, value.name)
		}
	}
}
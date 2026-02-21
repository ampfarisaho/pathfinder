package za.ampfarisaho.pathfinder.sample.common

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface PermissionLookup {
    fun isGranted(permission: String): Boolean
}

class PermissionLookupImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : PermissionLookup {

    override fun isGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}
package cn.wearbbs.note.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.room.Room
import cn.wearbbs.note.database.AppDatabase
import cn.wearbbs.note.database.dao.NoteDao

/**
 * @author JackuXL
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "note"
        ).allowMainThreadQueries().build()
        noteDao = db.noteDao()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
        lateinit var noteDao: NoteDao
        val applicationVersion: Double
            get() {
                val packageManager = context.packageManager
                try {
                    val packInfo = packageManager.getPackageInfo(context.packageName, 0)
                    return packInfo.versionName.toDouble()
                } catch (ignored: PackageManager.NameNotFoundException) {
                }
                return 0.0
            }
    }
}
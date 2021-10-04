package com.kapil.android.youlearn.api.db

import android.content.Context
import androidx.room.*
import com.kapil.android.youlearn.models.search.Item

@Database(
    entities = [Item::class],
    version = 14,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ItemDataBase: RoomDatabase() {

    abstract fun getItemDataBase() : ItemDao

    companion object{
        @Volatile
        private var instance : ItemDataBase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it}
        }

        private fun createDatabase(context: Context)  =
            Room.databaseBuilder(
                context.applicationContext,
                ItemDataBase::class.java,
                "item_db.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }

}
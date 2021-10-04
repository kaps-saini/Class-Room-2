package com.kapil.android.youlearn.api.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kapil.android.youlearn.models.search.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item): Long

    @Query("SELECT * FROM SearchTable ORDER BY searchKey DESC")
    fun getAllData() : LiveData<List<Item>>

    @Query("SELECT * FROM SearchTable WHERE searchVideoId = :id ")
    fun getAllVideoId(id: String?) : LiveData<List<Item>>

    @Update
    suspend fun update(item:Item)

    @Delete
    suspend fun delete(item: Item)

}
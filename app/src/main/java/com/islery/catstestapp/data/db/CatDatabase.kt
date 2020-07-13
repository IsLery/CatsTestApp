package com.islery.catstestapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.islery.catstestapp.data.model.Cat


const val CATS_TABLE = "fav_cats"
const val DATABASE_NAME = "cats_db"

@Database(
    entities = [Cat::class],
    version = 1,
    exportSchema = false
)
abstract class CatDatabase : RoomDatabase(){
    abstract fun catDao():CatDao
}

@Dao
interface CatDao{

    @Query("SELECT * FROM $CATS_TABLE")
    fun getAll():LiveData<List<Cat>>

    @Delete
    suspend fun delete(cat: Cat)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cat: Cat): Long


}
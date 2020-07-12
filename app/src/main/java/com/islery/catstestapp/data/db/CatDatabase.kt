package com.islery.catstestapp.data.db

import android.content.Context
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

    companion object{

        @Volatile private var instance: CatDatabase? = null

        fun getInstance(context: Context): CatDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): CatDatabase {
            return Room.databaseBuilder(context, CatDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }

}

@Dao
interface CatDao{

    @Query("SELECT * FROM $CATS_TABLE")
    fun getAll():LiveData<List<Cat>>

    @Delete
    suspend fun delete(cat: Cat)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cat: Cat)

    @Query("SELECT ID FROM $CATS_TABLE WHERE ID IN (:catIds)")
    suspend fun contains( catIds: List<String>):List<String>

}
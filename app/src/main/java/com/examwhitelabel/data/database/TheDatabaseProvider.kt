package com.examwhitelabel.data.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import com.examwhitelabel.data.TheItem

object TheDatabaseProvider {

    @Dao
    interface TheDatabaseDao : TheDatabaseInterface {

        @Query("SELECT * FROM the_item")
        override suspend fun getItems(): List<TheItem>

        @Query("DELETE FROM the_item")
        suspend fun removeItems()

        @Insert
        suspend fun addItems(items: List<TheItem>)

        @Transaction
        override suspend fun updateItems(items: List<TheItem>) {
            removeItems()
            addItems(items)
        }
    }

    @Database(entities = [TheItem::class], version = 1)
    abstract class TheDatabase : RoomDatabase() {

        abstract fun databaseDao(): TheDatabaseDao
    }

    fun provideTheDatabaseInterface(context: Context): TheDatabaseInterface =
        Room.databaseBuilder(context, TheDatabase::class.java, "the-database").build().databaseDao()
}

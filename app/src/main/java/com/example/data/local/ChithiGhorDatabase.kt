package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [LetterEntity::class, ReportEntity::class], version = 1, exportSchema = false)
abstract class ChithiGhorDatabase : RoomDatabase() {

    abstract fun letterDao(): LetterDao

    companion object {
        @Volatile
        private var INSTANCE: ChithiGhorDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ChithiGhorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChithiGhorDatabase::class.java,
                    "chithi_ghor_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.letterDao())
                    }
                }
            }

            suspend fun populateInitialData(letterDao: LetterDao) {
                val initialLetters = SeedLetters.getInitialLetters()
                letterDao.insertLetters(initialLetters)
            }
        }
    }
}

package com.example.wicrypttechnicaltest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.wicrypttechnicaltest.model.Jobs

/**
 * A database that stores SleepNight information.
 * And a global method to get access to the database.
 *
 * This pattern is pretty much the same for any database,
 * so you can reuse it.
 */

@Database(
        entities = [Jobs::class, RemoteKeys::class],
        version = 1,
        exportSchema = false
)
abstract class JobRepoDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */

    abstract fun reposDao(): JobRepoDao
    abstract fun remoteKeysDao(): RemoteKeyDao

    companion object {

        /**
         * Helper function to get the database.
         *
         * If a database has already been retrieved, the previous database will be returned.
         * Otherwise, create a new database.
         *
         * This function is threadsafe, and callers should cache the result for multiple database
         * calls to avoid overhead.
         *
         * This is an example of a simple Singleton pattern that takes another Singleton as an
         * argument in Kotlin.
         *
         * To learn more about Singleton read the wikipedia article:
         * https://en.wikipedia.org/wiki/Singleton_pattern
         *
         * @param context The application context Singleton, used to get access to the filesystem.
         */


        @Volatile
        private var INSTANCE: JobRepoDatabase? = null

        fun getInstance(context: Context): JobRepoDatabase =
        // Multiple threads can ask for the database at the same time, ensure we only initialize
        // it once by using synchronized. Only one thread may enter a synchronized block at a
        // time.
                // If instance is `null` make a new database instance.


                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also {
                        INSTANCE = it
                    }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(
                        context.applicationContext,
                        JobRepoDatabase::class.java, "JobHub.db"
                )
                        .build()
    }

}
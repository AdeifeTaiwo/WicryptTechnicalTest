package com.example.wicrypttechnicaltest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.wicrypttechnicaltest.model.Jobs


@Database(
        entities = [Jobs::class, RemoteKeys::class],
        version = 1,
        exportSchema = false
)
abstract class JobRepoDatabase: RoomDatabase(){

    abstract fun reposDao(): JobRepoDao
    abstract fun remoteKeysDao(): RemoteKeyDao

    companion object {

        @Volatile
        private var INSTANCE: JobRepoDatabase? = null

        fun getInstance(context: Context): JobRepoDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(
                        context.applicationContext,
                        JobRepoDatabase::class.java, "JobHub.db"
                )
                        .build()
    }

}
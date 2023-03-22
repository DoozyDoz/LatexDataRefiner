package com.example.latexdatarefiner

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [LatexData::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun latexDataDao(): LatexDataDao

    companion object {
        private const val DB_NAME = "latex_data.db"

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE latex_data ADD COLUMN katexCode TEXT")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}

package com.reyaz.searchrecipeapp.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.reyaz.search.data.local.RecipeDao
import com.reyaz.search.domain.model.Recipe

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getRecipeDao(): RecipeDao

    companion object {
        fun getInstance(context: Context) =
            Room
                .databaseBuilder(
                    context, AppDatabase::class.java, "app_database"
                )
                .fallbackToDestructiveMigration()
                .build()
    }
}
package com.zeynelinho.artbooktesting.roomDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zeynelinho.artbooktesting.model.Art

@Database(entities = [Art::class], version = 1)
abstract class ArtDatabase : RoomDatabase(){
    abstract fun artDao() : ArtDao

}
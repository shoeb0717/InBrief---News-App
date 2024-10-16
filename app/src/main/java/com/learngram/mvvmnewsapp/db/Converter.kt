package com.learngram.mvvmnewsapp.db

import androidx.room.TypeConverter
import com.learngram.mvvmnewsapp.Models.Source

class Converter {

    @TypeConverter
    fun fromSource(source: Source):String?{
       return source.name
    }

    @TypeConverter
    fun toSource(name:String): Source {
        return Source(name,name)
    }
}
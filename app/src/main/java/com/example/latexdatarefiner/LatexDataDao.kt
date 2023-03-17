package com.example.latexdatarefiner

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface LatexDataDao {
    @Query("SELECT * FROM latex_data")
    fun getAll(): LiveData<List<LatexData>>

    @Insert
    suspend fun insert(latexData: LatexData)
}

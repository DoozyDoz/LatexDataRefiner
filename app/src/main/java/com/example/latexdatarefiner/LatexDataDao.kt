package com.example.latexdatarefiner

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface LatexDataDao {
    @Query("SELECT * FROM latex_data")
    fun getAll(): LiveData<List<LatexData>>

    @Insert
    suspend fun insert(latexData: LatexData)

    @Update
    suspend fun update(latexData: LatexData)

    @Query("SELECT * FROM latex_data WHERE id = :id")
    suspend fun getLatexDataById(id: Int): LatexData?
}

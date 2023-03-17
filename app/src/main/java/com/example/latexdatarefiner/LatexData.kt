package com.example.latexdatarefiner

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "latex_data")
data class LatexData(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "latex_code") val latexCode: String,
    @ColumnInfo(name = "image_path") val imagePath: String
)
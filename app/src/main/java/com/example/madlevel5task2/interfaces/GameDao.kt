package com.example.madlevel5task2.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.madlevel5task2.models.Game


@Dao
interface GameDao {

    @Insert
    suspend fun insertGame(game: Game)

    @Query("SELECT * FROM gameTable")
    fun getAllGames(): LiveData<List<Game>>

    @Query("DELETE FROM gameTable")
    fun deleteAllGames()
}
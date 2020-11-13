package com.example.madlevel5task2.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.madlevel5task2.models.Game


@Dao
interface GameBacklogDao {

    @Insert
    suspend fun insertGame(game: Game)

    @Query("SELECT * FROM gameTable")
    fun getAllGames(): LiveData<List<Game>>

    @Delete
    suspend fun deleteGame(game: Game)

    @Query("DELETE FROM gameTable")
    fun deleteAllGames()
}
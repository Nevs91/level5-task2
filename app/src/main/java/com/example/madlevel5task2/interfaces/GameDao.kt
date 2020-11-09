package com.example.madlevel5task2.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.madlevel5task2.models.Game


@Dao
interface GameDao {

    @Insert
    suspend fun insertGame(game: Game)

    @Query("SELECT * FROM gameTable")
    fun getAllGames(): LiveData<Game?>

    @Query("SELECT COUNT(*) FROM gameTable WHERE id = :gameId")
    suspend fun getGameById(gameId: Long): LiveData<Game?>

    @Update
    suspend fun updateGame(note: Game)
}
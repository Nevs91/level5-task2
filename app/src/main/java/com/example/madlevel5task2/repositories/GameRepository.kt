package com.example.madlevel5task2.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.madlevel5task2.database.GameBacklogRoomDatabase
import com.example.madlevel5task2.interfaces.GameDao
import com.example.madlevel5task2.models.Game

class GameRepository(context: Context) {

    private val gameDao: GameDao

    init {
        val database = GameBacklogRoomDatabase.getDatabase(context)
        gameDao = database!!.gameDao()
    }

    /**
     * Get all games from the database
     */
    fun getGames(): LiveData<Game?> {
        return gameDao.getAllGames()
    }

    /**
     * Get a specific game from the database by ID
     */
    suspend fun getGameById(gameId: Long) {
        gameDao.getGameById(gameId)
    }

    /**
     * Update or insert a (new) game based on the provided game object
     */
    suspend fun updateGame(game: Game) {
        gameDao.updateGame(game)
    }
}
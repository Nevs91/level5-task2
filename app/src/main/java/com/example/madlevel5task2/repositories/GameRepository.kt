package com.example.madlevel5task2.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.madlevel5task2.database.GameBacklogRoomDatabase
import com.example.madlevel5task2.interfaces.GameBacklogDao
import com.example.madlevel5task2.models.Game

class GameRepository(context: Context) {

    private val gameBacklogDao: GameBacklogDao

    init {
        val database = GameBacklogRoomDatabase.getDatabase(context)
        gameBacklogDao = database!!.gameDao()
    }

    /**
     * Get all games from the database
     */
    fun getAllGames(): LiveData<List<Game>> {
        return gameBacklogDao.getAllGames()
    }

    /**
     * Insert and store a game in the database
     */
    suspend fun insertGame(game: Game) {
        gameBacklogDao.insertGame(game)
    }

    /**
     * Delete a single game from the database
     */
    suspend fun deleteGame(game: Game) {
        gameBacklogDao.deleteGame(game)
    }

    /**
     * Delete all games from the database
     */
    fun deleteAllGames() {
        gameBacklogDao.deleteAllGames()
    }
}
package com.example.madlevel5task2.views

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.madlevel5task2.models.Game
import com.example.madlevel5task2.repositories.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val gameRepository =  GameRepository(application.applicationContext)
    private val mainScope = CoroutineScope(Dispatchers.Main)

    val games: LiveData<List<Game>> = gameRepository.getAllGames()
    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()

    fun saveGame(title: String, platform: String, releaseDay: Int, releaseMonth: Int, releaseYear: Int) {

        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = releaseYear
        cal[Calendar.MONTH] = releaseMonth
        cal[Calendar.DAY_OF_MONTH] = releaseDay
        val releaseDate = cal.time

        val newGame = Game(
                title = title,
                platform = platform,
                releaseDate = releaseDate,
        )

        if(isGameValid(newGame)) {
            mainScope.launch {
                withContext(Dispatchers.IO) {
                    gameRepository.insertGame(newGame)
                }
                success.value = true
            }
        }
    }

    fun deleteAllGames() {
        if (gamesCanBeDeleted()) {
            mainScope.launch {
                withContext(Dispatchers.IO) {
                    gameRepository.deleteAllGames()
                }
                success.value = true
            }
        }
    }

    private fun isGameValid(game: Game): Boolean {
        return when {
            game.title.isBlank() -> {
                error.value = "Title must not be empty"
                false
            }
            game.platform.isBlank() -> {
                error.value = "Platform must not be empty"
                false
            }
            else -> true
        }
    }

    private fun gamesCanBeDeleted(): Boolean {
        return when {
            games.value.isNullOrEmpty() -> {
                error.value = "There are no games in the backlog to remove"
                false
            }
            else -> true
        }
    }
}
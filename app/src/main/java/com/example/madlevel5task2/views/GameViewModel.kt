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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

    private val gameRepository =  GameRepository(application.applicationContext)
    private val mainScope = CoroutineScope(Dispatchers.Main)

    val games: LiveData<List<Game>> = gameRepository.getAllGames()
    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()

    /**
     * Create and save a new game with the provided information to the database
     */
    fun createAndSaveGame(title: String, platform: String, dateString: String) {
        // Check if the dateString can construct a valid date before creating a new game instance
        if (isValidDateString(dateString)) {
            val newGame = Game(
                    title = title,
                    platform = platform,
                    releaseDate = dateFormat.parse(dateString)!!,
            )

            // Save the created game instance with success feedback
            saveGame(newGame, true)
        }
    }

    /**
     * Check if the provided game instance is valid and store (when valid) to the database
     */
    fun saveGame(game: Game, withSuccessStatus: Boolean) {
        if(isGameValid(game)) {
            mainScope.launch {
                withContext(Dispatchers.IO) {
                    gameRepository.insertGame(game)
                }

                if (withSuccessStatus) {
                    success.value = true
                }
            }
        }
    }

    /**
     * Store each item from the provided list in the database
     */
    fun saveGameList(games: ArrayList<Game>) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                games.forEach { game: Game ->
                    gameRepository.insertGame(game)
                }
            }
        }
    }

    /**
     * Delete the provided game instance
     */
    fun deleteGame(game: Game) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.deleteGame(game)
            }
            success.value = true
        }
    }

    /**
     * Delete all games from the database
     */
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

    /**
     * Check if the provided string can be parsed into a valid date
     */
    private fun isValidDateString(dateString: String): Boolean {
        dateFormat.isLenient = false

        try {
            dateFormat.parse(dateString)
        } catch (pe: ParseException) {
            error.value = "Please fill in a valid date"
            return false
        }

        return true
    }

    /**
     * Check if the properties of the provided game instance are valid
     */
    private fun isGameValid(game: Game): Boolean {
        return when {
            game.title.isBlank() -> {
                error.value = "Please fill in a title"
                false
            }
            game.platform.isBlank() -> {
                error.value = "Please fill in a platform"
                false
            }
            else -> true
        }
    }

    /**
     * Check if there are games to be removed
     */
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
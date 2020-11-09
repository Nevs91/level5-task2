package com.example.madlevel5task2.views

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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

    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()

    fun updateGame(gameId: Long?, title: String, platform: String, releaseDate: Date) {

        //game = gameRepository.

        //if there is an existing note, take that id to update it instead of adding a new one
        val newGame = Game(
            id = gameId,
            title = title,
            platform = platform,
            releaseDate = releaseDate,
        )

        if(isGameValid(newGame)) {
            mainScope.launch {
                withContext(Dispatchers.IO) {
                    gameRepository.updateGame(newGame)
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
}
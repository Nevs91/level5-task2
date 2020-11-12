package com.example.madlevel5task2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.madlevel5task2.views.GameViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_game.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddGameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val mainActivity = activity as MainActivity?

        mainActivity!!.fabSaveGame.setOnClickListener {
            this.saveGameInGameBacklog()
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.observeGame()
    }

    private fun observeGame() {
        viewModel.error.observe(viewLifecycleOwner, { message ->
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        })

        viewModel.success.observe(viewLifecycleOwner, {
            findNavController().popBackStack()
        })
    }

    private fun saveGameInGameBacklog() {
        viewModel.saveGame(
                etTitle?.text.toString(),
                etPlatform?.text.toString(),
                etReleaseDay?.text.toString().toInt(),
                etReleaseMonth?.text.toString().toInt(),
                etReleaseYear?.text.toString().toInt(),
        )
    }
}
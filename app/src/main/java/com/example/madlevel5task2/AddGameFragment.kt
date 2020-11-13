package com.example.madlevel5task2

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

    /**
     * Set the menu with specific menu options for this fragment
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_add_note, menu)

        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar

        // Enable the actionBar back button
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true) // enable the button
            actionBar.setDisplayHomeAsUpEnabled(true) // enable the left caret
            actionBar.setDisplayShowHomeEnabled(true) // enable the icon
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Handle clicks on icons in the title bar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        viewModel.createAndSaveGame(
                etTitle?.text.toString(),
                etPlatform?.text.toString(),
                String.format("%s-%s-%s",
                        etReleaseDay?.text.toString(),
                        etReleaseMonth?.text.toString(),
                        etReleaseYear?.text.toString()
                )
        )
    }
}
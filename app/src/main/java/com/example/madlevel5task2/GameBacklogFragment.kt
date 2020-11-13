package com.example.madlevel5task2

import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel5task2.adapters.GameBacklogAdapter
import com.example.madlevel5task2.models.Game
import com.example.madlevel5task2.views.GameViewModel
import kotlinx.android.synthetic.main.fragment_game_backlog.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GameBacklogFragment : Fragment() {

    private val games = arrayListOf<Game>()

    private lateinit var gameBacklogAdapter: GameBacklogAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_backlog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeAddGameResult()
    }

    /**
     * Set the menu with specific menu options for this fragment
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar

        // Disable the actionBar back button
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false) // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false) // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false) // remove the icon
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Handle clicks on icons in the title bar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_backlog -> {
                clearGameBacklog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Initialize the view of this fragment
     */
    private fun initViews() {
        gameBacklogAdapter = GameBacklogAdapter(games)
        viewManager = LinearLayoutManager(activity)

        //createItemTouchHelper().attachToRecyclerView(rvGames)

        rvGames.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = gameBacklogAdapter
        }
    }

    /**
     * Observe the viewModel for possible changes
     */
    private fun observeAddGameResult() {
        viewModel.games.observe(viewLifecycleOwner, { games ->
            this@GameBacklogFragment.games.clear()
            this@GameBacklogFragment.games.addAll(games.sortedBy { it.releaseDate })
            gameBacklogAdapter.notifyDataSetChanged()
        })
    }

    /**
     * Remove all stored match results in the database and re-populate the recyclerView
     */
    private fun clearGameBacklog() {
        viewModel.deleteAllGames()

        viewModel.error.observe(viewLifecycleOwner, { message ->
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        })

        viewModel.success.observe(viewLifecycleOwner, {
            Toast.makeText(activity, "Success!", Toast.LENGTH_SHORT).show()
        })
    }
}
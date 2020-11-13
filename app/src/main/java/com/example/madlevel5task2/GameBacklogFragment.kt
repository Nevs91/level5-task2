package com.example.madlevel5task2

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel5task2.adapters.GameBacklogAdapter
import com.example.madlevel5task2.models.Game
import com.example.madlevel5task2.views.GameViewModel
import com.google.android.material.snackbar.Snackbar
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

        createItemTouchHelper().attachToRecyclerView(rvGames)

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
        val gameListCopy: ArrayList<Game> = ArrayList(games)

        viewModel.deleteAllGames()

        viewModel.error.observe(viewLifecycleOwner, { message ->
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        })

        // When the undo button is pressed, insert all games from the copy to the database
        viewModel.success.observe(viewLifecycleOwner, {
            Snackbar.make(requireActivity().findViewById(R.id.mainActivity), "Successfully deleted backlog", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        viewModel.saveGameList(gameListCopy)
                    }
                    .show()
        })
    }

    /**
     * Create a touch helper to recognize when a user swipes an item from a recycler view.
     * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
     * and uses callbacks to signal when a user is performing these actions.
     */
    private fun createItemTouchHelper(): ItemTouchHelper {
        // Callback which is used to create the ItemTouch helper.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Disable the ability to move items up and down.
            override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val gameToDelete = games[position]

                // Remove the game from the database
                viewModel.deleteGame(gameToDelete)

                // When the undo button is pressed, insert the removed game back into the database
                viewModel.success.observe(viewLifecycleOwner, {
                    Snackbar.make(requireActivity().findViewById(R.id.mainActivity), "Successfully deleted game", Snackbar.LENGTH_LONG)
                            .setAction("Undo") {
                                viewModel.saveGame(gameToDelete, false)
                            }
                            .show()
                })
            }
        }
        return ItemTouchHelper(callback)
    }
}
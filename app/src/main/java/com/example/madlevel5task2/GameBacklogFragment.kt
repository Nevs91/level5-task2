package com.example.madlevel5task2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private fun observeAddGameResult() {
        viewModel.games.observe(viewLifecycleOwner, { games ->
            this@GameBacklogFragment.games.clear()
            this@GameBacklogFragment.games.addAll(games)
            gameBacklogAdapter.notifyDataSetChanged()
        })
    }
}
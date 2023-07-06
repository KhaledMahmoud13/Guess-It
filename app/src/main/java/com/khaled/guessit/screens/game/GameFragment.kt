package com.khaled.guessit.screens.game

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.khaled.guessit.R
import com.khaled.guessit.databinding.GameFragmentBinding

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        Log.i("GameFragment", "Called ViewModelProvider")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)



        binding.correctButton.setOnClickListener {
            viewModel.onCorrect()
        }
        binding.skipButton.setOnClickListener {
            viewModel.onSkip()
        }



        viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })

        viewModel.word.observe(viewLifecycleOwner, Observer { newWord ->
            binding.wordText.text = newWord
        })

        viewModel.currentTime.observe(viewLifecycleOwner, Observer { newTime ->
            binding.timerText.text = DateUtils.formatElapsedTime(newTime)

        })

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { hasFinished ->
            if (hasFinished){
                gameFinished()
                viewModel.onGameFinishComplete()
            }
        })

        return binding.root
    }

    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0)
        findNavController(this).navigate(action)
    }
}

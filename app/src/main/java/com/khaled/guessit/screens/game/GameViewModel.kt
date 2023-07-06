package com.khaled.guessit.screens.game

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    companion object{
        private const val DONE = 0L

        private const val ONE_SECOND = 1000L

        private const val COUNTDOWN_TIMER = 10000L
    }

    private val timer: CountDownTimer

    private val _currentTime = MutableLiveData<Long>()
    val currentTime : LiveData<Long> get() = _currentTime

    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String> get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean> get() = _eventGameFinish

    init {
        resetList()
        nextWord()
        _score.value = 0

        timer = object : CountDownTimer(COUNTDOWN_TIMER, ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventGameFinish.value = true
            }
        }

        timer.start()

    }

    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }

        _word.value = wordList.removeAt(0)
    }

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}
package com.tiorisnanto.coursepotato

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val scoreA : MutableLiveData<Int> = MutableLiveData()
    private val scoreB : MutableLiveData<Int> = MutableLiveData()

    fun addScoreTeamA(){
        val result = getScoreA()?.value?.plus(1) //getValue
        scoreA.value = result //setValue
    }

    fun addScoreTeamB(){
        val result = getScoreB()?.value?.plus(1)
        scoreB.value = result
    }

    fun minusScoreTeamA(){
        val result = getScoreA()?.value?.minus(1)
        if (result!! < 0) {
            scoreA.value = 0
        } else {
            scoreA.value = result
        }
    }

    fun minusScoreTeamB(){
        val result = getScoreB()?.value?.minus(1)
        if (result!! < 0) {
            scoreB.value = 0
        } else {
            scoreB.value = result
        }
    }

    fun resetScore(){
        scoreA.value = 0
        scoreB.value = 0
    }

    fun getScoreA() : LiveData<Int> {
        if (scoreA?.value == null) {
            scoreA.value = 0
        }
        return scoreA
    }

    fun getScoreB() : LiveData<Int> {
        if (scoreB?.value == null) {
            scoreB.value = 0
        }
        return scoreB
    }
}
package com.tiorisnanto.coursepotato

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tiorisnanto.coursepotato.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var getMainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.btnTeamA.setOnClickListener(this)
        binding.btnTeamB.setOnClickListener(this)
        binding.btnReset.setOnClickListener(this)
        binding.btnMinusTeamA.setOnClickListener(this)
        binding.btnMinusTeamB.setOnClickListener(this)

        initView()

    }

    private fun initView() {
        getMainViewModel.getScoreA().observe(this, Observer { value->
            if (value != null) {
                binding.tvScoreTeamA.text = value.toString()
            }
        })

        getMainViewModel.getScoreB().observe(this, Observer { value->
            if (value != null) {
                binding.tvScoreTeamB.text = value.toString()
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnTeamA -> {
                getMainViewModel.addScoreTeamA()
            }
            R.id.btnTeamB -> {
                getMainViewModel.addScoreTeamB()
            }
            R.id.btnMinusTeamA -> {
                getMainViewModel.minusScoreTeamA()
            }
            R.id.btnMinusTeamB -> {
                getMainViewModel.minusScoreTeamB()
            }
            R.id.btnReset -> {
                getMainViewModel.resetScore()
            }
        }
    }



}
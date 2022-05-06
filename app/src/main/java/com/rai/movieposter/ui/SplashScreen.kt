package com.rai.movieposter.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.rai.movieposter.databinding.ActivitySplashBinding


class SplashScreen : AppCompatActivity()  {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        with(binding){
            imgBackgroud.animate().translationY(-2500.0f).setDuration(1000).startDelay = 5000
            appMade.animate().translationY(2000.0f).setDuration(1000).startDelay = 5000
            animationView.animate().translationY(1500.0f).setDuration(1000).startDelay=5000
        }



        Handler().postDelayed({ startActivity(Intent(this@SplashScreen, MainActivity::class.java)) },
            6000)
    }
}
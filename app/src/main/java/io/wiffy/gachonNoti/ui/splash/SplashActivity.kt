package io.wiffy.gachonNoti.ui.splash

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import io.wiffy.gachonNoti.R
import io.wiffy.gachonNoti.model.`object`.Component
import io.wiffy.gachonNoti.utils.*
import io.wiffy.gachonNoti.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*

@Suppress("DEPRECATION")
class SplashActivity : SplashContract.View() {

    lateinit var mPresenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.statusBarColor = if (Component.darkTheme) {
            resources.getColor(getDarkColor1())
        } else {
            resources.getColor(getThemeColor())
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        supportActionBar?.hide()

        getScreenSize()

        mPresenter = SplashPresenter(this, applicationContext)
        mPresenter.initPresent()
    }

    override fun moveToMain() {
        val dt = intent.getStringExtra("keyData")
        if (!dt.isNullOrBlank()) {
            console(dt)
            Component.keyWordData = dt
        }
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.abc_fade_in, R.anim.not_move_activity)
            finish()
        }, Component.delay)
    }

    override fun onBackPressed() = finish()


    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            super.setRequestedOrientation(requestedOrientation)
        }
    }

    override fun setBirthdayText(str: String) {
        gachonAlimi.text = str
    }

    override fun setParams() {
        bububu.setBackgroundColor(resources.getColor(R.color.white))
        bababa.setBackgroundColor(resources.getColor(R.color.white))
        logo_splash.layoutParams = logo_splash.layoutParams.apply {
            width = 512
            height = 512
        }
    }

    override fun setTextColor(id: Int) {
        gachonAlimi.setTextColor(resources.getColor(id))
    }

    override fun setImageView(id: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            logo_splash.setImageDrawable(applicationContext.resources.getDrawable(id))
        } else {
            Glide.with(this).load(id).into(logo_splash)
        }
    }

    override fun darkTheme() {
        window.navigationBarColor = resources.getColor(getDarkColor2())
        bububu.setBackgroundColor(resources.getColor(R.color.myDark1))
        bababa.setBackgroundColor(resources.getColor(R.color.myDark1))
        setTextColor(R.color.white)
        setImageView(R.drawable.defaults_round)
    }
}
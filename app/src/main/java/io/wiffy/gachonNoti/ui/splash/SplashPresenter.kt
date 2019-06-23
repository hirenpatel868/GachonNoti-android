package io.wiffy.gachonNoti.ui.splash

import android.os.Handler
import android.os.Looper
import io.wiffy.gachonNoti.model.Util


class SplashPresenter(private val mView: SplashContract.View) : SplashContract.Presenter {

    override fun initPresent() = when (Util.firstBoot) {
        true -> mView.changeUI()
        false -> mView.firstBoot()
    }


    override fun move() {
        Handler(Looper.getMainLooper()).postDelayed({
            mView.moveToMain()
        }, 1234)
    }
}
package io.wiffy.gachonNoti.ui.main.information.credit

import android.content.Context
import io.wiffy.gachonNoti.model.SuperContract

interface CreditContract {
    abstract class View : SuperContract.SuperFragment() {
        abstract fun initView()
        abstract fun sendContext(): Context?
    }

    interface Presenter : SuperContract.WiffyObject {
        fun initPresent()
    }
}
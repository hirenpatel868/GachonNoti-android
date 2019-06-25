package io.wiffy.gachonNoti.ui.main

import android.view.View
import androidx.fragment.app.Fragment
import io.wiffy.gachonNoti.ui.main.notification.Parse
import io.wiffy.gachonNoti.ui.main.notification.ParseList

interface MainContract {
    interface View{
        fun changeUI(mList:ArrayList<Fragment>)
        fun builderUp()
        fun builderDismiss()
        fun makeToast(str:String)
    }
    interface Presenter{
        fun initPresent()
    }

    interface FragmentNotification{
        fun changeUI(list:ParseList)
        fun updateUI(list:ParseList)
        fun showLoad()
        fun dismissLoad()
    }
    interface PresenterNotification{
        fun initPresent()
        fun load()
        fun update(data: ParseList)
        fun show()
        fun dismiss()
        fun request()
    }

    interface FragmentSetting{

    }
    interface PresenterSetting
    {
        fun initPresent()

    }
}
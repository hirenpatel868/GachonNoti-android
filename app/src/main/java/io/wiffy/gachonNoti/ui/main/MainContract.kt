package io.wiffy.gachonNoti.ui.main


import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

interface MainContract {
    abstract class View : AppCompatActivity() {
        abstract fun changeUI(mList: ArrayList<Fragment?>)
        abstract fun builderUp()
        abstract fun builderDismiss(): Boolean?
        abstract fun updatedContents()
        abstract fun setTabText(str: String)
        abstract fun allThemeChange()
        abstract fun themeChange()
    }

    interface Presenter {
        fun initPresent()
        fun changeThemes()
        fun resetData(): AlertDialog
    }

}
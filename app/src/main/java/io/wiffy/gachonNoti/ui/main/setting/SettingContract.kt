package io.wiffy.gachonNoti.ui.main.setting

interface SettingContract {
    interface View {
        fun changeView()
        fun executeTask(query:String)
        fun builderUp()
        fun builderDismissAndContactUp()
    }

    interface Presenter {
        fun initPresent()

    }
}
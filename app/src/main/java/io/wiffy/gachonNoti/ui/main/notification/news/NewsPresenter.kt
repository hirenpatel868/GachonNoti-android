package io.wiffy.gachonNoti.ui.main.notification.news

import android.content.Context
import io.wiffy.gachonNoti.model.ParseList
import io.wiffy.gachonNoti.model.Util
import io.wiffy.gachonNoti.ui.main.notification.NotificationComponentAsyncTask
import io.wiffy.gachonNoti.ui.main.notification.NotificationComponentContract

class NewsPresenter(val mView: NotificationComponentContract.View, private val context: Context?) : NotificationComponentContract.Presenter {

    private var list = ParseList()
    private var isLoading = true

    override fun initPresent() {
        mView.changeUI(list)
        NotificationComponentAsyncTask(list,this,context,2,null).execute()
    }

    override fun load() {
        if (!isLoading) {
            request()
        }
    }

    override fun internetInterrupted() = mView.internetUnusable()

    override fun internetNotInterrupted() = mView.internetUsable()


    override fun update(data: ParseList) {
        isLoading = false
        list = data
        mView.updateUI(list)
    }

    override fun show() = mView.showLoad()


    override fun dismiss() = mView.dismissLoad()

    override fun request() {
        isLoading = true
        NotificationComponentAsyncTask(list,this,context,2,null).execute()
    }
    override fun search(src: String) {
        Util.NewsIndex = 0
        list.clear()
        NotificationComponentAsyncTask(list,this,context,2,src).execute()
    }
    override fun resetList() {
        Util.NewsIndex = 0
        list.clear()
        NotificationComponentAsyncTask(list,this,context,2,null).execute()
    }
}
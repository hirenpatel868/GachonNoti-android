package io.wiffy.gachonNoti.ui.main.notification.news

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.wiffy.gachonNoti.R
import io.wiffy.gachonNoti.model.ParseList
import io.wiffy.gachonNoti.model.Util.Companion.getThemeColor
import io.wiffy.gachonNoti.model.VerticalSpaceItemDecoration
import io.wiffy.gachonNoti.model.adapter.NotificationComponentAdapter
import io.wiffy.gachonNoti.ui.main.MainActivity
import io.wiffy.gachonNoti.ui.main.notification.NotificationComponentContract
import kotlinx.android.synthetic.main.fragment_notification_news.view.*

class NewsFragment : NotificationComponentContract.View() {
    lateinit var myView: View
    lateinit var mPresenter: NewsPresenter
    lateinit var adapter: NotificationComponentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.fragment_notification_news, container, false)

        mPresenter = NewsPresenter(this, context)
        myView.swipe2?.setOnRefreshListener {
            mPresenter.resetList()
            myView.swipe2?.isRefreshing = false
        }
        mPresenter.initPresent()
        return myView
    }

    override fun changeUI(list: ParseList) {
        adapter = NotificationComponentAdapter(
            list,
            activity?.applicationContext!!,
            activity as MainActivity,
            2
        )
        myView.recylcer2.adapter = adapter
        myView.recylcer2.layoutManager = LinearLayoutManager(activity?.applicationContext!!)
        myView.recylcer2.addItemDecoration(
            VerticalSpaceItemDecoration(
                2
            )
        )
        myView.recylcer2.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    mPresenter.load()
                }
            }
        })

        changeTheme()
    }

    override fun internetUnusable() = Handler(Looper.getMainLooper()).post {
        myView.par2.visibility = View.GONE
        myView.lottieXX2.visibility = View.VISIBLE
    }


    override fun internetUsable() = Handler(Looper.getMainLooper()).post {
        myView.par2.visibility = View.VISIBLE
        myView.lottieXX2.visibility = View.GONE
    }


    override fun changeTheme() = myView.swipe2.setColorSchemeColors(resources.getColor(getThemeColor()))

    override fun updateUI(list: ParseList) = adapter.update(list)

    override fun showLoad() = MainActivity.mView.builderUp()


    override fun dismissLoad() = MainActivity.mView.builderDismiss()


    override fun search(str: String) = mPresenter.search(str)

}
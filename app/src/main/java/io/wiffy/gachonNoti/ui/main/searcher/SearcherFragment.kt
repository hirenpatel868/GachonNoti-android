package io.wiffy.gachonNoti.ui.main.searcher


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import io.wiffy.gachonNoti.R
import io.wiffy.gachonNoti.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_searcher.view.*
import com.github.eunsiljo.timetablelib.data.TimeTableData
import com.github.eunsiljo.timetablelib.view.TimeTableView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.skydoves.whatif.whatIfNotNull
import io.wiffy.gachonNoti.utils.*
import io.wiffy.gachonNoti.model.`object`.Component
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class SearcherFragment : SearchContract.View() {
    lateinit var myView: View
    lateinit var mPresenter: SearcherPresenter

    var builder: SearchDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myView = inflater.inflate(R.layout.fragment_searcher, container, false)
        mPresenter = SearcherPresenter(this)
        mPresenter.initPresent()
        return myView
    }

    override fun initUI() {
        Glide.with(this).load(R.drawable.search).into(myView.fab)

        myView.fab.setOnClickListener {
            Component.getBuilder()?.show()

            builder = SearchDialog(context!!, mPresenter)
            builder?.show()
        }

        themeChanger()
        setTimeTable(null, "")
    }

    @SuppressLint("ApplySharedPref")
    fun resetDialog() = MaterialAlertDialogBuilder(context).apply {

        setMessage("저장된 데이터를 재설정 할까요?")
        setPositiveButton(
            "OK"
        ) { _, _ ->
            for (V in Component.timeTableSet.iterator()) {
                removeSharedItem(V)
            }
            Component.timeTableSet.clear()
            setSharedItem("tableItems", Component.timeTableSet)

            this@SearcherFragment.searcherVisible(true)

            Component.titles[STATE_SEARCHER] = Pair("강의실", true)
            MainActivity.mView.title = Component.titles[STATE_SEARCHER].first

            builder = SearchDialog(context, mPresenter)
            builder?.show()
        }
    }.show()!!


    fun themeChanger() {
        if (Component.darkTheme) {
            myView.blblblbl2.setTextColor(resources.getColor(R.color.myDarkLight))
            myView.blblblbl.setTextColor(resources.getColor(R.color.white))
            myView.fab.backgroundTintList = resources.getColorStateList(R.color.myDarkLight)
            myView.search_background.setBackgroundColor(resources.getColor(R.color.myDarkDeep))
            myView.search_card.setCardBackgroundColor(resources.getColor(getDarkColor1()))
        } else {
            myView.fab.backgroundTintList = resources.getColorStateList(getThemeColor())
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setTimeTable(arr: ArrayList<TimeTableData>?, name: String) {
        myView.timetable.setStartHour(9)
        myView.timetable.setShowHeader(true)
        myView.timetable.setTableMode(TimeTableView.TableMode.SHORT)

        arr.whatIfNotNull(
            whatIf = {
                myView.timetable.setOnTimeItemClickListener { _, _, data ->
                    toast(data.time.title)
                }
                myView.timetable.setTimeTable(0, arr)

                Component.titles[STATE_SEARCHER] = Pair(
                    "(${when (Component.SEMESTER) {
                        1 -> "1"
                        2 -> "2"
                        3 -> "여름"
                        else -> "겨울"
                    }}학기) $name ", true
                )
                MainActivity.mView.title = Component.titles[STATE_SEARCHER].first
                themeChanger()
                this@SearcherFragment.searcherVisible(false)

            },
            whatIfNot = { this@SearcherFragment.searcherVisible(true) })
    }

    override fun searcherVisible(bool: Boolean) = if (bool) {
        myView.showtu.visibility = View.VISIBLE
        myView.timetable.visibility = View.GONE
    } else {
        myView.showtu.visibility = View.GONE
        myView.timetable.visibility = View.VISIBLE
    }
}
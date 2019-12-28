package io.wiffy.gachonNoti.model.`object`

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.wiffy.gachonNoti.R
import org.json.JSONObject

@Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
object Component {

    lateinit var sharedPreferences: SharedPreferences

    var mHandler: Handler? = null

    var myObject:JSONObject? = null

    var mFragmentList:ArrayList<Fragment?>?=null

    var error = true

    var darkTheme = false

    var isNew = false

    var isBirthday = false

    var adminMode = false

    var YEAR = "2020"

    var isLogin = false

    var surfing = false

    var SEMESTER = 1

    var firstBoot = true

    var looper = true

    var version = "1.0.0"

    var notificationSet = true

    var delay = 100L

    var state = 0

    var helper = "인터넷 연결을 확인해주세요."

    var theme = "default"

    var noneVisible = false

    var campus = true

    var newActivity = true

    lateinit var timeTableSet: HashSet<String>

    private var builder: Dialog? = null

    var titles = arrayListOf(
        Pair("가천알림이", false),
        Pair("내 정보", false),
        Pair("강의실", true),
        Pair("설정", false),
        Pair("가천알림이", false)
    )

    var colorCount = 0

    var deviceWidth = 0
    var deviceHeight = 0

    val timeTableColor = arrayListOf(
        R.color.ran1,
        R.color.ran2,
        R.color.ran3,
        R.color.ran4,
        R.color.ran5,
        R.color.ran6,
        R.color.ran7,
        R.color.ran8
    )

    fun getBuilder(): Dialog? = builder

    fun setBuilder(activity: AppCompatActivity) {
        builder = object : Dialog(activity) {
            override fun show() {
                if (!isShowing) {
                    if (mHandler == null) mHandler = Handler(Looper.getMainLooper())
                    mHandler?.post {
                        super.show()
                    }
                }
            }

            override fun dismiss() {
                if (isShowing) {
                    if (mHandler == null) mHandler = Handler(Looper.getMainLooper())
                    mHandler?.post {
                        super.dismiss()
                    }
                }
            }
        }.apply {
            setContentView(R.layout.builder)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            this.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

}

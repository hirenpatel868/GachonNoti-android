package io.wiffy.gachonNoti.ui.main.setting

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.google.firebase.messaging.FirebaseMessaging
import io.wiffy.gachonNoti.R
import io.wiffy.gachonNoti.model.Util
import io.wiffy.gachonNoti.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_setting.view.*


class SettingFragment : Fragment(), SettingContract.View {
    lateinit var myView: View
    lateinit var mPresenter: SettingPresenter
    lateinit var builder: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.fragment_setting, container, false)
        builder = Dialog(activity!!)
        builder.setContentView(R.layout.builder)
        builder.setCancelable(false)
        builder.setCanceledOnTouchOutside(false)
        builder.window?.setBackgroundDrawableResource(android.R.color.transparent)
        mPresenter = SettingPresenter(this)
        mPresenter.initPresent()

        return myView
    }


    override fun changeView() {
        myView.notiSwitch.isChecked = Util.notifiSet
        themeChanger()

        myView.notiSwitch.setOnCheckedChangeListener { switch, isChecked ->
            when (isChecked) {
                false -> setOff()
                true -> {
                    if (NotificationManagerCompat.from(activity?.applicationContext!!)
                            .areNotificationsEnabled()
                    ) {
                        setOn()
                    } else {
                        switch.isChecked = false
                        setOff()

                        val tent = Intent()
                        tent.action = "android.settings.APP_NOTIFICATION_SETTINGS"

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            tent.putExtra("android.provider.extra.APP_PACKAGE", activity?.packageName)

                        } else {
                            tent.putExtra("app_package", activity?.packageName)
                            tent.putExtra("app_uid", activity?.applicationInfo?.uid)
                        }
                        startActivity(tent)
                        Toast.makeText(activity?.applicationContext, R.string.ssss, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        myView.detailSetting.setOnClickListener {
            val builder = DetailDialog(context!!)
            builder.show()
            builder.setListener(
                View.OnClickListener {
                    builder.okListen()
                    (activity as MainActivity).themeChange()
                    (activity as MainActivity).mPresenter.changeThemes()
                    builder.dismiss()
                },
                View.OnClickListener {
                    builder.dismiss()
                })

        }
        myView.money.setOnClickListener {
            Util.novisible = true
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://wiffy.io/gachon/donation")))
        }
        myView.maker.setOnClickListener {
            Util.novisible = true
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/wiffy-io")))
        }
        myView.source.setOnClickListener {
            val builder = AlertDialog.Builder(activity, R.style.light_dialog)
            builder.setTitle(resources.getString(R.string.source))
            builder.setMessage(resources.getString(R.string.open))
            builder.setPositiveButton(
                "OK"
            ) { _, _ -> }
            builder.show()
        }
        myView.helper.setOnClickListener {
            val builder = AlertDialog.Builder(activity, R.style.light_dialog)
            builder.setTitle("후원 목록")
            builder.setMessage(Util.helper)
            builder.setPositiveButton(
                "OK"
            ) { _, _ -> }
            builder.show()
        }
        myView.version.setOnClickListener {
            val builder = AlertDialog.Builder(activity, R.style.light_dialog)
            builder.setTitle(resources.getString(R.string.version))
            builder.setMessage(resources.getString(R.string.whatVersion))
            builder.setPositiveButton(
                "OK"
            ) { _, _ -> }
            builder.show()
        }
        myView.calling.setOnClickListener {
            val builder = ContactDialog(context!!, this)
            builder.show()
        }

    }

    override fun executeTask(query: String) {
        ContactAsyncTask(this).execute()
    }

    fun themeChanger() {
        val themeColorArray = arrayOf(
            intArrayOf(
                android.R.attr.state_checked
            ),
            intArrayOf(-android.R.attr.state_checked)
        )
        val color = when (Util.theme) {
            "red" -> intArrayOf(resources.getColor(R.color.red), resources.getColor(R.color.gray2))
            "green" -> intArrayOf(resources.getColor(R.color.green), resources.getColor(R.color.gray2))
            else -> intArrayOf(resources.getColor(R.color.main2Blue), resources.getColor(R.color.gray2))
        }
        val lightColor = when (Util.theme) {
            "red" -> intArrayOf(resources.getColor(R.color.lightRed), resources.getColor(R.color.lightGray))
            "green" -> intArrayOf(resources.getColor(R.color.lightGreen), resources.getColor(R.color.lightGray))
            else -> intArrayOf(resources.getColor(R.color.main2LightBlue), resources.getColor(R.color.lightGray))
        }

        myView.notiSwitch.thumbTintList = ColorStateList(themeColorArray, color)
        myView.notiSwitch.trackTintList = ColorStateList(themeColorArray, lightColor)
    }

    override fun builderUp() {
        Handler(Looper.getMainLooper()).post {
            builder.show()
        }
    }

    override fun builderDismissAndContactUp() {
        Handler(Looper.getMainLooper()).post {
            builder.dismiss()
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun setOn() {
        Util.notifiSet = true
        Util.sharedPreferences.edit().putBoolean("notiOn", true).commit()
        FirebaseMessaging.getInstance().subscribeToTopic("noti")
    }

    @SuppressLint("ApplySharedPref")
    private fun setOff() {
        Util.notifiSet = false
        Util.sharedPreferences.edit().putBoolean("notiOn", false).commit()
        FirebaseMessaging.getInstance().unsubscribeFromTopic("noti")
    }

}
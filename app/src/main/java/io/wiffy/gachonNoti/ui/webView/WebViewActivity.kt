package io.wiffy.gachonNoti.ui.webView

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import io.wiffy.gachonNoti.R
import io.wiffy.gachonNoti.model.Util
import io.wiffy.gachonNoti.model.Util.Companion.getThemeColor
import io.wiffy.gachonNoti.model.Util.Companion.getThemeDeepColor
import io.wiffy.gachonNoti.model.data.Parse
import kotlinx.android.synthetic.main.activity_webview.*
import java.lang.Exception

class WebViewActivity : AppCompatActivity(), WebViewContract.View {

    lateinit var mPresenter: WebViewPresenter
    lateinit var builder: Dialog
    lateinit var bundle: Parse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        with(webview.settings)
        {
            builtInZoomControls = true
            setSupportZoom(true)
            cacheMode = WebSettings.LOAD_NO_CACHE
            setAppCacheEnabled(false)
        }
        initBuild()
        bundle = (intent.getSerializableExtra("bundle") as Parse)
        title = "${bundle.value} ${bundle.data}"
        webview_text.text = bundle.text
        window.statusBarColor = resources.getColor(R.color.mainBlue)
        mPresenter = WebViewPresenter(this)
        mPresenter.initPresent(bundle.link)
        themeChange()
    }

    private fun initBuild() {
        Glide.with(this).load(R.drawable.defaults).into(logo_splash3)
        builder = Dialog(this@WebViewActivity).apply {
            setContentView(R.layout.builder)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            this.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

    }

    override fun builderUp() = builder.show()


    override fun builderDismiss() = builder.dismiss()


    @SuppressLint("SetJavaScriptEnabled")
    override fun changeUI(javaS: String) = try {
        webview.loadDataWithBaseURL("", javaS, "text/html", "UTF-8", "");
        webview.settings?.javaScriptEnabled = true
    } catch (e: Exception) {

    }


    override fun onUserLeaveHint() {
        invisible()
        super.onUserLeaveHint()
    }

    private fun visible() {
        webview_layout.visibility = View.VISIBLE
        web_splash.visibility = View.GONE
        webview_layout.invalidate()
        web_splash.invalidate()
    }

    private fun invisible() {
        if (!Util.novisible) {
            webview_layout.visibility = View.GONE
            web_splash.visibility = View.VISIBLE
            webview_layout.invalidate()
            web_splash.invalidate()
        }
    }

    override fun onAttachedToWindow() {
        visible()
        super.onAttachedToWindow()
    }

    override fun onStart() {
        visible()
        Util.surfing = true
        super.onStart()
    }

    override fun onResume() {
        Util.novisible = false
        visible()
        super.onResume()
    }


    override fun onPause() {
        invisible()
        super.onPause()
    }

    override fun onStop() {
        invisible()
        Util.surfing = false
        Util.novisible = false
        super.onStop()
    }

    override fun onDetachedFromWindow() {
        invisible()
        super.onDetachedFromWindow()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Util.surfing = false
        Util.novisible = true
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu, menu)
        return true
    }

    private fun goPage() = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(bundle.link)))


    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.actionmy -> {
            goPage()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    private fun themeChange() {
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(getThemeColor())))
        webview_layout.setBackgroundColor(resources.getColor(getThemeDeepColor()))
        window.statusBarColor = resources.getColor(getThemeColor())
    }
}
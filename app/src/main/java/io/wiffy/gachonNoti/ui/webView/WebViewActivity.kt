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
import com.bumptech.glide.Glide
import io.wiffy.gachonNoti.R
import io.wiffy.gachonNoti.utils.getThemeColor
import io.wiffy.gachonNoti.utils.getThemeDeepColor
import io.wiffy.gachonNoti.model.Parse
import io.wiffy.gachonNoti.model.`object`.Component
import io.wiffy.gachonNoti.utils.getDarkColor1
import io.wiffy.gachonNoti.utils.getDarkColor2
import kotlinx.android.synthetic.main.activity_webview.*
import java.lang.Exception

@Suppress("DEPRECATION")
class WebViewActivity : WebViewContract.View() {

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
        mPresenter = WebViewPresenter(this, bundle.link)
        mPresenter.initPresent()
        themeChange()
    }

    override fun mainNotification() {
        webview.settings.setSupportZoom(true)
        webview.settings.builtInZoomControls = false
        webview.loadUrl(bundle.link)
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
        if (!Component.noneVisible) {
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
        Component.surfing = true
        super.onStart()
    }

    override fun onResume() {
        Component.noneVisible = false
        visible()
        super.onResume()
    }


    override fun onPause() {
        invisible()
        super.onPause()
    }

    override fun onStop() {
        invisible()
        Component.surfing = false
        Component.noneVisible = false
        super.onStop()
    }

    override fun onDetachedFromWindow() {
        invisible()
        super.onDetachedFromWindow()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Component.surfing = false
        Component.noneVisible = true
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
        else -> {
            super.onOptionsItemSelected(item!!)
        }
    }

    private fun themeChange() {
        supportActionBar!!.setBackgroundDrawable(
            ColorDrawable(
                if (Component.darkTheme) {
                    resources.getColor(
                        getDarkColor2()
                    )
                } else {
                    resources.getColor(getThemeColor())
                }
            )
        )
        webview_layout.setBackgroundColor(
            if (Component.darkTheme) {
                resources.getColor(
                    getDarkColor1()
                )
            } else {
                resources.getColor(getThemeDeepColor())
            }
        )
        window.statusBarColor = if (Component.darkTheme) {
            resources.getColor(
                getDarkColor2()
            )
        } else {
            resources.getColor(getThemeColor())
        }
    }
}
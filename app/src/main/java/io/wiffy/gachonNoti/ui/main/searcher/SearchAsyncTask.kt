package io.wiffy.gachonNoti.ui.main.searcher

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Log
import io.wiffy.gachonNoti.model.Util
import io.wiffy.gachonNoti.ui.main.MainContract
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.nio.ByteBuffer
import java.nio.charset.Charset


class SearchAsyncTask(
    private val data: String,
    cate: String,
    yearsemester: String,
    private val mPresenter: MainContract.PresenterSearcher
) :
    AsyncTask<Void, Void, Int>() {

    private var done = ""
    private var cate = cate
    private var yearsemester = yearsemester

    override fun onPreExecute() {
        Handler(Looper.getMainLooper()).post {

        }
    }

    override fun doInBackground(vararg params: Void?): Int {
        val url = "http://gcis.gachon.ac.kr/haksa/src/jsp/ssu/ssu1000q.jsp?"
        val url2 =
            "groupType=20&searchYear=2019&searchTerm=10&$data&operationType=MAINSEARCH&comType=DEPT_TOT_CD&comViewValue=N&comResultTarget=cbDeptCD&condition1=CS0000&condition2=20&condition3=TOT"
        try {
            //Log.d("asdf",url+url2)
            try {
                val Url = URL(url + url2)
                val conn = Url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.instanceFollowRedirects = true
                HttpURLConnection.setFollowRedirects(true)

                conn.doOutput = true
                conn.doInput = true
                conn.useCaches = false
                conn.defaultUseCaches = false
                val `is` = conn.inputStream
                val builder = StringBuilder()
                val reader = BufferedReader(InputStreamReader(`is`, "euc-kr"))

                var line = reader.readLine()
                while (line != null) {
                    builder.append(line + "\n")
                    line = reader.readLine()
                }

                done = builder.toString()
                Log.d("asdf", done)

            } catch (e: Exception) {
            }


        } catch (e: Exception) {

        }

        return 0
    }


    override fun onPostExecute(result: Int?) {
        Handler(Looper.getMainLooper()).post {
            if (done.contains("<haksuNo>")) {
                Util.sharedPreferences.edit().putString("$yearsemester-$cate", done).commit()
                mPresenter.dismissLoad()
            } else {
                mPresenter.error()
            }


        }
    }
}
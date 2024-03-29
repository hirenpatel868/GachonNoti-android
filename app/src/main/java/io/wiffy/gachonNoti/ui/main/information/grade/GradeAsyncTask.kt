package io.wiffy.gachonNoti.ui.main.information.grade

import io.wiffy.extension.isNetworkConnected
import io.wiffy.gachonNoti.model.`object`.Component
import io.wiffy.gachonNoti.utils.ACTION_FAILURE
import io.wiffy.gachonNoti.utils.ACTION_SUCCESS
import io.wiffy.gachonNoti.model.CreditAverage
import io.wiffy.gachonNoti.model.CreditFormal
import io.wiffy.gachonNoti.model.SuperContract
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.json.JSONObject

class GradeAsyncTask(
    private val mView: GradeContract.View, private val number: String,
    private val mYear: String = "", private val mSemester: String = "",
    private val mGrade: String = ""
) :
    SuperContract.SuperAsyncTask<Void, Void, Int>() {

    var creditAverage: CreditAverage? = null
    private val creditList = ArrayList<CreditFormal>()

    //getJson("AVG_SQL")
    //getJson("SQL")

    private fun getJson(str: String) = JSONObject().apply {
        put("fsp_cmd", "executeAjaxMap")
        put("fsp_action", "AffairsAction")
        put("GRADE", mGrade)
        put(
            "jsonParams",
            "{\"STUDENT_NO\":\"$number\",\"YEAR\":\"${mYear}\",\"TERM_CD\":\"${mSemester}\",\"GRADE\":\"${mGrade}\",\"SQL_ID\":\"mobile/affairs:EXAM_GRADE_STUDENT_${str}_S01\",\"fsp_action\":\"AffairsAction\",\"fsp_cmd\":\"executeAjaxMap\"}"
        )
        put("SQL_ID", "mobile/affairs:EXAM_GRADE_STUDENT_${str}_S01")
        put("STUDENT_NO", number)
        put("TERM_CD", mSemester)
        put("YEAR", mYear)
    }

    override fun onPreExecute() {
        Component.getBuilder()?.show()
    }

    override fun doInBackground(vararg params: Void?): Int {
        if (!isNetworkConnected(mView.sendContext()!!)) {
            return ACTION_FAILURE
        }
        return try {
            JSONObject(EntityUtils.toString(DefaultHttpClient().execute(HttpPost("http://smart.gachon.ac.kr:8080//WebJSON").apply {
                entity = StringEntity(getJson("AVG_SQL").toString())
            }).entity)).getJSONArray("ds_list").let {
                creditAverage = it.getJSONObject(0).run {
                    CreditAverage(
                        getDouble("CREDIT"),
                        getDouble("SCORE"),
                        getDouble("MARK")
                    )
                }
            }
            JSONObject(EntityUtils.toString(DefaultHttpClient().execute(HttpPost("http://smart.gachon.ac.kr:8080//WebJSON").apply {
                entity = StringEntity(getJson("SQL").toString())
            }).entity)).getJSONArray("ds_list").apply {
                for (n in 0 until length()) {
                    creditList.add(getJSONObject(n).run {
                        CreditFormal(
                            getString("YEAR"),
                            getString("TERM_CD"),
                            getString("SUBJECT_NM"),
                            getInt("GRADE"),
                            getInt("CREDIT"),
                            getString("MARK")
                        )
                    })
                }
            }
            ACTION_SUCCESS
        } catch (e: Exception) {
            333
        }
    }

    override fun onPostExecute(result: Int?) {
        Component.getBuilder()?.dismiss()
        when (result) {
            ACTION_SUCCESS -> mView.setView(creditAverage, creditList)
            ACTION_FAILURE -> mView.toast("인터넷 연결을 확인해주세요.")
            else -> mView.setView(CreditAverage(0.0, 0.0, 0.0), ArrayList())
        }
    }
}
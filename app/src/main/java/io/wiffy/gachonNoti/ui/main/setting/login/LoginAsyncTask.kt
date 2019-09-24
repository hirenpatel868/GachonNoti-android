package io.wiffy.gachonNoti.ui.main.setting.login

import android.os.Handler
import android.os.Looper
import io.wiffy.gachonNoti.`object`.Component
import io.wiffy.gachonNoti.function.*
import io.wiffy.gachonNoti.model.StudentInformation
import io.wiffy.gachonNoti.model.SuperContract
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.json.JSONObject
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.Exception


class LoginAsyncTask(
    private val ids: String,
    private val password: String,
    val mView: LoginDialog
) : SuperContract.SuperAsyncTask<Void, Void, Int>() {

    lateinit var studentInformation: StudentInformation
    private val strBuilder = StringBuilder()

    override fun onPreExecute() {
        Component.getBuilder()?.show()
    }

    override fun doInBackground(vararg params: Void?): Int {
        if (!isNetworkConnected(mView.sendContext())) {
            return ACTION_NETWORK_FAILURE
        }
        val number: String
        try {
            val sendObject = JSONObject().apply {
                put("fsp_cmd", "login")
                put("DVIC_ID", "dwFraM1pVhl6mMn4npgL2dtZw7pZxw2lo2uqpm1yuMs=")
                put("fsp_action", "UserAction")
                put("LOGIN_ID", ids)
                put("USER_ID", ids)
                put("PWD", password)
                put("APPS_ID", "com.sz.Atwee.gachon")
            }

            // TODO 학번만 가져온후에 다른걸로 보내기

            JSONObject(EntityUtils.toString(DefaultHttpClient().execute(HttpPost("http://smart.gachon.ac.kr:8080//WebJSON").apply {
                entity = StringEntity(sendObject.toString())
            }).entity)).getJSONObject("ds_output").apply {
                number = getString("userUniqNo")
                studentInformation = StudentInformation(
                    getString("userNm"),
                    number,
                    ids,
                    password,
                    try {
                        getJSONArray("clubList").getJSONObject(0).getString("clubNm")
                    } catch (e: Exception) {
                        strBuilder.append("clubList : ${e.message}\n")
                        "undefined"
                    },
                    "http://gcis.gachon.ac.kr/common/picture/haksa/shj/$number.jpg",
                    try {
                        getString("affiCd")
                    } catch (e: Exception) {
                        strBuilder.append("affiCd : ${e.message}\n")
                        "undefined"
                    }
                )
            }

            try {
                val data =
                    (DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                        InputSource(
                            StringReader(
                                EntityUtils.toString(
                                    DefaultHttpClient().execute(
                                        HttpPost("http://gcis.gachon.ac.kr/common/src/jsp/comComponent/componentList.jsp?comType=STU%5FCOM%5FINFO&comViewValue=N&comResultTarget=cbGroupCD&condition1=$number")
                                    ).entity
                                ).replace(
                                    "<?xml version='1.0' encoding='EUC-KR'?>",
                                    ""
                                )
                            )
                        )
                    ).getElementsByTagName("rsCommonInfo").item(0) as Element).getElementsByTagName(
                        "jumin"
                    ).item(0)
                        .childNodes.item(0).nodeValue.split("-")
                setSharedItem("birthday", data[0])
                setSharedItem("gender", data[1][0].toInt() % 2 == 1)
            } catch (e: Exception) {
                strBuilder.append("birthday : ${e.message}\n")
            }

        } catch (e: Exception) {
            strBuilder.append("functionError : ${e.message}\n")
            return ACTION_FAILURE
        }
        return ACTION_SUCCESS
    }

    override fun onPostExecute(result: Int?) {
        Handler(Looper.getMainLooper()).post {
            Component.getBuilder()?.dismiss()
        }
        when (result ?: ACTION_FAILURE) {
            ACTION_SUCCESS -> {
                mView.saveInformation(studentInformation)
            }
            ACTION_NETWORK_FAILURE -> {
                mView.networkFailed()
            }
            else -> {
                mView.loginFailed(strBuilder.toString())
            }
        }
    }
}
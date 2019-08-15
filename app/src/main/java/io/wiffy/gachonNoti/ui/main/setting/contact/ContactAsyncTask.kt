package io.wiffy.gachonNoti.ui.main.setting.contact

import android.os.AsyncTask
import io.wiffy.gachonNoti.model.Util
import io.wiffy.gachonNoti.model.data.ContactInformation
import io.wiffy.gachonNoti.ui.main.setting.SettingContract
import org.jsoup.Jsoup
import java.lang.Exception
import java.net.URL

class ContactAsyncTask(private val myView: SettingContract.View, private val query: String) :
    AsyncTask<Void, Void, Int>() {
    private val myList = ArrayList<ContactInformation>()

    override fun onPreExecute() {
        myView.builderUp()
    }

    override fun doInBackground(vararg params: Void?): Int {
        try {
            val mElements = Jsoup.parseBodyFragment(URL(query).readText()).select("div.boardlist table.data")
            val rows = mElements.select("tr")

            if (rows.size > 1)
                for (n in 1 until rows.size) {
                    try {
                        val tel = rows[n].select("td")[3].text()

                        myList.add(
                            ContactInformation(
                                rows[n].select("td")[0].text(),
                                rows[n].select("td")[2].text(),
                                when (tel.length) {
                                    10 -> "${tel.substring(0, 3)}-${tel.substring(3, 6)}-${tel.substring(6)}"
                                    else -> tel
                                }
                            )
                        )
                    } catch (e: Exception) {
                    }
                }
        } catch (e: Exception) {
        }
        return Util.ACTION_SUCCESS
    }

    override fun onPostExecute(result: Int?) {

        myView.builderDismissAndContactUp(myList)
    }
}
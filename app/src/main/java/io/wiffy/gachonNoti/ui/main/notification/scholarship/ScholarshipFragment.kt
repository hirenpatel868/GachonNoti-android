package io.wiffy.gachonNoti.ui.main.notification.scholarship

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.wiffy.gachonNoti.R

class ScholarshipFragment : Fragment() {
    lateinit var myView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.fragment_notification_scholarship, container, false)

        return myView
    }

    fun changeTheme()
    {

    }
}
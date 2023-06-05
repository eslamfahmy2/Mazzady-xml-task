package com.example.mazzadytask.presentation.ui

import android.app.Activity
import android.content.BroadcastReceiver
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mazzadytask.R
import com.example.mazzadytask.utils.NoConnectionReceiver
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //listen for network state
    private lateinit var internetConnectionBroadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        internetConnectionBroadcastReceiver =
            NoConnectionReceiver(WeakReference<Activity>(this).get() ?: this)
    }
}

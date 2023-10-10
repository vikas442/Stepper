package com.example.gpayintegration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gpayintegration.databinding.ActivityStepperSampleBinding

class StepperSampleActivity : AppCompatActivity() {
    private lateinit var layout: ActivityStepperSampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityStepperSampleBinding.inflate(layoutInflater)
        setContentView(layout.root)


    }
}

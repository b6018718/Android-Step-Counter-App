package com.example.healthapp

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {
    // Sensor variables
    private lateinit var sensorManager: SensorManager
    private var mSensors: Sensor? = null



    // Step variables
    private var startingSteps:Float = 0F
    private var currentSteps:Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(isCompatibleWithStepCounter()) {
            println("Compatible")
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            mSensors = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            sensorManager.registerListener(this, mSensors, SensorManager.SENSOR_DELAY_NORMAL)

        }else {
            println("Incompatible")
            stepText.text = "Sorry, your phone doesn't have a step sensor"
        }
    }

    fun isCompatibleWithStepCounter(): Boolean {
        val pm: PackageManager = packageManager
        // Get the version number
        val apiVersion: Int = Build.VERSION.SDK_INT
        // Return a boolean
        return apiVersion >= 19
                && pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
                && pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event!!.sensor.type){
            Sensor.TYPE_STEP_COUNTER ->{
                val stepCounter = event.values[0]
                if(startingSteps < 1){
                    startingSteps = stepCounter
                }
                currentSteps = stepCounter - startingSteps
            }
            Sensor.TYPE_STEP_DETECTOR -> {
                currentSteps += event.values.size
            }

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume(){
        super.onResume()
        // Register the sensor when the app starts
        stepText.text = currentSteps.toInt().toString()
    }

    override fun onPause() {
        super.onPause()

    }

}

package com.ishant.dumboo.application
/*Android Developer
* Ishant Sharma
* Java and Kotlin
*
* */
import android.app.Application
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp(MultiDexApplication::class)
class DumbooApplication  : Hilt_DumbooApplication(){

}
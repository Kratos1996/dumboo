package com.ishant.dumboo.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.media.AudioManager
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.ishant.dumboo.R
import com.ishant.dumboo.application.DumbooApplication
import com.ishant.dumboo.database.prefrence.SharedPre
import com.ishant.dumboo.repositories.methods.MethodsRepo
import com.ishant.dumboo.ui.home.HomeFragment
import com.ishant.dumboo.ui.home.HomeViewModel
import com.ishant.dumboo.ui.receivers.PhonecallReceiver
import com.ishant.dumboo.ui.receivers.RingtonAccessReceiver
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
abstract class DumbooBaseActivity : AppCompatActivity(){
    val viewmodel: HomeViewModel by viewModels()
    @Inject
    lateinit var manager: FragmentManager
    @Inject
    lateinit var am: AudioManager
    @Inject
    lateinit var methods: MethodsRepo
    @Inject
    lateinit var sharedPre: SharedPre
    private var addToBackStack = false
    private var transaction: FragmentTransaction? = null
    private var fragment: Fragment? = null
    private var rxPermissions: RxPermissions? = null
    private var doubleBackToExitPressedOnce = false
    private var auth: FirebaseAuth? = null
    private lateinit var snackBar: Snackbar
    var isSilentModeActivated = false

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        when (am.getRingerMode()) {
            AudioManager.RINGER_MODE_SILENT -> isSilentModeActivated = true
            AudioManager.RINGER_MODE_VIBRATE -> isSilentModeActivated = true
            AudioManager.RINGER_MODE_NORMAL -> isSilentModeActivated = false
        }

    }

    fun getUtils(): MethodsRepo {
        return methods
    }


    override fun finish() {
        super.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    open fun GetApplicationContext(): Context? {
        return applicationContext
    }

    open fun getRxPermissions(): RxPermissions? {
        if (rxPermissions == null) {
            rxPermissions = RxPermissions(this)
        }
        return rxPermissions
    }


    open fun startFragment(fragment: Fragment?, backStackTag: String?, addToBackStack: Boolean) {
        transaction = manager.beginTransaction()
        this.addToBackStack = addToBackStack
        transaction!!.addToBackStack(backStackTag)
        transaction!!.replace(R.id.container, fragment!!)
        if (!isFinishing && !isDestroyed) {
            transaction!!.commit()
        }
    }

    open fun startFragment(fragment: Fragment?, addToBackStack: Boolean, backStackTag: String?) {
        this.addToBackStack = addToBackStack
        val fragmentPopped = manager.popBackStackImmediate(backStackTag, 0)
        if (!fragmentPopped) {
            transaction = manager.beginTransaction()
            if (addToBackStack) {
                transaction!!.addToBackStack(backStackTag)
            } else {
                transaction!!.addToBackStack(null)
            }
            transaction!!.replace(R.id.container, fragment!!)
            transaction!!.commit()
        }
    }

    open fun startFragment(
        fragment: Fragment?,
        addToBackStack: Boolean,
        backStackTag: String?,
        wantAnimation: Boolean
    ) {
        this.addToBackStack = addToBackStack
        val fragmentPopped = manager.popBackStackImmediate(backStackTag, 0)
        if (!fragmentPopped) {
            transaction = manager.beginTransaction()
            if (wantAnimation) {
                transaction!!.setCustomAnimations(R.anim.slide_up, 0, 0, 0)
            }
            if (addToBackStack) {
                transaction!!.addToBackStack(backStackTag)
            } else {
                transaction!!.addToBackStack(null)
            }
            transaction!!.replace(R.id.container, fragment!!)
            transaction!!.commit()
        }
    }

    open fun showCustomAlert(
        msg: String?,
        v: View?,
        button: String?,
        isRetryOptionAvailable: Boolean,
        listener: RetrySnackBarClickListener
    ) {
        if (isRetryOptionAvailable) {
            snackBar = Snackbar.make(v!!, msg!!, Snackbar.LENGTH_LONG)
                .setAction(button) { listener.onClickRetry() }
        } else {
            snackBar = Snackbar.make(v!!, msg!!, Snackbar.LENGTH_LONG)
        }
        snackBar.setActionTextColor(Color.BLUE)
        val snackBarView: View = snackBar.getView()
        val textView = snackBarView.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackBar.show()
    }

    open fun showCustomAlert(msg: String?, v: View?) {
        snackBar = Snackbar.make(v!!, msg!!, Snackbar.LENGTH_LONG)
        snackBar.setActionTextColor(Color.BLUE)
        val snackBarView: View = snackBar.getView()
        val textView = snackBarView.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackBar.show()
    }

    override fun onBackPressed() {
        fragment = getCurrentFragment()
        /* for (entry in 0 until manager!!.backStackEntryCount) {
             Log.e("Ishant", "Found fragment: " + manager!!.getBackStackEntryAt(entry).id)
         }*/
        if (addToBackStack) {
            if (fragment is HomeFragment) {
                if (doubleBackToExitPressedOnce) {
                    finish()
                    return
                }
                doubleBackToExitPressedOnce = true
                // showAlertDialog();
                Toast.makeText(this, "Press back again", Toast.LENGTH_SHORT)
                lifecycleScope.launch(Dispatchers.Default) {
                    delay(2000)
                    doubleBackToExitPressedOnce = false
                }
            } else {
                if ( manager.backStackEntryCount > 0) {
                    manager.popBackStackImmediate()
                } else {
                    super.onBackPressed()
                }
            }
        } else {
            super.onBackPressed()
        }
    }

    open fun getCurrentFragment(): Fragment? {
        fragment = supportFragmentManager.findFragmentById(R.id.container)
        return fragment
    }

    open fun hideSoftKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
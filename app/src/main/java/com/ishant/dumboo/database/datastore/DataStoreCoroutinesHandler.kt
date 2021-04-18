package com.ishant.dumboo.database.datastore

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

object DataStoreCoroutinesHandler {
    fun main(work : suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            work()
        }
    // I/O operations
    fun io(work : suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.IO).launch {
            work()
        }

    fun io(viewModel : ViewModel, work : suspend (() -> Unit)) {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            work()
        }
    }
    // Uses heavy CPU computation
    fun default(work : suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Default).launch {
            work()
        }
    // No need to run on specific context
    fun unconfined(work : suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Unconfined).launch {
            work()
        }




}
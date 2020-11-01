package com.ayata.weatherappkotlin.internal

import android.util.Log
import com.google.android.gms.tasks.Task

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

fun<T> Task<T>.asDeferred():Deferred<T>{
    val deferred= CompletableDeferred<T>()
    this.addOnSuccessListener { result->
        Log.d("devicedeferred success", "asDeferred: ");
        deferred.complete(result)
    }
    this.addOnFailureListener {exception->
        Log.d("devicedeferred failed", "asDeferred: ");
        deferred.completeExceptionally(exception)
    }
    return deferred
}
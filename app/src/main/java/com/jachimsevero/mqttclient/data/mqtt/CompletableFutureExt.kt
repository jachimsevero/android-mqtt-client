package com.jachimsevero.mqttclient.data.mqtt

import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> CompletableFuture<T>.await(): T = suspendCancellableCoroutine { cont ->
  this.whenComplete { result, exception ->
    if (exception != null) {
      cont.resumeWithException(exception)
    } else {
      cont.resume(result)
    }
  }

  cont.invokeOnCancellation { this.cancel(true) }
}

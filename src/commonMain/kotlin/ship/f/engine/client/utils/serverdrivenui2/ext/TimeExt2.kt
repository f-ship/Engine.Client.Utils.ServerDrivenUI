package ship.f.engine.client.utils.serverdrivenui2.ext

import kotlinx.coroutines.*

fun createTimer(intervalSeconds: Int, func: () -> Boolean): Job {
    val job = CoroutineScope(Dispatchers.Default).launch {
        while (true) {
            delay(1000L * intervalSeconds)
            if (!func()) {
                cancel()
            }
        }
    }
    return job
}
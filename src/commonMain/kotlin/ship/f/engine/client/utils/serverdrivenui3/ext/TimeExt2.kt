package ship.f.engine.client.utils.serverdrivenui3.ext

//fun createTimer(intervalSeconds: Int, func: () -> Boolean): Job {
//    val job = CoroutineScope(Dispatchers.Default).launch {
//        while (true) {
//            delay(1000L * intervalSeconds)
//            if (!func()) {
//                cancel()
//            }
//        }
//    }
//    return job
//}
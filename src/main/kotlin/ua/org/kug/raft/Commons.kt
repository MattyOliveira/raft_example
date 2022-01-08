package ua.org.kug.raft

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withTimeout
import mu.KotlinLogging
import java.util.*
import kotlin.concurrent.schedule

class ResettableCountdownTimer(private val action: suspend () -> Unit) {

    private val log = KotlinLogging.logger("timer")

    private var timer = startTimer()

    fun reset() {
        log.info { "Timer reseated" }
        timer.cancel()
        timer = startTimer()
    }

    private fun startTimer(): Timer {
        val time = (20_000..23_000).random().toLong()
        val newTimer = Timer()
        newTimer.schedule(time) {
            runBlocking { action() }
        }
        return newTimer
    }
}

fun ClosedRange<Int>.random() =
        Random().nextInt((endInclusive + 1) - start) + start


suspend fun <T> retry(delay: Long = 5000, block: suspend () -> T): T {
    while (true) {
        try {
            return block()
        } catch (e: Exception) {
            // nothing
        }
        delay(delay)
    }
}

class Log<T> {

    var lastIndex = 0

    private val log = mutableListOf<T>()

    fun get(i: Int): T =
            if (lastIndex - 1 < i) throw IndexOutOfBoundsException() else log[i]

    fun add(i: Int, entry: T) =
         when {
            lastIndex == i -> {
                lastIndex += 1
                log.add(entry)
            }
            lastIndex < i -> false
            else -> {
                log[i] = entry
                lastIndex = i + 1
                true
            }
        }

    fun entries() = log.subList(0, lastIndex)
}

fun main(args: Array<String>) = runBlocking {

    withTimeout(10L) {
        delay(15)
        print("hi")
    }
}
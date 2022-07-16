package co.fanavari.myapplication.coroutines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import co.fanavari.myapplication.MainActivity
import co.fanavari.myapplication.R
import co.fanavari.myapplication.databinding.ActivityCoroutinesBinding
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.HttpRetryException
import kotlin.system.measureTimeMillis

class CoroutinesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoroutinesBinding

    val TAG = "Coroutines activity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCoroutinesBinding.inflate(layoutInflater)
        setContentView(binding.root)



        GlobalScope.launch {
            delay(3000L)
            Log.d(TAG, "coroutines from thread ${Thread.currentThread().name}")
        }

        Log.d(TAG, "from thread ${Thread.currentThread().name}")

        //define suspend functions
        GlobalScope.launch {
            val netCallAnswer1 = doNetworkCall1()
            val netCallAnswer2 = doNetworkCall2()
            Log.d(TAG, netCallAnswer1)
            Log.d(TAG, netCallAnswer2)
        }

        //coroutines contexts IO, Main, Default , Unconfined
        GlobalScope.launch(Dispatchers.IO) {
            Log.d(TAG, "coroutines from thread ${Thread.currentThread().name}")
            val netCallAnswer1 = doNetworkCall1()
            withContext(Dispatchers.Main) {
                Log.d(TAG, "text from thread ${Thread.currentThread().name}")
                binding.textViewCoroutineName.text = netCallAnswer1
            }
        }

        //coroutines unblocking
        Log.d(TAG, "before runBlocking")
        runBlocking {
            Log.d(TAG, "start runBlocking")
            delay(5000L)
            Log.d(TAG, "end of runBlocking")
        }
        Log.d(TAG, "after runBlocking")


        Log.d(TAG, "before runBlocking")
        runBlocking {

        }
        Log.d(TAG, "start runBlocking")
        Thread.sleep(5000L)
        Log.d(TAG, "end of runBlocking")
        Log.d(TAG, "after runBlocking")


        Log.d(TAG, "before runBlocking")
        runBlocking {
            launch(Dispatchers.IO) {
                delay(3000L)
                Log.d(TAG, "finish IO 1")
            }
            launch(Dispatchers.IO) {
                delay(3000L)
                Log.d(TAG, "finish IO 2")
            }
            Log.d(TAG, "start runBlocking")
            delay(5000L)
            Log.d(TAG, "end of runBlocking")
        }
        Log.d(TAG, "after runBlocking")


        //Jobs, Waiting, Cancelation
        val job = GlobalScope.launch(Dispatchers.Default) {
            repeat(5) {
                Log.d(TAG, "coroutines is still working...")
                delay(1000L)
            }
        }

        runBlocking {
            job.join()
            Log.d(TAG, "main thread is continuing")
        }

        runBlocking {
            delay(2000L)
            job.cancel()
            Log.d(TAG, "main thread is continuing")
        }

        //cancel heavy calculations
        val job1 = GlobalScope.launch(Dispatchers.Default) {
            repeat(5) {
                Log.d(TAG, "starting long running calculation...")
                for (i in 30..40) {
                    Log.d(TAG, "result for i: ${fib(i)}")
                }
                Log.d(TAG, "ending long running calculation...")
            }
        }

        runBlocking {
            delay(2000L)
            job1.cancel()
            Log.d(TAG, "canceled job2!")
        }


        val job2 = GlobalScope.launch(Dispatchers.Default) {
            repeat(5) {
                Log.d(TAG, "starting long running calculation...")
                for (i in 30..40) {
                    if (isActive) {
                        Log.d(TAG, "result for i: ${fib(i)}")
                    }
                }
                Log.d(TAG, "ending long running calculation...")
            }
        }

        runBlocking {
            delay(2000L)
            job2.cancel()
            Log.d(TAG, "canceled job2!")
        }


        val job3 = GlobalScope.launch(Dispatchers.Default) {
            repeat(5) {
                Log.d(TAG, "starting long running calculation...")
                withTimeout(3000L) {
                    for (i in 30..40) {
                        if (isActive) {
                            Log.d(TAG, "result for i: ${fib(i)}")
                        }
                    }
                }
                Log.d(TAG, "ending long running calculation...")
            }
        }


        //Async and await
        GlobalScope.launch {
            val time = measureTimeMillis {
                val netCallAnswer1 = doNetworkCall1()
                val netCallAnswer2 = doNetworkCall2()
                Log.d(TAG, "answer1: $netCallAnswer1")
                Log.d(TAG, "answer2: $netCallAnswer2")
            }
            Log.d(TAG, "requests took $time ms.")
        }

        GlobalScope.launch {
            val time = measureTimeMillis {
                var netCallAnswer1: String? = null
                var netCallAnswer2: String? = null
                val jobC1 = launch { netCallAnswer1 = doNetworkCall1() }
                val jobC2 = launch { netCallAnswer2 = doNetworkCall1() }
                jobC1.join()
                jobC2.join()
                Log.d(TAG, "answer1: $netCallAnswer1")
                Log.d(TAG, "answer2: $netCallAnswer2")
            }
            Log.d(TAG, "requests took $time ms.")
        }

        GlobalScope.launch {
            val time = measureTimeMillis {
                val netCallAnswer1 = async { doNetworkCall1() }
                val netCallAnswer2 = async { doNetworkCall2() }
                Log.d(TAG, "answer1: ${netCallAnswer1.await()}")
                Log.d(TAG, "answer2: ${netCallAnswer2.await()}")
            }
            Log.d(TAG, "requests took $time ms.")
        }


        //lifecycleScope
        binding.buttonStartSecondActivity.setOnClickListener {
            GlobalScope.launch {
                while (true) {
                    delay(1000L)
                    Log.d(TAG, "still working...")
                }
            }
            GlobalScope.launch {
                delay(5000L)
                Intent(this@CoroutinesActivity, MainActivity::class.java).also {
                    startActivity(intent)
                    finish()
                }
            }
        }



        binding.buttonStartSecondActivity.setOnClickListener {
            lifecycleScope.launch { // viewModelScope like lifecycleScope
                while (true) {
                    delay(1000L)
                    Log.d(TAG, "still working...")
                }
            }
            GlobalScope.launch {
                delay(5000L)
                Intent(this@CoroutinesActivity, MainActivity::class.java).also {
                    startActivity(intent)
                    finish()
                }
            }
        }

        //cancellation and exception
        //with error
        lifecycleScope.launch {
            try {
                launch {
                    throw Exception()
                }
            } catch (e: Exception) {
                println("caught exception: $e")
            }


            try {
                launch {
                    launch {
                        throw Exception()
                    }
                }
            } catch (e: Exception) {
                println("caught exception: $e")
            }

        }

        //without error
        lifecycleScope.launch {
            launch {
                launch {
                    try {
                        throw Exception()
                    } catch (e: Exception) {
                        println("caught exception: $e")
                    }

                }
            }
        }


        //with error
        lifecycleScope.launch {
            val string = async {
                delay(500L)
                throw Exception()
                "Result"
            }
        }

        //without error
        val deferred = lifecycleScope.async {
            val string = async {
                delay(500L)
                throw Exception()
                "Result"
            }
        }

        //error
        lifecycleScope.launch {
            deferred.await()
        }

        //without error
        lifecycleScope.launch {
            try {
                deferred.await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //without error
        val handler = CoroutineExceptionHandler { _, throwable ->
            println("Caught exception : $throwable")
        }
        lifecycleScope.launch(handler) {
            launch {
                throw Exception()
            }
        }

        //with error
        CoroutineScope(Dispatchers.Main).launch {
            launch {
                delay(300L)
                throw Exception("Coroutine 1 failed")
            }
            launch {
                delay(400L)
                println("Coroutine 2 finished")
            }
        }

        //without error failed both
        val handler1 = CoroutineExceptionHandler { _, throwable ->
            println("Caught exception: $throwable")
        }
        CoroutineScope(Dispatchers.Main + handler1).launch {
            launch {
                delay(300L)
                throw Exception("Coroutine 1 failed")
            }
            launch {
                delay(400L)
                println("Coroutine 2 finished")
            }
        }

        //without error failed Coroutines 1
        val handler2 = CoroutineExceptionHandler { _, throwable ->
            println("Caught exception: $throwable")
        }
        CoroutineScope(Dispatchers.Main + handler1).launch {
            supervisorScope {
                launch {
                    delay(300L)
                    throw Exception("Coroutine 1 failed")
                }
                launch {
                    delay(400L)
                    println("Coroutine 2 finished")
                }
            }
        }

        //
        lifecycleScope.launch {
            val jobL = launch {
                try {
                    delay(500L)
                } catch (e: Exception) {
                    if (e is CancellationException) {
                        throw e
                    } else
                        e.printStackTrace()
                }
                println("coroutine 1 is finished")
            }
            delay(300L)
            jobL.cancel()
        }

        lifecycleScope.launch {
            val jobL = launch {
                try {
                    delay(500L)
                } catch (e: HttpRetryException) {
                    e.printStackTrace()
                }
                println("coroutine 1 is finished")
            }
            delay(300L)
            jobL.cancel()
        }
    }

    private fun fib(n: Int): Long {
        return if (n == 0) 0
        else if (n == 1) 1
        else fib(n - 1) + fib(n - 2)

    }

    suspend fun doNetworkCall1(): String {
        delay(3000)
        return "do network call 1"
    }

    suspend fun doNetworkCall2(): String {
        delay(3000)
        return "do network call 2"
    }
}
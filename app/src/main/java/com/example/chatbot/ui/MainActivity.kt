package com.example.chatbot.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatbot.databinding.ActivityMainBinding
import com.example.chatbot.data.Message
import com.example.chatbot.utils.Constants.RECEIVE_ID
import com.example.chatbot.utils.Constants.SEND_ID
import com.example.chatbot.utils.BotResponse
import com.example.chatbot.utils.Constants.OPEN_GOOGLE
import com.example.chatbot.utils.Constants.OPEN_SEARCH
import com.example.chatbot.utils.Time
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MessagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView()

        clickEvents()

        customBotMessage("Hello! I am a Bot, how may I help?")
    }

    private fun clickEvents() {
        binding.btnSend.setOnClickListener {
            sendMessage()
        }

        binding.etMessage.setOnClickListener {
            lifecycleScope.launch {
                delay(100)
                binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun recyclerView() {
        adapter = MessagingAdapter()
        binding.rvMessages.adapter = adapter
        binding.rvMessages.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            delay(100)
            binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun sendMessage() {
        val message = binding.etMessage.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            binding.etMessage.setText("")

            adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            binding.rvMessages.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        lifecycleScope.launch {
            delay(1000)
            val response = BotResponse.basicResponses(message)

            adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))
            binding.rvMessages.scrollToPosition(adapter.itemCount - 1)

            when (response) {
                OPEN_GOOGLE -> {
                    try {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "Error Occurred!! Cannot Open Google", Toast.LENGTH_SHORT).show()

                    }
                }
                OPEN_SEARCH -> {
                    try {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = message.substringAfterLast("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "Error Occurred!! Cannot Open Google", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun customBotMessage(message: String) {
        lifecycleScope.launch {
            delay(1000)
            val timeStamp = Time.timeStamp()
            adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))
            binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
        }
    }
}

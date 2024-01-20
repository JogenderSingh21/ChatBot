package com.example.chatbot.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbot.R
import com.example.chatbot.data.Message
import com.example.chatbot.databinding.MessageItemBinding
import com.example.chatbot.utils.Constants.RECEIVE_ID
import com.example.chatbot.utils.Constants.SEND_ID

class MessagingAdapter : RecyclerView.Adapter<MessagingAdapter.MessageViewHolder>() {

    var messagesList = mutableListOf<Message>()

    inner class MessageViewHolder(val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {

                // Remove message on the item clicked
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    messagesList.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = MessageItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messagesList[position]
        val context = holder.itemView.context

        val binding = holder.binding
        when (currentMessage.id) {
            SEND_ID -> {
                binding.tvMessage.text = currentMessage.message
                binding.tvMessage.visibility = View.VISIBLE
                binding.tvBotMessage.visibility = View.GONE
            }
            RECEIVE_ID -> {
                binding.tvBotMessage.text = currentMessage.message
                binding.tvBotMessage.visibility = View.VISIBLE
                binding.tvMessage.visibility = View.GONE
            }
        }
    }

    fun insertMessage(message: Message) {
        this.messagesList.add(message)
        notifyItemInserted(messagesList.size)
    }
}
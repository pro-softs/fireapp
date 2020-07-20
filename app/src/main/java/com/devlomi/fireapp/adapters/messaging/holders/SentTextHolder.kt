package com.devlomi.fireapp.adapters.messaging.holders

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.devlomi.fireapp.R
import com.devlomi.fireapp.adapters.messaging.holders.base.BaseSentHolder
import com.devlomi.fireapp.model.constants.DownloadUploadStat
import com.devlomi.fireapp.model.realms.Message
import com.devlomi.fireapp.model.realms.User
import com.devlomi.fireapp.utils.FileUtils

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView

// sent message with type text
class SentTextHolder(context: Context, itemView: View) : BaseSentHolder(context,itemView) {
    var tvMessageContent: EmojiconTextView = itemView.findViewById(R.id.tv_message_content)
    override fun bind(message: Message, user: User) {
        super.bind(message,user)
        tvMessageContent.text = message.content
    }

}


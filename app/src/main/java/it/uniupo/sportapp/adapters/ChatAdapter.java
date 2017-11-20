package it.uniupo.sportapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.models.ChatMessage;

/**
 * Created by dgavio on 16/11/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private static final int MESSAGE_SENT = 1;
    private static final int MESSAGE_RECEIVED = 2;



    class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageTv, authorTv, timeTv;
        public ImageView profileIv;

        ViewHolder(View itemView) {
            super(itemView);
            messageTv = itemView.findViewById(R.id.txt_message);
            authorTv = itemView.findViewById(R.id.txt_user);
            timeTv = itemView.findViewById(R.id.time_tv);
            profileIv = itemView.findViewById(R.id.profile_msg_img);
        }
    }

    // Store a member variable for the contacts
    private List<ChatMessage> mMessages;
    // Store the context for easy access
    private Context mContext;

    public ChatAdapter(List<ChatMessage> mMessages, Context mContext) {
        this.mMessages = mMessages;
        this.mContext = mContext;
    }

    private Context getContext() {
        return mContext;
    }

    @Override public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_message_sent, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_message_received, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessage chat = mMessages.get(position);
        holder.messageTv.setText(chat.getMessageText());
        holder.authorTv.setText(chat.getMessageUserName());
        ImageView profileImageView = holder.profileIv;
        if(getItemViewType(position) == MESSAGE_SENT && Singleton.getCurrentPlayer().getPlayerImageUid()!=null)
            Picasso.with(getContext()).load(chat.getMessageUserImage()).into(profileImageView);
    }

    @Override public int getItemCount() {
        return mMessages.size();
    }

    @Override public int getItemViewType(int position) {
        if (mMessages.get(position).getMessageUserKey().equals(Singleton.getCurrentPlayer().getPlayerKey())) return MESSAGE_SENT;

        return MESSAGE_RECEIVED;
    }


}

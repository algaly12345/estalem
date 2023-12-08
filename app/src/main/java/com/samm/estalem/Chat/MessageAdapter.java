package com.samm.estalem.Chat;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.samm.estalem.R;

import java.util.ArrayList;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    List<Message> messages = new ArrayList<Message>();
    Activity context;
    Message message;
    View convertView;
    int Layout;

    public MessageAdapter(Activity context) {
        this.context = context;
    }


    public int add(Message message) {
        Log.v("add", "************************************************************************");

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });

//        notifyDataSetChanged();
        messages.add(message);
        if (message.belongsToCurrentUser == "1") {
            Layout = 0;
            Layout = R.layout.my_message;

        } else {
            Layout = 0;
            Layout = R.layout.their_message;

        }


        return getItemCount();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v("onCreateViewHolder", "************************************************************************");

        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        convertView = messageInflater.inflate(Layout, null);


        return new MessageAdapter.MessageViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        message = messages.get(position);
        holder.messageBody.setText(message.text);
        Log.v("onBindViewHolder", "************************************************************************");
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View avatar;
        public TextView name;
        public TextView messageBody;


        public MessageViewHolder(View itemView) {
            super(itemView);
          //  avatar = (View) itemView.findViewById(R.id.avatar);
        //    name = (TextView) itemView.findViewById(R.id.name);
            messageBody = (TextView) itemView.findViewById(R.id.message_body);

            Log.v("MessageViewHolder", "************************************************************************");
        }

        @Override
        public void onClick(View view) {

        }
    }
}


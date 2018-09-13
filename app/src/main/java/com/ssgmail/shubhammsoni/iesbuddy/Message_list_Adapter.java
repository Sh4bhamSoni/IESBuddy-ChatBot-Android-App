package com.ssgmail.shubhammsoni.iesbuddy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

/**
 * Created by shubham soni on 29-Mar-18.
 */

public class Message_list_Adapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    List<Message_Model> messageList;
    Context context;



    public Message_list_Adapter(List<Message_Model> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;

    }


    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.send_message_layout, parent, false);
            return new Sent_Message_holder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.received_message_layout, parent, false);
            return new Recieved_Message_holder(view);
        }

        return null;
    }

    //  count items in list or number of items in Recyclar view
    @Override
    public int getItemCount() {
        return messageList.size();
    }


    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message_Model message = messageList.get(position);

        if (message.isSender()) {

            return VIEW_TYPE_MESSAGE_SENT;

        } else {

            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }


    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Message_Model message = messageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT: {
                ((Sent_Message_holder) holder).bind(message);

                break;
            }

            case VIEW_TYPE_MESSAGE_RECEIVED: {




                ((Recieved_Message_holder) holder).bind(message);

                ((Recieved_Message_holder) holder).testimv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, ImageViwerActivity.class);
                        i.putExtra("imageurl", messageList.get(position).getImageUrl());
                        context.startActivity(i);

                    }
                });
                break;
            }

            default:
                Log.d("Switch", "default");

        }
    }

    public class Recieved_Message_holder extends RecyclerView.ViewHolder {


        TextView txtmessage;
        ImageView IPSLogo;
        ImageView testimv;


        public Recieved_Message_holder(View itemView) {


            super(itemView);

            txtmessage = itemView.findViewById(R.id.text_received_message);
            IPSLogo = itemView.findViewById(R.id.image_message_profile);
            testimv = itemView.findViewById(R.id.testImv);


        }

        void bind(Message_Model message) {

            txtmessage.setText(message.getMessage());

            if (message.getImageUrl() != null) {
                testimv.setVisibility(View.VISIBLE);
                Glide.with(context).load(message.getImageUrl()).into(testimv);
            } else {
                testimv.setVisibility(View.GONE);
            }


        }
    }


    public class Sent_Message_holder extends RecyclerView.ViewHolder {

        TextView txtmessage;


        public Sent_Message_holder(View itemView) {
            super(itemView);
            txtmessage = itemView.findViewById(R.id.text_send_message);

        }

        void bind(Message_Model message) {

            txtmessage.setText(message.getMessage());
        }
    }


}

package com.samm.estalem.Chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.samm.estalem.R;
import com.samm.estalem.Util.FormatUtils;
import com.samm.estalem.Util.PicassoClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MessagesAdapter extends BaseAdapter {

    static List<Message> messages = new ArrayList<Message>();
    Activity context;
    boolean zoomOut;

    public MessagesAdapter(Activity context) {
        this.context = context;
    }


    public int add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
        return messages.size();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);

        if (message.belongsToCurrentUser == "1") {
            convertView = messageInflater.inflate(R.layout.my_message, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            holder.messageBody.setText(message.getText());

            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_time.setText("" + message.dateTime);


            notifyDataSetChanged();
        }
        else if ((message.belongsToCurrentUser == "0")) {
            convertView = messageInflater.inflate(R.layout.their_message, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);

            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_time.setText(message.dateTime);

            holder.messageBody.setText(message.getText());
            notifyDataSetChanged();
        }
        else if ((message.belongsToCurrentUser == "11")) {
            convertView = messageInflater.inflate(R.layout.my_message_image, null);
            holder.Image = (ImageView) convertView.findViewById(R.id.Image);
            Bitmap bitmap = null;
            try {
                if (message.uri != null) {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), message.uri);
                    holder.Image.setImageBitmap(bitmap);
                } else {
                    PicassoClient.downloadImage(context, ChatConnection.BASE_URL2 + message.text, holder.Image);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            holder.Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FullScreenImage.class);
                    Bitmap bitmap = ((BitmapDrawable) holder.Image.getDrawable()).getBitmap();

                    if (message.uri != null) {
                        intent.putExtra("image", message.uri.toString());
                    } else {
                        intent.putExtra("image", getImageUri(context, bitmap).toString());
                    }
                    context.startActivity(intent);

                }
            });

            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_time.setText(""+message.dateTime);

            notifyDataSetChanged();
        }else {

            convertView = messageInflater.inflate(R.layout.thire_message_image, null);
            holder.Image = (ImageView) convertView.findViewById(R.id.Image);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_time.setText(message.dateTime);
            PicassoClient.downloadImage(context, ChatConnection.BASE_URL2 + message.text, holder.Image);
            Bitmap bitmap = ((BitmapDrawable) holder.Image.getDrawable()).getBitmap();
            holder.Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FullScreenImage.class);
                    intent.putExtra("image", getImageUri(context, bitmap).toString());
                    context.startActivity(intent);

                }
            });
            notifyDataSetChanged();
        }

        return convertView;
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    class MessageViewHolder {
        public View avatar;
        public TextView name;
        public TextView tv_time;
        public TextView messageBody;
        public ImageView Image;
    }


}

package com.samm.estalem.Chat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.R;
import com.samm.estalem.Retrofit.CREATE_GET_POST;
import com.samm.estalem.Util.NetworkStateChangeReceiver;
import com.samm.estalem.Util.PathUtil;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Upload;
import com.samm.estalem.Util.UtilityPermission;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    Uri uri;
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";
    private String userChoosenTask;

    public static Activity activity;
    private static EditText editText;
    Dialog dialog;
    private static MessagesAdapter messageAdapter;
    //  private static RecyclerView messagesView;
    static ListView messagesView;
    RecyclerView.LayoutManager layoutManager;
    String phone, myPhone;
    String TAG="XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        dialog = ShowDialog.progres(this);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.CAMERA},
                2);


        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextAppearance(getApplication(), R.style.actionBarTitleTextStyle);

        phone = getIntent().getExtras().getString("phone");

        editText = (EditText) findViewById(R.id.editText);
        TextView tv_time = (TextView) findViewById(R.id.tv_time);
        ImageView sendMessage = (ImageView) findViewById(R.id.SendMessage);
        ImageView SendImage = (ImageView) findViewById(R.id.SendImage);


        messageAdapter = new MessagesAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

//        messageAdapter=new MessageAdapter(this);
//        messagesView = (RecyclerView) findViewById(R.id.messages_view);
//        messagesView.setAdapter(messageAdapter);
//        layoutManager = new LinearLayoutManager(this);
//        messagesView.setLayoutManager(layoutManager);

        myPhone = SharedpreferencesData.getValuePreferences(ChatActivity.this, "clientPhone", "") + SharedpreferencesData.getValuePreferences(ChatActivity.this, "providerPhone", "");

        SendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().isEmpty()) {
                    Message message = new Message(editText.getText().toString(), "1", null, Calendar.getInstance().getTime()+"");

                    messagesView.post(new Runnable() {
                        @Override
                        public void run() {
                            messagesView.smoothScrollToPosition(messageAdapter.add(message));
                            messageAdapter.notifyDataSetChanged();
                            sendMessage(editText.getText().toString() + "          ", SharedpreferencesData.getValuePreferences(ChatActivity.this, "clientPhone", "") + SharedpreferencesData.getValuePreferences(ChatActivity.this, "providerPhone", ""), phone, "text");
                            editText.setText("");
                        }
                    });
                }
            }
        });
        isNetworkAvailable();
        getMyChat(SharedpreferencesData.getValuePreferences(ChatActivity.this, "clientPhone", "") + SharedpreferencesData.getValuePreferences(ChatActivity.this, "providerPhone", ""), phone);

    }


    private void getMyChat(String myPhone, String otherPhone) {
        dialog.show();
        ChatConnection retrofit_connection = new ChatConnection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.GetMyChat(myPhone, otherPhone).enqueue(new Callback<List<ChatModel>>() {
            @Override
            public void onResponse(Call<List<ChatModel>> call, Response<List<ChatModel>> response) {
                MessagesAdapter.messages.clear();
                for (int i = 0; i < response.body().size(); i++) {
                    ChatModel chatModel = response.body().get(i);
                    if (chatModel.messageType.equals("text") && chatModel.userPhone.equals(myPhone) && chatModel.otherUserPhone.equals(phone)) {
                        messagesView.post(new Runnable() {
                            @Override
                            public void run() {
                                messagesView.smoothScrollToPosition(messageAdapter.add(new Message(chatModel.message + "          ", "1", null, chatModel.dateTimeNow)));
                                messageAdapter.notifyDataSetChanged();
                            }
                        });
                    } else if (chatModel.messageType.equals("image") && chatModel.userPhone.equals(myPhone) && chatModel.otherUserPhone.equals(phone)) {
                        messagesView.post(new Runnable() {
                            @Override
                            public void run() {
                                messagesView.smoothScrollToPosition(messageAdapter.add(new Message(chatModel.message + "          ", "11", null, chatModel.dateTimeNow)));
                                messageAdapter.notifyDataSetChanged();
                            }
                        });
                    } else if (chatModel.messageType.equals("text") && chatModel.userPhone.equals(phone) && chatModel.otherUserPhone.equals(myPhone)) {
                        messagesView.post(new Runnable() {
                            @Override
                            public void run() {
                                messagesView.smoothScrollToPosition(messageAdapter.add(new Message(chatModel.message + "          ", "0", null, chatModel.dateTimeNow)));
                                messageAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        messagesView.post(new Runnable() {
                            @Override
                            public void run() {
                                messagesView.smoothScrollToPosition(messageAdapter.add(new Message(chatModel.message + "          ", "00", null, chatModel.dateTimeNow)));
                                messageAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                }
                stopService(new Intent(ChatActivity.this,  ChatService.class));
                startService(new Intent(ChatActivity.this, ChatService.class));
                dialog.dismiss();
            }


            @Override
            public void onFailure(Call<List<ChatModel>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ChatActivity.this, "" + getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case UtilityPermission.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {/*"Take Photo",*/ "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = UtilityPermission.checkPermission(ChatActivity.this);

             /*   if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else */if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");




        UploadImage(getImageUri(ChatActivity.this,thumbnail));


    }

    private Uri getImageUri(Context context, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void UploadImage(Uri uri) {
        String realPath = null;
        File file = null;
        try {
            realPath = PathUtil.getPath(getApplicationContext(), uri);
            file = new File(realPath);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        ChatConnection retrofit_connection = new ChatConnection();
        retrofit_connection.con_GSON();
        final CREATE_GET_POST create_post_get = retrofit_connection.retrofit.create(CREATE_GET_POST.class);
        MultipartBody.Part image = Upload.upLoadProfileImage(file);
        create_post_get.UploadImage(image).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    Message message = new Message(editText.getText().toString(), "11", uri, Calendar.getInstance().getTime()+"");
                    messagesView.post(new Runnable() {
                        @Override
                        public void run() {
                            messagesView.smoothScrollToPosition(messageAdapter.add(message));
                            messageAdapter.notifyDataSetChanged();
                            sendMessage(response.body().getMessage().toString(), SharedpreferencesData.getValuePreferences(ChatActivity.this, "clientPhone", "") + SharedpreferencesData.getValuePreferences(ChatActivity.this, "providerPhone", ""), phone, "image");
                            editText.setText("");
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ShowDialog.showErrorDialog(getResources().getString(R.string.sorry), getResources().getString(R.string.no_internet), ChatActivity.this);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
Bitmap bitmap;
        if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();


            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                bitmap = Bitmap.createScaledBitmap(bitmap, 3000, 3000, false);
                uri = getImageUri(this, bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }


            UploadImage(uri);
        } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
        }
    }

    public static void receiveMessage(String message, String dateTime) {

        messagesView.post(new Runnable() {
            @Override
            public void run() {
                messagesView.smoothScrollToPosition(messageAdapter.add(new Message(message + "          ", "0", null, dateTime)));
                messageAdapter.notifyDataSetChanged();
            }
        });

    }

    public static void receiveImage(String message, String dateTime) {
        messagesView.post(new Runnable() {
            @Override
            public void run() {
                messagesView.smoothScrollToPosition(messageAdapter.add(new Message(message + "          ", "00", null, dateTime)));
                messageAdapter.notifyDataSetChanged();
            }
        });

    }

    public void sendMessage(String message, String userPhone, String otherPhone, String type) {
        ChatConnection ChatConnection = new ChatConnection();
        ChatConnection.con_GSON();
        final CREATE_GET_POST create_post_get = ChatConnection.retrofit.create(CREATE_GET_POST.class);
        create_post_get.SendMessage(new ChatModel(message, userPhone, otherPhone, "8/8/8", type)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Toast.makeText(ChatActivity.this, ""+response.body(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void isNetworkAvailable() {
        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                // String networkStatus = isNetworkAvailable ? "connected" : "disconnected";
                if (isNetworkAvailable) {
                    getMyChat(SharedpreferencesData.getValuePreferences(ChatActivity.this, "clientPhone", "") + SharedpreferencesData.getValuePreferences(ChatActivity.this, "providerPhone", ""), phone);
                }
            }
        }, intentFilter);
    }
}





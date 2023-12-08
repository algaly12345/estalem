package com.samm.estalem.Util;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Upload {
    @SuppressLint("LongLogTag")
    public static MultipartBody.Part upLoadProfileImage(File file) {


        // creates RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual filename
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);


      return body;
    }


    public static MultipartBody.Part NationalCardImage(File file) {


        // creates RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual filename
        MultipartBody.Part body = MultipartBody.Part.createFormData("NationalCardImage", file.getName(), requestFile);


        return body;
    }


    public static MultipartBody.Part DrivinglicenseImage(File file) {


        // creates RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual filename
        MultipartBody.Part body = MultipartBody.Part.createFormData("DrivinglicenseImage", file.getName(), requestFile);


        return body;
    }


    public static MultipartBody.Part FormImage(File file) {


        // creates RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual filename
        MultipartBody.Part body = MultipartBody.Part.createFormData("FormImage", file.getName(), requestFile);


        return body;
    }


    public static MultipartBody.Part DrivinglicenseAuthoImage(File file) {


        // creates RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual filename
        MultipartBody.Part body = MultipartBody.Part.createFormData("DrivinglicenseAuthoImage", file.getName(), requestFile);


        return body;
    }

    public static MultipartBody.Part BackCarImage(File file) {


        // creates RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual filename
        MultipartBody.Part body = MultipartBody.Part.createFormData("BackCarImage", file.getName(), requestFile);


        return body;
    }


    public static MultipartBody.Part FrontCarImage(File file) {


        // creates RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual filename
        MultipartBody.Part body = MultipartBody.Part.createFormData("FrontCarImage", file.getName(), requestFile);


        return body;
    }

}

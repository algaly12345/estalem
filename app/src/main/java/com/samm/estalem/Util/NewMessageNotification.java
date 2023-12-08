package com.samm.estalem.Util;//package com.baskoda.electricityapp.Classes.Util;
//
//import android.annotation.TargetApi;
//import android.app.notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Build;
//import android.support.v4.app.NotificationCompat;
//
//import com.baskoda.cashmonyalgent.R;
//
///**
// * Helper class for showing and canceling new message
// * notifications.
// * <p>
// * This class makes heavy use of the {@link NotificationCompat.Builder} helper
// * class to create notifications in a backward-compatible way.
// */
//public class NewMessageNotification {
//    /**
//     * The unique identifier for this type of notification.
//     */
//    private static final String NOTIFICATION_TAG = "NewMessage";
//
//    /**
//     * Shows the notification, or updates a previously shown notification of
//     * this type, with the given parameters.
//     * <p>
//     * TODO: Customize this method's arguments to present relevant content in
//     * the notification.
//     * <p>
//     * TODO: Customize the contents of this method to tweak the behavior and
//     * presentation of new message notifications. Make
//     * sure to follow the
//     * <a href="https://developer.android.com/design/patterns/notifications.html">
//     * notification design guidelines</a> when doing so.
//     *
//     * @see #cancel(Context)
//     */
//    public static void notify(final Context context,
//                              final String exampleString, final int number) {
//        final Resources res = context.getResources();
//
//        // This image is used as the notification's large icon (thumbnail).
//        // TODO: Remove this if your notification has no relevant thumbnail.
//        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.example_picture);
//
//
//        final String ticker = exampleString;
//        final String title = res.getString(
//                R.string.new_message_notification_title_template, exampleString);
//        final String text = res.getString(
//                R.string.new_message_notification_placeholder_text_template, exampleString);
//
//        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//
//                // Set appropriate defaults for the notification light, sound,
//                // and vibration.
//                .setDefaults(notification.DEFAULT_ALL)
//
//                // Set required fields, including the small icon, the
//                // notification title, and text.
//                .setSmallIcon(R.drawable.ic_stat_new_message)
//                .setContentTitle(title)
//                .setContentText(text)
//
//                // All fields below this line are optional.
//
//                // Use a default priority (recognized on devices running Android
//                // 4.1 or later)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//                // Provide a large icon, shown with the notification in the
//                // notification drawer on devices running Android 3.0 or later.
//                .setLargeIcon(picture)
//
//                // Set ticker text (preview) information for this notification.
//                .setTicker(ticker)
//
//                // Show a number. This is useful when stacking notifications of
//                // a single type.
//                .setNumber(number)
//
//                // If this notification relates to a past or upcoming event, you
//                // should set the relevant time information using the setWhen
//                // method below. If this call is omitted, the notification's
//                // timestamp will by set to the time at which it was shown.
//                // TODO: Call setWhen if this notification relates to a past or
//                // upcoming event. The sole argument to this method should be
//                // the notification timestamp in milliseconds.
//                //.setWhen(...)
//
//                // Set the pending intent to be initiated when the user touches
//                // the notification.
//                .setContentIntent(
//                        PendingIntent.getActivity(
//                                context,
//                                0,
//                                new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")),
//                                PendingIntent.FLAG_UPDATE_CURRENT))
//
//                // Show expanded text content on devices running Android 4.1 or
//                // later.
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(text)
//                        .setBigContentTitle(title)
//                        .setSummaryText("Dummy summary text"))
//
//                // Example additional actions for this notification. These will
//                // only show on devices running Android 4.1 or later, so you
//                // should ensure that the activity in this notification's
//                // content intent provides access to the same actions in
//                // another way.
//                .addAction(
//                        R.drawable.ic_action_stat_share,
//                        res.getString(R.string.action_share),
//                        PendingIntent.getActivity(
//                                context,
//                                0,
//                                Intent.createChooser(new Intent(Intent.ACTION_SEND)
//                                        .setType("text/plain")
//                                        .putExtra(Intent.EXTRA_TEXT, "Dummy text"), "Dummy title"),
//                                PendingIntent.FLAG_UPDATE_CURRENT))
//                .addAction(
//                        R.drawable.ic_action_stat_reply,
//                        res.getString(R.string.action_reply),
//                        null)
//
//                // Automatically dismiss the notification when it is touched.
//                .setAutoCancel(true);
//
//        notify(context, builder.build());
//    }
//
//    @TargetApi(Build.VERSION_CODES.ECLAIR)
//    private static void notify(final Context context, final notification notification) {
//        final NotificationManager nm = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
//            nm.notify(NOTIFICATION_TAG, 0, notification);
//        } else {
//            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
//        }
//    }
//
//    /**
//     * Cancels any notifications of this type previously shown using
//     * {@link #notify(Context, String, int)}.
//     */
//    @TargetApi(Build.VERSION_CODES.ECLAIR)
//    public static void cancel(final Context context) {
//        final NotificationManager nm = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
//            nm.cancel(NOTIFICATION_TAG, 0);
//        } else {
//            nm.cancel(NOTIFICATION_TAG.hashCode());
//        }
//    }
//}
/* Print Invoices
private void printMatrixText() {
        new Thread(new Runnable() {
@Override
public void run() {
        AssetManager asm = getActivity().getAssets();
        InputStream inputStream = null;
        try {
        inputStream = asm.open("china_unin.bmp");
        } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }
        Drawable d = Drawable.createFromStream(inputStream, null);
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();

        int printStatus = mPrinter.getPrinterStatus();
        if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
        getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
        DialogUtils.show(getActivity(), getString(R.string.printer_out_of_paper));

        }
        });
        } else {
        mPrinter.setPrintAppendBitmap(bitmap, Layout.Alignment.ALIGN_CENTER);
        PrnStrFormat format = new PrnStrFormat();
        format.setTextSize(30);
        format.setAli(Layout.Alignment.ALIGN_CENTER);
        format.setStyle(PrnTextStyle.BOLD);
        if (fontsStyle == 0) {
        format.setFont(PrnTextFont.CUSTOM);
        format.setPath(Environment.getExternalStorageDirectory() + "/fonts/simsun.ttf");
        } else if (fontsStyle == 1) {
        format.setFont(PrnTextFont.DEFAULT);
        //  format.setPath(Environment.getExternalStorageDirectory()+"/fonts/heiti.ttf");
        } else {
        format.setFont(PrnTextFont.CUSTOM);
        format.setPath(Environment.getExternalStorageDirectory() + "/fonts/fangzhengyouyuan.ttf");
        }
        mPrinter.setPrintAppendString(getResources().getString(R.string.pos_sales_slip), format);
        format.setTextSize(25);
        format.setStyle(PrnTextStyle.NORMAL);
        format.setAli(Layout.Alignment.ALIGN_NORMAL);
        String nomal = "";
        mPrinter.setPrintAppendString(" ", format);
        mPrinter.setPrintAppendString(getResources().getString(R.string.merchant_name) + " Test ", format);
        mPrinter.setPrintAppendString(getResources().getString(R.string.merchant_no) + " 123456789012345 ", format);
        mPrinter.setPrintAppendString(getResources().getString(R.string.terminal_name) + " 12345678 ", format);
        mPrinter.setPrintAppendString(getResources().getString(R.string.operator_no) + " 01 ", format);
        mPrinter.setPrintAppendString(getResources().getString(R.string.card_no) + " ", format);
        format.setAli(Layout.Alignment.ALIGN_CENTER);
        format.setTextSize(30);
        format.setStyle(PrnTextStyle.BOLD);
        mPrinter.setPrintAppendString("6214 44** **** **** 7816", format);
        format.setAli(Layout.Alignment.ALIGN_NORMAL);
        format.setStyle(PrnTextStyle.NORMAL);
        format.setTextSize(25);
        mPrinter.setPrintAppendString(getResources().getString(R.string.acq_institute), format);
        mPrinter.setPrintAppendString(getResources().getString(R.string.iss) + " ", format);
        mPrinter.setPrintAppendString(getResources().getString(R.string.trans_type) + " ", format);
        format.setTextSize(30);
        format.setStyle(PrnTextStyle.BOLD);
        mPrinter.setPrintAppendString(getResources().getString(R.string.sale) + " (C) ", format);
        format.setAli(Layout.Alignment.ALIGN_NORMAL);
        format.setStyle(PrnTextStyle.NORMAL);
        format.setTextSize(25);
        mPrinter.setPrintAppendString(getResources().getString(R.string.exe_date) + " 2030/10  ", format);
        mPrinter.setPrintAppendString(getResources().getString(R.string.batch_no) + " 000335 ", format);
        mPrinter.setPrintAppendString(getResources().getString(R.string.voucher_no) + " 000002 ", format);
        mPrinter.setPrintAppendString(getResources().getString(R.string.date) + " 2018/05/28 ", format);
        mPrinter.setPrintAppendString(getResources().getString(R.string.time) + " 00:00:01 ", format);
        format.setTextSize(30);
        format.setStyle(PrnTextStyle.BOLD);
        mPrinter.setPrintAppendString(getResources().getString(R.string.amount) + "￥0.01", format);
        format.setStyle(PrnTextStyle.NORMAL);
        format.setTextSize(25);
        mPrinter.setPrintAppendString(getResources().getString(R.string.reference) + " ", format);
        mPrinter.setPrintAppendString(getResources().getString(R.string.cardholder_signature) + " ", format);
        mPrinter.setPrintAppendString(" ", format);

        mPrinter.setPrintAppendString(" -----------------------------", format);
        mPrinter.setPrintAppendString(getResources().getString(R.string.print_remark) + " ", format);
        mPrinter.setPrintAppendString(getResources().getString(R.string.cardholder_copy) + " ", format);
        mPrinter.setPrintAppendString(" ", format);
        mPrinter.setPrintAppendString(" ", format);
        mPrinter.setPrintAppendString(" ", format);
        mPrinter.setPrintAppendString(" ", format);
        printStatus = mPrinter.setPrintStart();
        if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
        getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
        DialogUtils.show(getActivity(), getString(R.string.printer_out_of_paper));

        }
        });
        }
        }


        }
        }).start();
        } */
package com.samm.estalem.Util;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

public class ShareActivity extends AppCompatActivity {

    public static void share(Activity activity, int productId) {
        if (SharedpreferencesData.getValuePreferences(activity, "clientId", "").equals("")) {
            Toast.makeText(activity, "عليك تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
        } else {
            Branch branch = Branch.getInstance();
            branch.setIdentity("" + SharedpreferencesData.getValuePreferences(activity, "clientId", ""));
            branch.initSession(new Branch.BranchReferralInitListener() {
                @Override
                public void onInitFinished(JSONObject referringParams, BranchError error) {
                    if (error == null) {
                        // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                        // params will be empty if no data found
                        // ... insert custom logic here ...
                    } else {
                        Log.i("MyApp", error.getMessage());
                    }
                }
            }, activity.getIntent().getData(), activity);
            // Lifecycle callback method

            BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                    // The identifier is what Branch will use to de-dupe the content across many different Universal Objects
                    .setCanonicalIdentifier("item/58264")
                    // This is where you define the open graph structure and how the object will appear on Facebook or in a deepview
                    .setTitle("زاجل السعادة")
                    .setContentDescription("الذهاب الي تطبيق زاجل السعادة")
                    .setContentImageUrl("http://zajelal-sadaa.com/wwwroot/Images/main_logo.png")
                    // You use this to specify whether this content can be discovered publicly - default is public
                    .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                    // Here is where you can add custom keys/values to the deep link data
                    .addContentMetadata("productId", productId + "");

            LinkProperties linkProperties = new LinkProperties()
                    .setChannel("facebook")
                    .setFeature("sharing")
                    .addControlParameter("$desktop_url", "http://zajelal-sadaa.com/")
                    //.addControlParameter("$android_url", "http://zajelal-sadaa.com/")
                    .addControlParameter("$ios_url", "http://zajelal-sadaa.com/");

            branchUniversalObject.generateShortUrl(activity, linkProperties, new Branch.BranchLinkCreateListener() {
                @Override
                public void onLinkCreate(String url, BranchError error) {
                    if (error == null) {
                        Log.i("MyApp", "*******************************************got my Branch link to share: " + url);
                    } else {
                        Log.e("error", error.toString());
                    }
                }
            });

            ShareSheetStyle shareSheetStyle = new ShareSheetStyle(activity, "زاجل السعادة!", "منتج من زاجل السعادة: ")
                    .setMoreOptionStyle(activity.getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more")
                    .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                    .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL);

            branchUniversalObject.showShareSheet(activity,
                    linkProperties,
                    shareSheetStyle,
                    new Branch.BranchLinkShareListener() {
                        @Override
                        public void onShareLinkDialogLaunched() {
                        }

                        @Override
                        public void onShareLinkDialogDismissed() {
                        }

                        @Override
                        public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                            Log.e("LinkShared", "success");
                        }

                        @Override
                        public void onChannelSelected(String channelName) {
                        }
                    });
        }
    }

    public static void balance(Activity activity) {

        Branch branch = Branch.getInstance(activity);
        branch.loadRewards(new Branch.BranchReferralStateChangedListener() {
            @Override
            public void onStateChanged(boolean changed, BranchError error) {
                int credits = branch.getCredits();
                Toast.makeText(activity, "" + credits, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getRedeem() {
        Branch branch = Branch.getInstance(getApplicationContext());
        branch.redeemRewards(8);
    }
}
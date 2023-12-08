package com.samm.estalem.Faraments.ui.active_provider;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.samm.estalem.Activities.Client.ClientMainActivity;
import com.samm.estalem.Activities.Provider.ProviderMainActivity;
import com.samm.estalem.Faraments.ui.client_payment_fragment.ClientPaymentFragment;
import com.samm.estalem.Faraments.ui.client_payment_fragment.ClientPaymentViewModel;
import com.samm.estalem.Faraments.ui.main_order_client_order.MainClientOrderFragment;
import com.samm.estalem.Faraments.ui.main_provider_fragment.MainProviderFragment;
import com.samm.estalem.R;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Util;

public class ActiveProviderFragment extends Fragment {
    Dialog dialog;
    private ClientPaymentViewModel mViewModel;

    public static ClientPaymentFragment newInstance() {
        return new ClientPaymentFragment();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Util.language(getActivity());

        View view= inflater.inflate(R.layout.active_fragment, container, false);
        dialog = ShowDialog.progres(getActivity());
        dialog.show();
        ImageView button=(ImageView) view.findViewById(R.id.img_nav_menu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProviderMainActivity)getActivity()).showNavBar();
            }
        });

        ImageView imgBack=(ImageView) view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.language(getActivity());
                MainProviderFragment offerPriceFragment=new MainProviderFragment(getActivity());
                FragmentTransaction ft =((FragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, offerPriceFragment);
                ft.commit();
            }
        });


        WebView webView = (WebView)view.findViewById(R.id.wvPay);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://astalem.com/MoayserPayment/index2?providerPhone=" + SharedpreferencesData.getValuePreferences(getActivity(),"providerPhone","")
                +"&amount="+SharedpreferencesData.getValuePreferences(getActivity(),"amountToActiveProvider",""));


        dismissDialog();
        return view;
    }

    private void dismissDialog() {

        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                dialog.dismiss();
            }
        }.start();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ClientPaymentViewModel.class);
        // TODO: Use the ViewModel
    }

}

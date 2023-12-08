package com.samm.estalem.Faraments.ui.client_payment_fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.samm.estalem.Activities.Client.ClientMainActivity;
import com.samm.estalem.Faraments.ui.main_order_client_order.MainClientOrderFragment;
import com.samm.estalem.R;
import com.samm.estalem.Util.ShowDialog;
import com.samm.estalem.Util.Util;

public class ClientPaymentFragment extends Fragment {

    Dialog dialog;
    private ClientPaymentViewModel mViewModel;

    public static ClientPaymentFragment newInstance() {
        return new ClientPaymentFragment();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Util.language(getActivity());

        View view= inflater.inflate(R.layout.client_payment_fragment, container, false);
        dialog = ShowDialog.progres(getActivity());
        dialog.show();
        ImageView button=(ImageView) view.findViewById(R.id.img_nav_menu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ClientMainActivity)getActivity()).showNavBar();
            }
        });

        ImageView imgBack=(ImageView) view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.language(getActivity());
                MainClientOrderFragment offerPriceFragment = new MainClientOrderFragment(getActivity());
                FragmentTransaction ft = ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, offerPriceFragment);
                ft.commit();
            }
        });

        try {
            WebView webView = (WebView) view.findViewById(R.id.wvPay);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return false;
                }
            });
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("https://astalem.com/MoayserPayment/index?orderId=" + getArguments().getInt("orderId"));
        } catch (Exception e) {
        }


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

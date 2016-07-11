package com.triasbrata.foodhunter.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.triasbrata.foodhunter.LandingActivity;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.etc.Config;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "RegisterFragment";
    Button btnRegister,btnBack;
    LandingActivity aActivity;
    private EditText txtUsername;
    private EditText txtPassword;
    private EditText txtTownBase;
    private EditText txtEmail;

    public RegisterFragment() {
        // Required empty public constructor
    }


    public static RegisterFragment newInstance() {
        return  new RegisterFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnRegister.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        txtUsername = (EditText) view.findViewById(R.id.txtUsername);
        txtPassword = (EditText) view.findViewById(R.id.txtPassword);
        txtTownBase = (EditText) view.findViewById(R.id.txtTownBase);
        txtEmail = (EditText) view.findViewById(R.id.txtEmail);
        Typeface tfDc = Typeface.createFromAsset(getActivity().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-DemiCn.otf");
        Typeface tfM = Typeface.createFromAsset(getActivity().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-MediumCn.otf");
        btnBack.setTypeface(tfDc);
        btnRegister.setTypeface(tfDc);
        txtUsername.setTypeface(tfM);
        txtPassword.setTypeface(tfM);
        txtTownBase.setTypeface(tfM);
        txtEmail.setTypeface(tfM);
        aActivity = (LandingActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == btnBack.getId()){
            aActivity.loadLoginFragment();
            return;
        }
        if(v.getId() == btnRegister.getId()){
            mBtnRegisterListener();
        }

    }

    private void mBtnRegisterListener() {


        String url = Config.base_url + "register";
            JsonObject userCredential = new JsonObject();
            userCredential.addProperty("username", txtUsername.getText().toString());
            userCredential.addProperty("password", txtPassword.getText().toString());
            userCredential.addProperty("town_base", txtTownBase.getText().toString());
            userCredential.addProperty("email", txtEmail.getText().toString());
            final MaterialDialog md = new MaterialDialog.Builder(aActivity)
                    .content(R.string.please_wait)
                    .canceledOnTouchOutside(false)
                    .backgroundColorRes(R.color.white)
                    .contentColorRes(R.color.textDark)
                    .cancelable(false)
                    .progress(true, 0)
                    .show();
            Ion.with(getContext())
                    .load(url)
                    .setJsonObjectBody(userCredential)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            Log.d(TAG, "onCompleted: ", new Throwable(e.getMessage(), e.getCause()));
                            md.dismiss();
                            if (e != null) {
                                new MaterialDialog.Builder(aActivity)
                                        .backgroundColorRes(R.color.white)
                                        .contentColorRes(R.color.textDark)
                                        .content(e.getMessage())
                                        .show();
                                return;
                            }
                            registerSuccess();

                        }
                    });
        }

    private void registerSuccess() {


    }
}

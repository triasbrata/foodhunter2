package com.triasbrata.foodhunter.fragment;


import android.app.PendingIntent;
import android.content.Intent;
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
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.triasbrata.foodhunter.DashboardActivity;
import com.triasbrata.foodhunter.LandingActivity;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.etc.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "Login Fragment";
    private Button btnRegister, btnLogin;
    private EditText txtUsername, txtPassword;
    private LandingActivity mActivity;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Typeface tfDc = Typeface.createFromAsset(getActivity().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-DemiCn.otf");
        Typeface tfM = Typeface.createFromAsset(getActivity().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-MediumCn.otf");
        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        txtUsername = (EditText) view.findViewById(R.id.txtUsername);
        txtPassword = (EditText) view.findViewById(R.id.txtPassword);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnLogin.setTypeface(tfDc);
        btnRegister.setTypeface(tfDc);
        txtPassword.setTypeface(tfM);
        txtUsername.setTypeface(tfM);
        mActivity = (LandingActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public static Fragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onClick(View v) {
        if(btnLogin.getId() == v.getId()){
            btnLoginListener();
            return;
        }
        if(btnRegister.getId() == v.getId()){
            btnRegisterListener();
            return;
        }
    }

    private void btnRegisterListener() {
        mActivity.loadRegisterFragment();
    }

    private void btnLoginListener() {
        String username, password;
        username = txtUsername.getText().toString();
        password = txtPassword.getText().toString();

        mMakeRequestLogin(username, password);
    }

    public void mMakeRequestLogin(String username, String password) {
        final MaterialDialog md = new MaterialDialog.Builder(mActivity)
                .content(R.string.please_wait)
                .canceledOnTouchOutside(false)
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.textDark)
                .cancelable(false)
                .progress(true, 0)
                .show();
//        loginWithApi(username, password);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                ((ViewGroup) md.getProgressBar().getParent()).removeView((View) md.getProgressBar());
                if(e == null){
                    md.dismiss();
                    getActivity().startActivity(new Intent(getActivity(),DashboardActivity.class));
                }else{
                    e.printStackTrace();
                }
                md.setContent(R.string.login_fail);
                md.setCancelable(true);
            }
        });
    }

//    private void loginWithApi(String username, String password) {
//
//        JsonObject json = new JsonObject();
//        json.addProperty("username", username);
//        json.addProperty("password", password);
//        String url = Config.base_url + "login";
//        Ion.with(getContext())
//            .load(url)
//            .setJsonObjectBody(json)
//            .asJsonObject()
//            .setCallback(new FutureCallback<JsonObject>() {
//                @Override
//                public void onCompleted(Exception e, JsonObject result) {
//                    md.dismiss();
//                    if(e != null){
//                                new MaterialDialog.Builder(mActivity)
//                                .backgroundColorRes(R.color.white)
//                                .contentColorRes(R.color.textDark)
//                                .content(e.getMessage())
//                                .show();
//                        return;
//                    }
//                    if(!result.isJsonNull()){
//                        new MaterialDialog.Builder(mActivity)
//                                .backgroundColorRes(R.color.white)
//                                .contentColorRes(R.color.textDark)
//                                .content("Gagal Login")
//                                .show();
//                    }
//                    loginSuccess();
//
//                }
//            });
//    }

    private void loginSuccess() {

    }


}

package com.triasbrata.foodhunter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.triasbrata.foodhunter.etc.Config;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterFragment extends Fragment implements View.OnClickListener {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        btnRegister = (Button) v.findViewById(R.id.btnRegister);
        btnBack = (Button) v.findViewById(R.id.btnBack);
        btnRegister.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        txtUsername = (EditText) v.findViewById(R.id.txtUsername);
        txtPassword = (EditText) v.findViewById(R.id.txtPassword);
        txtTownBase = (EditText) v.findViewById(R.id.txtTownBase);
        txtEmail = (EditText) v.findViewById(R.id.txtEmail);
        Typeface tfDc = Typeface.createFromAsset(getActivity().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-DemiCn.otf");
        Typeface tfM = Typeface.createFromAsset(getActivity().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-MediumCn.otf");
        btnBack.setTypeface(tfDc);
        btnRegister.setTypeface(tfDc);
        txtUsername.setTypeface(tfM);
        txtPassword.setTypeface(tfM);
        txtTownBase.setTypeface(tfM);
        txtEmail.setTypeface(tfM);
        aActivity = (LandingActivity) getActivity();
        return v;
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
        RequestQueue queue = Volley.newRequestQueue(aActivity);
        String url = Config.base_url + "register";
        try {
            JSONObject userCredential = new JSONObject();
            userCredential.put("username", txtUsername.getText().toString());
            userCredential.put("password", txtPassword.getText().toString());
            userCredential.put("town_base", txtTownBase.getText().toString());
            userCredential.put("email", txtEmail.getText().toString());
            final MaterialDialog md = new MaterialDialog.Builder(aActivity)
                    .content(R.string.please_wait)
                    .canceledOnTouchOutside(false)
                    .backgroundColorRes(R.color.white)
                    .contentColorRes(R.color.textDark)
                    .cancelable(false)
                    .progress(true, 0)
                    .show();
            JsonObjectRequest request = new JsonObjectRequest(url, userCredential, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    md.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    md.dismiss();
                    new MaterialDialog.Builder(aActivity)
                            .backgroundColorRes(R.color.white)
                            .contentColorRes(R.color.textDark)
                            .content(error.getMessage())
                            .show();
                }
            });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.triasbrata.foodhunter.fragment;


import android.graphics.Typeface;
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
import com.triasbrata.foodhunter.LandingActivity;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.etc.Config;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    private View v;
    private Button btnRegister, btnLogin;
    private EditText txtUsername, txtPassword;
    private LandingActivity mActivity;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_login, container, false);
        Typeface tfDc = Typeface.createFromAsset(getActivity().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-DemiCn.otf");
        Typeface tfM = Typeface.createFromAsset(getActivity().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-MediumCn.otf");
        btnRegister = (Button) v.findViewById(R.id.btnRegister);
        btnLogin = (Button) v.findViewById(R.id.btnLogin);
        txtUsername = (EditText) v.findViewById(R.id.txtUsername);
        txtPassword = (EditText) v.findViewById(R.id.txtPassword);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnLogin.setTypeface(tfDc);
        btnRegister.setTypeface(tfDc);
        txtPassword.setTypeface(tfM);
        txtUsername.setTypeface(tfM);
        mActivity = (LandingActivity) getActivity();
        return v;
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
        RequestQueue queue = Volley.newRequestQueue(mActivity);
        String url = Config.base_url + "login";
        try {
            JSONObject userCredential = new JSONObject();
            userCredential.put("username", username);
            userCredential.put("password", password);
            final MaterialDialog md = new MaterialDialog.Builder(mActivity)
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
                    new MaterialDialog.Builder(mActivity)
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

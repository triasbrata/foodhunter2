package com.triasbrata.foodhunter.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.androidadvance.topsnackbar.TSnackbar;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.triasbrata.foodhunter.DashboardActivity;
import com.triasbrata.foodhunter.LandingActivity;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.etc.Config;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.annotations.RegExp;
import eu.inmite.android.lib.validations.form.callback.SimpleCallback;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "RegisterFragment";
    Button btnRegister,btnBack;
    LandingActivity aActivity;
    @NotEmpty(messageId = R.string.ERROR_USERNAME_EMPTY)
    @MinLength(value = 5,messageId = R.string.ERROR_USERNAME_MIN)
    private EditText txtUsername;
    @NotEmpty(messageId = R.string.ERROR_PASSWORD_EMPTY)
    private EditText txtPassword;
    @NotEmpty(messageId = R.string.ERROR_EMAIL_EMPTY)
    @RegExp(value = RegExp.EMAIL, messageId = R.string.ERROR_EMAIL_FALSE)
    private EditText txtEmail;
    @NotEmpty(messageId = R.string.ERROR_USERNAME_EMPTY)
    private EditText txtNama;
    public RegisterFragment() {
        // Required empty public constructor
    }


    public static RegisterFragment newInstance() {
        return  new RegisterFragment();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnRegister.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        txtUsername = (EditText) view.findViewById(R.id.txtUsername);
        txtPassword = (EditText) view.findViewById(R.id.txtPassword);
        txtNama = (EditText) view.findViewById(R.id.txtNama);
        txtEmail = (EditText) view.findViewById(R.id.txtEmail);
        Typeface tfDc = Typeface.createFromAsset(getActivity().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-DemiCn.otf");
        Typeface tfM = Typeface.createFromAsset(getActivity().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-MediumCn.otf");
        btnBack.setTypeface(tfDc);
        btnRegister.setTypeface(tfDc);
        txtUsername.setTypeface(tfM);
        txtPassword.setTypeface(tfM);
        txtNama.setTypeface(tfM);
        txtEmail.setTypeface(tfM);
        aActivity = (LandingActivity) getActivity();
        btnRegister.setEnabled(false);
        FormValidator.startLiveValidation(this, new SimpleCallback(getContext(),true) {
            @Override
            protected void showValidationMessage(FormValidator.ValidationFail firstFail) {
                final TSnackbar s = TSnackbar.make(view.getRootView(),firstFail.message,TSnackbar.LENGTH_INDEFINITE);
                s.setAction("X", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        s.dismiss();
                    }
                });
            }
        });

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
            mParseRegisterAction();
        }

    }

    private void mParseRegisterAction() {
        final MaterialDialog md = new MaterialDialog.Builder(aActivity)
                .content(R.string.please_wait)
                .canceledOnTouchOutside(false)
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.textDark)
                .cancelable(false)
                .progress(true, 0)
                .show();
        String password = txtPassword.getText().toString();
        String username = txtUsername.getText().toString();
        String name = txtNama.getText().toString();
        String email = txtEmail.getText().toString();
        final ParseUser user = new ParseUser();
        user.setPassword(password);
        user.setUsername(username);
        user.setEmail(email);
        final ParseObject client = new ParseObject("client");
        client.put("nama", name);
        client.put("parent", user);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                md.dismiss();
                if(e != null) {
                    client.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Intent i = new Intent(getActivity(), DashboardActivity.class);
                                i.putExtra(Config.TagBundle.LOGIN_NOTIFY, true);
                                i.putExtra(Config.TagBundle.CLIENT_NAME_NOTIFY, client.getString("name"));
                                startActivity(i);
                            }
                        }
                    });
                }else{
                    new MaterialDialog.Builder(aActivity)
                            .content(R.string.register_fail)
                            .canceledOnTouchOutside(false)
                            .backgroundColorRes(R.color.white)
                            .contentColorRes(R.color.textDark)
                            .positiveText("Coba lagi")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .progress(true, 0)
                            .show();
                }
            }
        });
    }

    private MaterialDialog makePopUp(String content) {
        return new MaterialDialog.Builder(getActivity())
                .content(content)
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.textDark)
                .positiveText("Coba Lagi")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

//    private void mBtnRegisterListener() {
//            JsonObject userCredential = new JsonObject();
//            userCredential.addProperty("username", txtUsername.getText().toString());
//            userCredential.addProperty("password", txtPassword.getText().toString());
//            userCredential.addProperty("nama", txtNama.getText().toString());
//            userCredential.addProperty("email", txtEmail.getText().toString());
//            Ion.with(getContext())
//                    .load(Config.URL.register())
//                    .setJsonObjectBody(userCredential)
//                    .asJsonObject()
//                    .setCallback(new FutureCallback<JsonObject>() {
//                        @Override
//                        public void onCompleted(@Nullable Exception e, JsonObject result) {
//                            md.dismiss();
//                            if (e instanceof Exception) {
//                                new MaterialDialog.Builder(aActivity)
//                                        .backgroundColorRes(R.color.white)
//                                        .contentColorRes(R.color.textDark)
//                                        .content(e.getMessage())
//                                        .show();
//                                return;
//                            }
//                            registerSuccess();
//
//                        }
//                    });
//        }
//
//    private void registerSuccess() {
//
//
//    }
}

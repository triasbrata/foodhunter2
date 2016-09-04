package com.triasbrata.foodhunter.fragment.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.triasbrata.foodhunter.DashboardActivity;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.etc.Config;
import com.triasbrata.foodhunter.models.Food;
import com.triasbrata.foodhunter.models.Store;


/**
 * Created by triasbrata on 11/07/16.
 */
public class DialogDetailFoodFragment extends DialogFragment{
    private static final String TAG_MODEL = "mModel";
    private SliderLayout mDemoSlider;
    private LinearLayout mCloseBtn,mNavigateBtn,mLikeBtn,mBrowseBtn;
    private TextView mFoodNameTxt,mFoodPriceTxt,mStoreNameTxt,mStoreAddress;
    private String TAG = "DialogFoodDetailFragment";
    private DashboardActivity mActivity = null;
    private Food mModel;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        registerViewProperty(view);
        changeFont();
        registerFromModel(mModel);
        makeListener();
        for(String name : mModel.getImage().getSlide()){

            DefaultSliderView slideModel = (DefaultSliderView) new DefaultSliderView(getContext())
                    .image(name)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);

            mDemoSlider.addSlider(slideModel);
        }
        PagerIndicator pdi = (PagerIndicator) view.findViewById(R.id.custom_indicator);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setBackgroundColor(Color.parseColor("#E74C3C"));
        mDemoSlider.setCustomIndicator(pdi);
        mDemoSlider.setDuration(4000);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = Food.parseJson(getArguments().getString(TAG_MODEL));
        
    }

    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
    public void onActivityCreated(Bundle savedInstanceState)
    {
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.getAttributes().windowAnimations = R.style.DialogSlideAnim;
        super.onActivityCreated(savedInstanceState);

    }
    private void makeListener() {
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btn Close");
                DialogDetailFoodFragment.this.dismiss();
            }
        });
        mLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mActivity,"Buum like it",Toast.LENGTH_LONG).show();
            }
        });
        mNavigateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity,"Buum navigate it",Toast.LENGTH_LONG).show();
            }
        });
        mBrowseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDetailFoodFragment.this.dismiss();
                Log.d(TAG, "onClick: "+Config.URL.store_detail(String.valueOf(mModel.getStore().getId())));
                Ion.with(getContext())
                    .load(Config.URL.store_detail(String.valueOf(mModel.getStore().getId())))
                    .asJsonObject()
                    .setCallback( new CallbackBrowseStore(mActivity));
            }
        });
    }

    private void registerFromModel(Food foods) {
        mFoodNameTxt.setText(foods.getName());
        mFoodPriceTxt.setText("Rp. "+String.valueOf(foods.getPrice()));
        mStoreNameTxt.setText(foods.getStore().getName());
        mStoreAddress.setText(foods.getStore().getAddress());
    }

    private void changeFont() {
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-Regular.otf");
        mFoodNameTxt.setTypeface(tf);
        mFoodPriceTxt.setTypeface(tf);
        mStoreNameTxt.setTypeface(tf);
        mStoreAddress.setTypeface(tf);
    }

    private void registerViewProperty(View view) {
        mDemoSlider = (SliderLayout)view.findViewById(R.id.slider);
        mCloseBtn = (LinearLayout) view.findViewById(R.id.btnBack);
        mNavigateBtn = (LinearLayout) view.findViewById(R.id.btnNavigate);
        mLikeBtn = (LinearLayout) view.findViewById(R.id.btnLike);
        mBrowseBtn = (LinearLayout) view.findViewById(R.id.btnBrowse);
        mFoodNameTxt = (TextView) view.findViewById(R.id.food_name);
        mFoodPriceTxt = (TextView) view.findViewById(R.id.food_price);
        mStoreNameTxt = (TextView) view.findViewById(R.id.store_name);
        mStoreAddress = (TextView) view.findViewById(R.id.store_address);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mActivity == null) mActivity = (DashboardActivity) getActivity();
        getDialog().setCanceledOnTouchOutside(true);
        return  inflater.inflate(R.layout.fragment_dialog_food_detail,container,false);
    }

    @Override
    public void onAttach(Context context) {
        if(mActivity == null) mActivity = (DashboardActivity) context;
        super.onAttach(context);
    }

    public DialogDetailFoodFragment() {
    }

    public static DialogDetailFoodFragment newInstance() {
        return  new DialogDetailFoodFragment();

    }

    public static DialogDetailFoodFragment newInstance(String model) {
     DialogDetailFoodFragment f = new DialogDetailFoodFragment();
     Bundle args = new Bundle();
     args.putString(TAG_MODEL,model);
     f.setArguments(args);
    return f;

    }

    private class CallbackBrowseStore implements FutureCallback<JsonObject> {

        private final DashboardActivity mActivity;

        public CallbackBrowseStore(DashboardActivity mActivity) {
            this.mActivity = mActivity;
        }

        @Override
        public void onCompleted(Exception e, JsonObject result) {
            if(e != null){
                Toast.makeText(mActivity,R.string.notif_detail_store_fail, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            Log.d(TAG, "onCompleted: "+result.toString());
            mActivity.loadStore(new Store(result));
        }
    }
}

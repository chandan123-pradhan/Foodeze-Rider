package com.foodeze.rider.ActivitiesAndFragments.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodeze.rider.Constants.AllConstants;
import com.foodeze.rider.Constants.PreferenceClass;
import com.foodeze.rider.Utils.FontHelper;
import com.foodeze.rider.Utils.RelateToFragment_OnBack.RootFragment;
import com.foodeze.rider.R;

/**
 * Created by Dinosoftlabs on 10/18/2019.
 */

public class RiderAccountInfoFragment extends RootFragment {

    ImageView back_icon;
    TextView user_f_name,user_l_name,user_contact_number,rider_mail;

    SharedPreferences sPref;

    View v;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         v = inflater.inflate(R.layout.rider_edit_account, container, false);
         context=getContext();

         sPref = getContext().getSharedPreferences(PreferenceClass.user, Context.MODE_PRIVATE);
         FrameLayout frameLayout = v.findViewById(R.id.account_main_container);

        FontHelper.applyFont(getContext(),frameLayout, AllConstants.verdana);
        init(v);
        return v;

    }

    public void init(View v){

        String userF_name= sPref.getString(PreferenceClass.pre_first,"");
        String userL_name = sPref.getString(PreferenceClass.pre_last,"");
        String phone_number = sPref.getString(PreferenceClass.pre_contact,"");
        String rider_email = sPref.getString(PreferenceClass.pre_email,"");


        back_icon = v.findViewById(R.id.back_icon);
        user_f_name = v.findViewById(R.id.user_f_name);
        user_l_name = v.findViewById(R.id.user_l_name);
        user_contact_number = v.findViewById(R.id.user_contact_number);
        rider_mail = v.findViewById(R.id.rider_mail);

        user_f_name.setText(userF_name);
        user_l_name.setText(userL_name);
        user_contact_number.setText(phone_number);
        rider_mail.setText(rider_email);

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    getActivity().onBackPressed();

            }
        });

    }
}

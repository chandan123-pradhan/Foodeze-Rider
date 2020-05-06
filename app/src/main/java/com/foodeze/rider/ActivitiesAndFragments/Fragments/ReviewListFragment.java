package com.foodeze.rider.ActivitiesAndFragments.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodeze.rider.Adapters.ShowReviewListAdapter;
import com.foodeze.rider.Constants.AllConstants;
import com.foodeze.rider.Constants.ApiRequest;
import com.foodeze.rider.Constants.Callback;
import com.foodeze.rider.Constants.Config;
import com.foodeze.rider.Constants.PreferenceClass;
import com.foodeze.rider.Models.RatingListModel;
import com.foodeze.rider.Utils.FontHelper;
import com.foodeze.rider.Utils.RelateToFragment_OnBack.RootFragment;
import com.foodeze.rider.R;
import com.gmail.samehadar.iosdialog.CamomileSpinner;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dinosoftlabs on 10/18/2019.
 */

public class ReviewListFragment extends RootFragment {

    ImageView back_icon;
    SharedPreferences sPref;
    ArrayList<RatingListModel> listDataReview;

    RecyclerView.LayoutManager recyclerViewlayoutManager;
    ShowReviewListAdapter recyclerViewadapter;
    RecyclerView review_recycler_view;
    SwipeRefreshLayout refresh_layout;

    CamomileSpinner progressBar;
    RelativeLayout transparent_layer,progressDialog;

    TextView total_review_tv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_list_fragment, container, false);


        FrameLayout frameLayout = view.findViewById(R.id.review_list_main);
        FontHelper.applyFont(getContext(),frameLayout, AllConstants.verdana);

        sPref = getContext().getSharedPreferences(PreferenceClass.user, Context.MODE_PRIVATE);
        total_review_tv = view.findViewById(R.id.total_review_tv);
        initUI(view);
        showRatingList();
        return view;
    }

    public void initUI(View v){

        review_recycler_view = v.findViewById(R.id.review_list_recycler_view);
        progressBar = v.findViewById(R.id.reviewProgress);
         progressBar.start();
        progressDialog = v.findViewById(R.id.progressDialog);
        transparent_layer = v.findViewById(R.id.transparent_layer);

        review_recycler_view.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(getContext());
        review_recycler_view.setLayoutManager(recyclerViewlayoutManager);

        refresh_layout = v.findViewById(R.id.swipe_refresh);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh_layout.setRefreshing(false);
            }
        });


        back_icon = v.findViewById(R.id.back_icon_review_list);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               getActivity().onBackPressed();
            }
        });


    }



    public void showRatingList(){

        transparent_layer.setVisibility(View.VISIBLE);
        progressDialog.setVisibility(View.VISIBLE);

         String user_id = sPref.getString(PreferenceClass.pre_user_id,"");
        listDataReview = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
                jsonObject.put("user_id",user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String REVIEW_API;
            REVIEW_API = Config.SHOW_RIDER_REVIEW;


        ApiRequest.Call_Api(getContext(), REVIEW_API, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                try {
                    JSONObject jsonResponse = new JSONObject(resp);
                    int code_id = Integer.parseInt(jsonResponse.optString("code"));

                    if (code_id == 200) {

                        JSONObject json = new JSONObject(jsonResponse.toString());
                        JSONArray jsonarray = json.getJSONArray("msg");

                        for (int i = 0; i<jsonarray.length();i++){

                            JSONObject jsonObject1 = jsonarray.getJSONObject(i);
                            JSONArray commentArray = jsonObject1.getJSONArray("comments");

                            for (int j = 0; j<commentArray.length();j++){

                                JSONObject jsonObject2 = commentArray.getJSONObject(j);
                                JSONObject restaurantRating;
                                if(RProfileFragment.RIDER_REVIEW){
                                    restaurantRating = jsonObject2.getJSONObject("RiderRating");
                                }
                                else {
                                    restaurantRating = jsonObject2.getJSONObject("RestaurantRating");
                                }
                                JSONObject userInfo = jsonObject2.getJSONObject("UserInfo");

                                RatingListModel ratingListModel = new RatingListModel();

                                ratingListModel.setComment(restaurantRating.optString("comment"));
                                ratingListModel.setCreated(restaurantRating.optString("created"));
                                ratingListModel.setRating(restaurantRating.optString("star"));
                                ratingListModel.setF_name(userInfo.optString("first_name"));
                                ratingListModel.setL_name(userInfo.optString("last_name"));

                                listDataReview.add(ratingListModel);
                            }

                        }

                        if(listDataReview!=null) {

                            transparent_layer.setVisibility(View.GONE);
                            progressDialog.setVisibility(View.GONE);

                            recyclerViewadapter = new ShowReviewListAdapter(listDataReview, getContext());
                            review_recycler_view.setAdapter(recyclerViewadapter);
                            recyclerViewadapter.notifyDataSetChanged();

                            total_review_tv.setText(String.valueOf(listDataReview.size())+ " REVIEWS");
                        }
                        else {
                            transparent_layer.setVisibility(View.GONE);
                            progressDialog.setVisibility(View.GONE);
                        }

                    }else {
                        transparent_layer.setVisibility(View.GONE);
                        progressDialog.setVisibility(View.GONE);
                    }



                }catch (JSONException e){

                    e.getCause();

                }

            }
        });

    }


}

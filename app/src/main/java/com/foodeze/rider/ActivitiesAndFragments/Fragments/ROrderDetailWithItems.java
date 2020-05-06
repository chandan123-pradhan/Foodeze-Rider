package com.foodeze.rider.ActivitiesAndFragments.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodeze.rider.Adapters.ExpandableListAdapter;
import com.foodeze.rider.Constants.AllConstants;
import com.foodeze.rider.Constants.ApiRequest;
import com.foodeze.rider.Constants.Callback;
import com.foodeze.rider.Constants.Config;
import com.foodeze.rider.Constants.PreferenceClass;
import com.foodeze.rider.Models.MenuItemExtraModel;
import com.foodeze.rider.Models.MenuItemModel;
import com.foodeze.rider.Utils.CustomExpandableListView;
import com.foodeze.rider.Utils.FontHelper;
import com.foodeze.rider.Utils.RelateToFragment_OnBack.RootFragment;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.foodeze.rider.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dinosoftlabs on 10/18/2019.
 */
public class ROrderDetailWithItems extends RootFragment {

    TextView order_title_tv;
    SharedPreferences sPref_Items;
    String order_number;
    ExpandableListAdapter listAdapter;
    CustomExpandableListView customExpandableListView;
    ArrayList<MenuItemModel> listDataHeader;
    ArrayList<MenuItemExtraModel> listChildData;
    private ArrayList<ArrayList<MenuItemExtraModel>> ListChild;
    CamomileSpinner orderProgress;
    RelativeLayout transparent_layer,progressDialog;
    ImageView back_icon;

    View v;
    Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.order_detail_items, container, false);
         context=getContext();

         FrameLayout frameLayout = v.findViewById(R.id.main_order_item_detail);
         FontHelper.applyFont(getContext(),frameLayout, AllConstants.verdana);

        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        sPref_Items = getContext().getSharedPreferences(PreferenceClass.user, Context.MODE_PRIVATE);

        initUI(v);


        customExpandableListView = (CustomExpandableListView ) v.findViewById(R.id.custon_list_order_items);
        customExpandableListView .setExpanded(true);
        customExpandableListView.setGroupIndicator(null);

        customExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true; // This way the expander cannot be collapsed
            }
        });
        return v;
    }

    public void initUI(View v){

        order_number = sPref_Items.getString(PreferenceClass.RIDER_ORDER_NUMBER,"");
        order_title_tv = v.findViewById(R.id.order_title_tv);
        order_title_tv.setText("Order #"+order_number);
        orderProgress = v.findViewById(R.id.orderProgress);
        orderProgress.start();
        progressDialog = v.findViewById(R.id.progressDialog);
        transparent_layer = v.findViewById(R.id.transparent_layer);
        back_icon = v.findViewById(R.id.back_icon);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();

            }
        });


        getOrderDetailItems();


    }


    public void getOrderDetailItems(){

        transparent_layer.setVisibility(View.VISIBLE);
        progressDialog.setVisibility(View.VISIBLE);
        listDataHeader = new ArrayList<MenuItemModel>();
        ListChild = new ArrayList<>();


        JSONObject orderJsonObject = new JSONObject();
        try {
            orderJsonObject.put("order_id",order_number);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.Call_Api(context, Config.SHOW_ORDER_DETAIL, orderJsonObject, new Callback() {
            @Override
            public void Responce(String resp) {


                try {
                    JSONObject jsonResponse = new JSONObject(resp);

                    int code_id  = Integer.parseInt(jsonResponse.optString("code"));

                    if(code_id == 200) {

                        JSONObject json = new JSONObject(jsonResponse.toString());
                        JSONArray jsonArray = json.getJSONArray("msg");

                        for (int i=0;i<jsonArray.length();i++){

                            JSONObject allJsonObject = jsonArray.getJSONObject(i);
                            JSONObject restaurantJsonObject = allJsonObject.getJSONObject("Restaurant");
                            JSONObject restaurantCurrencuObj = restaurantJsonObject.getJSONObject("Currency");
                            String currency_symbol= restaurantCurrencuObj.optString("symbol");

                          JSONArray menuItemArray = allJsonObject.getJSONArray("OrderMenuItem");

                            for (int j=0;j<menuItemArray.length();j++) {

                                JSONObject alljsonJsonObject2 = menuItemArray.getJSONObject(j);
                                MenuItemModel menuItemModel = new MenuItemModel();
                                menuItemModel.setItem_name(alljsonJsonObject2.optString("name"));
                                menuItemModel.setItem_price(currency_symbol + alljsonJsonObject2.optString("price"));
                                menuItemModel.setId(alljsonJsonObject2.optString("id"));
                                menuItemModel.setOrder_id(alljsonJsonObject2.optString("order_id"));
                                menuItemModel.setOrder_quantity(alljsonJsonObject2.optString("quantity"));

                                listDataHeader.add(menuItemModel);

                                listChildData = new ArrayList<>();

                                JSONArray extramenuItemArray = alljsonJsonObject2.getJSONArray("OrderMenuExtraItem");
                                if(extramenuItemArray!=null&& extramenuItemArray.length()>0){
                                    for (int k = 0; k < extramenuItemArray.length(); k++) {
                                        if (extramenuItemArray.length() != 0) {
                                            JSONObject allJsonObject3 = extramenuItemArray.getJSONObject(k);
                                            MenuItemExtraModel menuItemExtraModel = new MenuItemExtraModel();

                                            menuItemExtraModel.setExtra_item_name(allJsonObject3.optString("name"));
                                            menuItemExtraModel.setPrice(allJsonObject3.optString("price"));
                                            menuItemExtraModel.setQuantity(allJsonObject3.optString("quantity"));
                                            menuItemExtraModel.setCurrency(currency_symbol);

                                            listChildData.add(menuItemExtraModel);

                                        }

                                    }

                                }
                                ListChild.add(listChildData);
                            }
                        }

                        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, ListChild);
                         customExpandableListView.setAdapter(listAdapter);
                        for(int l=0; l < listAdapter.getGroupCount(); l++)
                            if(ListChild.size()!=0) {
                                customExpandableListView.expandGroup(l);
                            }

                    }

                    transparent_layer.setVisibility(View.GONE);
                    progressDialog.setVisibility(View.GONE);

                }catch (Exception e){
                    e.getMessage();
                    transparent_layer.setVisibility(View.GONE);
                    progressDialog.setVisibility(View.GONE);
                }

            }
        });



    }

}

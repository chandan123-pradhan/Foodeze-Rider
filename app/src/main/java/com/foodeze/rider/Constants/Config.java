package com.foodeze.rider.Constants;

/**
 * Created by Dinosoftlabs on 11/14/2015.
 */
public class Config {


  public static String baseURL = "http://radssoon.com/foodeze/mobileapp_api/api/";



    public static final String LOGIN_URL = baseURL+"login";

    public static final String CHANGE_PASSWORD = baseURL+"changePassword";

    public static final String forgotPassword = baseURL+"forgotPassword";
    public static final String verifyforgotPasswordCode = baseURL+"verifyforgotPasswordCode";
    public static final String changePasswordForgot = baseURL+"changePasswordForgot";


    public static final String SHOW_ORDER_DETAIL = baseURL+"showOrderDetail";


    public static final String SHOW_RIDER_TRACKING = baseURL+"showRiderTracking";
    public static final String TRACK_RIDER_STATUS = baseURL+"trackRiderStatus";

    public static final String ADD_LOCATIONS = baseURL+"addRiderLocation";
    public static final String SHOW_RIDER_ORDER_BASE_ONDATE= baseURL+"showRiderOrdersBasedOnDate";
    public static final String SHOW_UP_COMMING_RIDER_SHIFTS = baseURL+"showUpComingRiderShifts";
    public static final String UPDATE_RIDER_STATUS = baseURL+"checkIn";
    public static final String Accept_RIDER_ORDER=baseURL+"updateRiderOrderStatus";
    public static final String SHOW_RIDER_REVIEW = baseURL+"showRiderRatings";


    public static final String GET_CITY_BOUNDRIES = "http://maps.google.com/maps/api/geocode/json?address=";


    public static final String TOPIC_GLOBAL = "global";

    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    public static final int NOTIFICATION_ID = 100;




}

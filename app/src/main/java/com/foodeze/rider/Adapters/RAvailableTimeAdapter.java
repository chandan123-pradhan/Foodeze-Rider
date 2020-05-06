package com.foodeze.rider.Adapters;

import android.content.Context;
import androidx.appcompat.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.foodeze.rider.R;
import com.foodeze.rider.Models.RShiftModel;
import com.foodeze.rider.Models.RParentModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by Dinosoftlabs on 10/18/2019.
 */
public class RAvailableTimeAdapter extends BaseExpandableListAdapter {

    Context context;
    ArrayList<RParentModel> ListTerbaru;
    ArrayList<ArrayList<RShiftModel>> ListChildTerbaru;
    int count;

    ListPopupWindow listPopupWindow;
    String[] products = {"Delete"};

    public RAvailableTimeAdapter (Context context, ArrayList<RParentModel>ListTerbaru, ArrayList<ArrayList<RShiftModel>> ListChildTerbaru){
        this.context=context;
        this.ListTerbaru=ListTerbaru;
        this.ListChildTerbaru=ListChildTerbaru;
//      this.count=ListTerbaru.size();
//      this.count=ListChildTerbaru.size();
    }
    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }


    @Override
    public RShiftModel getChild(int groupPosition, int childPosition) {
        return ListChildTerbaru.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        RShiftModel childTerbaru = getChild(groupPosition, childPosition);
        RAvailableTimeAdapter.ViewHolder holder= null;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_item_available_child, null);

            holder=new RAvailableTimeAdapter.ViewHolder();
            holder.child_tv=(TextView)convertView.findViewById(R.id.child_tv);
            convertView.setTag(holder);

        }
        else{
            holder=(RAvailableTimeAdapter.ViewHolder)convertView.getTag();
        }

        String endTime = childTerbaru.getEnd_time();

     String   startTime = childTerbaru.getStart_time();
        StringTokenizer tk = new StringTokenizer(startTime);
        String date = tk.nextToken();
        String time = startTime;
        String time2 = endTime;

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        Date dt1,dt2;
        try {
            dt1 = sdf.parse(time);
            dt2 = sdf.parse(time2);
         //   System.out.println("Time Display: " + sdfs.format(dt)); // <-- I got result here
            holder.child_tv.setText(sdfs.format(dt1)+"-"+sdfs.format(dt2));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }





       // holder.child_tv.setText(childTerbaru.getStart_time()+childTerbaru.getEnd_time());



     //   holder.child_tv.setText(childTerbaru.getQuantity()+"x "+childTerbaru.getExtra_item_name()+" +"+childTerbaru.getCurrency()+childTerbaru.getPrice());


        return convertView;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return ListChildTerbaru.get(groupPosition).size();
    }

    @Override
    public RParentModel getGroup(int groupPosition) {
        return ListTerbaru.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return ListTerbaru.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        RParentModel terbaruModel = getGroup(groupPosition);
        RAvailableTimeAdapter.ViewHolder holder= null;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_item_available_parent, null);

            holder=new RAvailableTimeAdapter.ViewHolder();
            holder.parent_tv=(TextView)convertView.findViewById(R.id.parent_tv);
            convertView.setTag(holder);

        }
        else{
            holder=(RAvailableTimeAdapter.ViewHolder)convertView.getTag();
        }
        String dtStart = terbaruModel.getDate();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfs = new SimpleDateFormat("dd-MM-yyyy");
        Date dt1;
        try {
            dt1 = sdf.parse(dtStart);



               holder.parent_tv.setText( getDayFromDateString(dtStart,"yyyy-MM-dd"));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



      return convertView;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }


    static class ViewHolder{
        TextView parent_tv,child_tv;
    }

    public static String getRequiedformatDate(String serverFormate,
                                              String requiredFormate, String ourDate) {
        DateFormat theDateFormat = new SimpleDateFormat(serverFormate,Locale.getDefault());
        Date date = null;
        String dateStr = "";
        try {
            if (ourDate != null && !ourDate.equals("null")) {
                date = theDateFormat.parse(ourDate);
                theDateFormat = new SimpleDateFormat(requiredFormate,Locale.ENGLISH);
                dateStr = theDateFormat.format(date).toString();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return dateStr;
    }



    public static String getDayFromDateString(String stringDate,String dateTimeFormat)
    {
        String[] daysArray = new String[] {"Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday"};
        String[] monthArray = new String[]{"January","February","March","April","May","June","July","August","September","October",
                                            "November","December"};
        String day = "";
        String month ="";
        String year = "";
        int dayOfWeek =0;
        int monthOfWeek = 0;
        SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
        Date date;
        try {
            date = formatter.parse(stringDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;
            monthOfWeek = c.get(Calendar.MONTH)-1;
            year = String.valueOf(c.get(Calendar.YEAR));
            if (dayOfWeek < 0) {
                dayOfWeek += 7;
            }
            if(monthOfWeek<0){
                monthOfWeek+=12;
            }
            day = daysArray[dayOfWeek];
            month = monthArray[monthOfWeek];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return month+" "+day+" "+year;
    }


}
package com.example.praneethguduguntla.medic;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class MedViewAdapter extends BaseAdapter {
    ArrayList<String> messages = new ArrayList<String>();
    Context context;
    ArrayList<Boolean> hasTakenMedication = new ArrayList<Boolean>();


    public void setMessages(ArrayList<String> arr){
        messages = arr;
    }

    public void setHasTakenMedication(ArrayList<Boolean> hasTakenMedication){
        this.hasTakenMedication = hasTakenMedication;
    }

    public MedViewAdapter(Context context){
        this.context = context;
    }

    public void add(String m){
        messages.add(m);
        notifyDataSetChanged();
    }

    public int getCount(){
        return messages.size();
    }

    public Object getItem(int i){
        return messages.get(i);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MedHolder holder = new MedHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        String element = messages.get(i);
        boolean currBool = hasTakenMedication.get(i);
        if(currBool){
            convertView = messageInflater.inflate(R.layout.green_row, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);
            holder.messageBody.setText(element);
        }
        else{
            convertView = messageInflater.inflate(R.layout.red_row, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);
            holder.messageBody.setText(element);
        }



        return convertView;
    }

}

class MedHolder {
    public TextView name;
    public TextView messageBody;
}



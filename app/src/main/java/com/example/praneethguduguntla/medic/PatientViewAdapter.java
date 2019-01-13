package com.example.praneethguduguntla.medic;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class PatientViewAdapter extends BaseAdapter {
    ArrayList<String> messages = new ArrayList<String>();
    Context context;



    public PatientViewAdapter(Context context){
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
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        String element = messages.get(i);

        convertView = messageInflater.inflate(R.layout.row, null);
        holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
        convertView.setTag(holder);
        holder.messageBody.setText(element);


        return convertView;
    }

}

class PatientHolder {
    public TextView name;
    public TextView messageBody;
}



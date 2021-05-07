package com.winkbr.browser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winkbr.browser.Model.dbModel;
import com.winkbr.browser.R;

import java.util.ArrayList;

public class dbAdapter extends BaseAdapter {

    Context context;
    ArrayList<dbModel> arrayList;
    public dbAdapter(Context context, ArrayList<dbModel> arrayList){
        this.context=context;
        this.arrayList=arrayList;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_model, null);

            TextView t1_id = (TextView) convertView.findViewById(R.id.id_txt);
            TextView t2_Title = (TextView) convertView.findViewById(R.id.Title_txt);
            TextView t3_url = (TextView) convertView.findViewById(R.id.url_txt);


            dbModel dbModel = arrayList.get(position);
            t1_id.setText(String.valueOf(dbModel.getId()));
            t2_Title.setText(dbModel.getName());
            t3_url.setText(dbModel.getAge());



        return convertView;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }
}

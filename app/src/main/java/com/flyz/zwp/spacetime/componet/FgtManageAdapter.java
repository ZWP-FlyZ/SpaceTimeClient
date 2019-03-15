package com.flyz.zwp.spacetime.componet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.flyz.zwp.spacetime.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zwp12 on 2017/4/17.
 */

public class FgtManageAdapter extends BaseAdapter {

    private ArrayList<HashMap<String,Object>> datas = null;
    private LayoutInflater inflater = null;
    private Context context;


    public FgtManageAdapter(Context context, ArrayList<HashMap<String, Object>> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder viewHolder = null;
        if(convertView==null){
            viewHolder = new ItemHolder();
            convertView = inflater.inflate(R.layout.item_mag_fgts,null);
            viewHolder.cbSelect = (CheckBox)convertView.findViewById(R.id.item_mag_fgts_cb);
            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.item_mag_fgts_ftitle);
            viewHolder.tvState = (TextView)convertView.findViewById(R.id.item_mag_fgts_state);
            viewHolder.tvftDetail = (TextView)convertView.findViewById(R.id.item_mag_fgts_ftdetail);
            viewHolder.tvfpDetail = (TextView)convertView.findViewById(R.id.item_mag_fgts_fpdetail);
            viewHolder.tvcTime = (TextView)convertView.findViewById(R.id.item_mag_fgts_ctime);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ItemHolder)convertView.getTag();
        }
        // new String[]{"fTile","fState","ftDetail","fpDetail","cTime"},
        //boolean b = (boolean)datas.get(position).get("isSelected")
        //if(datas.size()==0)return convertView;
        viewHolder.cbSelect.setChecked( (boolean)datas.get(position).get("isSelected"));
        viewHolder.tvTitle.setText((String)datas.get(position).get("fTile"));
        viewHolder.tvState.setText((String)datas.get(position).get("fState"));
        viewHolder.tvftDetail.setText((String)datas.get(position).get("ftDetail"));
        viewHolder.tvfpDetail.setText((String)datas.get(position).get("fpDetail"));
        viewHolder.tvcTime.setText((String)datas.get(position).get("cTime"));
        return convertView;
    }

    static class ItemHolder {

        public CheckBox cbSelect;
        public TextView tvTitle;
        public TextView tvfpDetail;
        public TextView tvftDetail;
        public TextView tvState;
        public TextView tvcTime;
    }
}

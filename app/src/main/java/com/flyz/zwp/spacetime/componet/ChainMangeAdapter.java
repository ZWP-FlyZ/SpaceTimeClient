package com.flyz.zwp.spacetime.componet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.flyz.zwp.spacetime.R;
import com.flyz.zwp.spacetime.model.ItemChain;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zwp12 on 2017/4/17.
 */

public class ChainMangeAdapter extends BaseAdapter {

    private List<ItemChain> datas = null;
    private LayoutInflater inflater = null;
    private Context context;

    public ChainMangeAdapter(Context context, List<ItemChain> datas) {
        this.context = context;
        this.datas = datas;
        inflater  = LayoutInflater.from(context);
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
        ItemViewHolder viewHolder = null;

        if(convertView==null){
            viewHolder = new ItemViewHolder();
            convertView = inflater.inflate(R.layout.item_mag_chain,null);
            viewHolder.cbSelect = (CheckBox)convertView.findViewById(R.id.item_mag_chain_cb);
            viewHolder.tvName = (TextView)convertView.findViewById(R.id.item_mag_chain_name);
            viewHolder.tvDetail = (TextView)convertView.findViewById(R.id.item_mag_chain_detail);
            viewHolder.tvState = (TextView)convertView.findViewById(R.id.item_mag_chain_state);
            viewHolder.tvsTime = (TextView)convertView.findViewById(R.id.item_mag_chain_sTime);
            viewHolder.tveTime = (TextView)convertView.findViewById(R.id.item_mag_chain_eTime);
            viewHolder.tvcTime = (TextView)convertView.findViewById(R.id.item_mag_chain_cTime);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ItemViewHolder)convertView.getTag();
        }

        viewHolder.cbSelect.setChecked((datas.get(position).isSelected()));
        Logger.d("position = "+position+" sel = "+datas.get(position).isSelected());
        viewHolder.tvName.setText(datas.get(position).getName());
        viewHolder.tvDetail.setText(datas.get(position).getName());
        viewHolder.tvState.setText(datas.get(position).getState());
        viewHolder.tvsTime.setText(datas.get(position).getsTime());
        viewHolder.tveTime.setText(datas.get(position).geteTime());
        viewHolder.tvcTime.setText(datas.get(position).getcTime());


        return convertView;
    }


     static class ItemViewHolder  {
        public CheckBox cbSelect;
        public TextView tvName;
        public TextView tvDetail;
        public TextView tvState;
        public TextView tvsTime;
        public TextView tveTime;
        public TextView tvcTime;
    }

}

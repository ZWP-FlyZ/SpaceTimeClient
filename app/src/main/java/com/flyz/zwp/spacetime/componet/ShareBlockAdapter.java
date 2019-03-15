package com.flyz.zwp.spacetime.componet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyz.zwp.spacetime.App;
import com.flyz.zwp.spacetime.R;
import com.flyz.zwp.spacetime.activities.MaActivity;
import com.flyz.zwp.spacetime.activities.MemChainActivity_;
import com.flyz.zwp.spacetime.activities.MemFragmentActivity_;
import com.flyz.zwp.spacetime.activities.UserInfoActivity_;
import com.flyz.zwp.spacetime.model.ShareBlock;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by zwp12 on 2017/4/19.
 */

public class ShareBlockAdapter extends BaseAdapter {


    private List<ShareBlock> blocks =null;
    private MaActivity context;
    private LayoutInflater inflater = null;
    private App app;
    private ShareBlockAdapter adapter;

    public ShareBlockAdapter(MaActivity context, List<ShareBlock> blocks) {
        this.context = context;
        this.blocks = blocks;
        inflater = LayoutInflater.from(context);
        app = (App)context.getApplication();
        adapter = this;
    }

    @Override
    public int getCount() {
        return blocks.size();
    }

    @Override
    public Object getItem(int position) {
        return blocks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder =null;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_share_block,parent ,false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(blocks.size()==0) return convertView;
        viewHolder.tvuName.setOnClickListener(new MyInfoListener(position));
        //viewHolder.btnPoker.setOnClickListener(new MyPokerListener(position));
        viewHolder.llDatas.setOnClickListener(new MyDataListener(position));
        viewHolder.tvuName.setText(blocks.get(position).getUNickName());
        viewHolder.tvsData1.setText(blocks.get(position).getSData1());
        viewHolder.tvsData2.setText(blocks.get(position).getSData2());
        viewHolder.tvsData3.setText(blocks.get(position).getSData3());
        viewHolder.tvsTime.setText(blocks.get(position).getSTime());
        viewHolder.tvPokerC.setText(blocks.get(position).getPokerCount()+"");

        return convertView;
    }


    class MyInfoListener implements View.OnClickListener{
        private int pos;

        public MyInfoListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            i.putExtra("uId",blocks.get(pos).getUId());
            i.putExtra("uNickName",blocks.get(pos).getUNickName());
            i.putExtra("isOp",false);
            i.setClass(context,UserInfoActivity_.class);
            context.startActivity(i);
            Logger.d("show user info ="+blocks.get(pos).getUId());
        }
    }

    class MyPokerListener implements  View.OnClickListener{
        private int pos;

        public MyPokerListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            int c = blocks.get(pos).getPokerCount();
            c++;
            blocks.get(pos).setPokerCount(c);
            adapter.notifyDataSetChanged();
            v.setEnabled(false);
            Logger.d("sId is poked ="+ blocks.get(pos).getPokerCount()+"\npos= "+pos);
        }
    }

    class MyDataListener implements  View.OnClickListener{
        private int pos;

        public MyDataListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            ShareBlock block = blocks.get(pos);
            if(block.getSFlag()==0) {
                //fagment
                i.putExtra("FId",block.getSId());
                i.putExtra("uId",block.getUId());
                i.putExtra("FTitle",block.getSData1());
                i.putExtra("isOp",false);
                i.setClass(context,MemFragmentActivity_.class);
            }else if(block.getSFlag()==1){
                i.putExtra("ChId",block.getSId());
                i.putExtra("uId",block.getUId());
                i.putExtra("isOp",false);
                i.setClass(context,MemChainActivity_.class);
                //chain
            }
            context.startActivity(i);
        }
    }

    static class ViewHolder {
        public TextView tvuName;
        public LinearLayout llDatas;
        public TextView tvsData1;
        public TextView tvsData2;
        public TextView tvsData3;
        public TextView tvsTime;
        public Button btnPoker;
        public TextView tvPokerC;
        public Button btndPoker;
        public TextView tvdPokerC;
        public ViewHolder(View v){
            tvuName = (TextView) v.findViewById(R.id.tv_share_block_user_name);
            llDatas = (LinearLayout)v.findViewById(R.id.ll_share_block_data);
            tvsData1 = (TextView) v.findViewById(R.id.tv_share_block_s_data1);
            tvsData2 = (TextView) v.findViewById(R.id.tv_share_block_s_data2);
            tvsData3 = (TextView) v.findViewById(R.id.tv_share_block_s_data3);
            tvsTime = (TextView) v.findViewById(R.id.tv_share_block_s_time);
            tvPokerC = (TextView) v.findViewById(R.id.tv_share_block_poker_c);
            tvdPokerC = (TextView) v.findViewById(R.id.tv_share_block_dpoker_c);

            btnPoker = (Button) v.findViewById(R.id.btn_share_block_poker);
            btndPoker = (Button)v.findViewById(R.id.btn_share_block_dpoker);

            tvdPokerC.setVisibility(View.GONE);
            btndPoker.setVisibility(View.GONE);
            tvPokerC.setVisibility(View.GONE);
            btnPoker.setVisibility(View.GONE);
        }

    }
}

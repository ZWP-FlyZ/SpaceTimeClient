package com.flyz.zwp.spacetime.componet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by zwp12 on 2017/5/11.
 */

public class MyScrollView extends ScrollView {

    private OnScrollBottomListener _listener;
    private int _calCount;

    public interface OnScrollBottomListener {
        void srollToBottom();
    }

    public void registerOnScrollViewScrollToBottom(OnScrollBottomListener l) {
        _listener = l;
    }

    public void unRegisterOnScrollViewScrollToBottom() {
        _listener = null;
    }


    public MyScrollView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = this.getChildAt(0);
        if (this.getHeight() + this.getScrollY() == view.getHeight()) {
            _calCount++;
            if (_calCount == 1) {
                if (_listener != null) {
                    _listener.srollToBottom();
                }
            }
        } else {
            _calCount = 0;
        }
    }



}

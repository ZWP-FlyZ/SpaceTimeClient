package com.flyz.zwp.spacetime.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.flyz.zwp.spacetime.R;
import com.flyz.zwp.spacetime.componet.MyTime;
import com.flyz.zwp.spacetime.componet.TimeEnum.*;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

@EActivity
public class TimeCrtActivity extends AppCompatActivity {

    MyTime time = null;
    int tFlag = 0;
    String detail ="";
    String[] mYearUnitItem_1 = null;
    String[] mYearUnitItem_2 = null;
    String[] moreDataHints = null;
    String[] moreDataBtns    = null;
    String tmp;
    String tmp2;
    int tmpInt;
    int moreDateIndex = -1;
    int timeOriFlag = -1;
    int timeYearFlag = -1;



    ArrayAdapter<String> adapterYearUnit = null;

    @ViewById(R.id.ll_time_crt_2)
    LinearLayout linearLayout_2;

    @ViewById(R.id.ll_time_crt_3)
    LinearLayout linearLayout_3;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.tv_time_crt_desc)
    TextView tvTimeDesc;

    @ViewById(R.id.btn_time_crt_reset)
    Button btnReset;

    @ViewById(R.id.spn_time_crt_ori)
    Spinner spnTimeOri;
    @ViewById(R.id.spn_time_crt_year_unit)
    Spinner spnYearUnit;

    @ViewById(R.id.et_time_crt_year)
    EditText etYearData;

    @ViewById(R.id.et_time_crt_more)
    EditText etMoreData;

    @ViewById(R.id.btn_time_crt_add_more)
    Button btnMoreData;


    @TextChange(R.id.et_time_crt_year)
    void etChanged(){
        tmp = etYearData.getText().toString();
        if(tmp==null||tmp.equals("")) {
            time.setYear(1);
        }else {
            tmpInt = Integer.parseInt(tmp);
            if(tmpInt<1) time.setYear(1);
            else time.setYear(tmpInt);
        }
        testShowTime();
    }

    @Click(R.id.btn_time_crt_reset)
    void timeReset(){
        time.clear();
        testShowTime();
        linearLayout_2.setVisibility(View.VISIBLE);

        enableOthers();
        timeOriFlag = TimeOri.A_D.getIndex();
        timeYearFlag= TimeYearUint.YEAR.getIndex();

        spnYearUnit.setSelection(0);
        spnTimeOri.setSelection(0);
        timeOriFlag=TimeOri.A_D.getIndex();
        etMoreData.setText("");
        etYearData.setText("");
        moreDateIndex = TimeMoreUint.MONTH.getIndex();
        etMoreData.setHint(moreDataHints[moreDateIndex]);
        btnMoreData.setText(moreDataBtns[moreDateIndex]);
    }

    boolean checkInput(String v,int index){
        if(v==null||v.equals(""))return false;
        return true;
    }

    void disableOthers(){
        spnTimeOri.setEnabled(false);
        spnYearUnit.setEnabled(false);
        etYearData.setEnabled(false);
    }
    void enableOthers(){
        spnTimeOri.setEnabled(true);
        spnYearUnit.setEnabled(true);
        etYearData.setEnabled(true);
        etMoreData.setEnabled(true);
        btnMoreData.setEnabled(true);
    }
    @Click(R.id.btn_time_crt_add_more)
    void addMoreDate(){
        tmp2 = etMoreData.getText().toString();

        //输入结束 moreDataIndex == 5
        if(moreDateIndex==TimeMoreUint.NIAN_DAI.getIndex()-1){
//            Intent i = new Intent();
//            Bundle b  = new Bundle();
//            b.putInt("tFlag",
//                    time.getTimeFlag());
//            b.putString("detail",
//                    time.getTimeDetail());
//            setResult(1110, i.putExtras(b));
           testShowTime();
            btnMoreData.setEnabled(false);
        }
        else  if(moreDateIndex==TimeMoreUint.NIAN_DAI.getIndex()){//年代
            if(checkInput(tmp2,moreDateIndex)){
                disableOthers();
                moreDateIndex=moreDateIndex-1;
                etMoreData.setHint(moreDataHints[moreDateIndex]);
                etMoreData.setEnabled(false);
                btnMoreData.setText(moreDataBtns[moreDateIndex]);
                time.setNianDai(Integer.parseInt(tmp2));
                etMoreData.setText("");
                testShowTime();
            }
        }//end NianDai
        else {
            if(checkInput(tmp2,moreDateIndex)){
                disableOthers();
                time.setMoreDataByIndex(moreDateIndex,Integer.parseInt(tmp2));
                moreDateIndex = moreDateIndex-1;
                if((moreDateIndex==TimeMoreUint.HOUR.getIndex()
                        &&timeOriFlag==TimeOri.B_C.getIndex())||moreDateIndex<0) {//结束
                    moreDateIndex=TimeMoreUint.NIAN_DAI.getIndex()-1;
                    etMoreData.setEnabled(false);
                }
                etMoreData.setText("");
                etMoreData.setHint(moreDataHints[moreDateIndex]);
                btnMoreData.setText(moreDataBtns[moreDateIndex]);
                testShowTime();
            }
        }//end else
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_crt);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        tFlag = i.getIntExtra("tFlag",0);
        detail = i.getStringExtra("detail");
        time = MyTime.getMyTimeByDetail(tFlag,detail);
        tvTimeDesc.setText(time.toDescribe());

        moreDataHints = getResources().getStringArray(R.array.more_date_et);
        moreDataBtns  = getResources().getStringArray(R.array.more_date_btn);

        setYearUintSpinner();
        setOriSpinner();
    }

    public void setYearUintSpinner(){
        mYearUnitItem_1 = getResources().getStringArray(R.array.time_year_1);
        mYearUnitItem_2 = getResources().getStringArray(R.array.time_year_2);
        adapterYearUnit = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adapterYearUnit.
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYearUnit.setAdapter(adapterYearUnit);
        spnYearUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                linearLayout_3.setVisibility(View.GONE);
                timeYearFlag = position;
                if(timeOriFlag!=TimeOri.AGO.getIndex()
                        &&(timeYearFlag==TimeYearUint.YEAR.getIndex()
                        || timeYearFlag==TimeYearUint.CENTURY.getIndex()))
                    linearLayout_3.setVisibility(View.VISIBLE);
                if(position == TimeYearUint.YEAR.getIndex()){
                    Logger.d("YEAR");
                    moreDateIndex = TimeMoreUint.MONTH.getIndex();
                    time.setTimeYearUint(TimeYearUint.YEAR);
                    etMoreData.setHint(moreDataHints[moreDateIndex]);
                    btnMoreData.setText(moreDataBtns[moreDateIndex]);

                }
                else if(position == TimeYearUint.CENTURY.getIndex()){
                    Logger.d("CENTURY");
                    time.setTimeYearUint(TimeYearUint.CENTURY);
                    moreDateIndex =TimeMoreUint.NIAN_DAI.getIndex();
                    etMoreData.setHint(moreDataHints[moreDateIndex]);
                    btnMoreData.setText(moreDataBtns[moreDateIndex]);
                }
                else if(position == TimeYearUint.HUNDRED_YEAR.getIndex()){
                    Logger.d("HUNDRED_YEAR");
                    time.setTimeYearUint(TimeYearUint.HUNDRED_YEAR);
                }
                else if(position == TimeYearUint.THOUSAND_YEAR.getIndex()){
                    Logger.d("THOUSAND_YEAR");
                    time.setTimeYearUint(TimeYearUint.THOUSAND_YEAR);
                }
                else if(position == TimeYearUint.TEN_THOUSAND_YEAR.getIndex()){
                    Logger.d("TEN_THOUSAND_YEAR");
                    time.setTimeYearUint(TimeYearUint.TEN_THOUSAND_YEAR);
                }
                else if(position == TimeYearUint.MILLON_YEAR.getIndex()){
                    Logger.d("MILLON_YEAR");
                    time.setTimeYearUint(TimeYearUint.MILLON_YEAR);
                }
                else if(position == TimeYearUint.TEN_MILLON_YEAR.getIndex()){
                    Logger.d("TEN_MILLON_YEAR");
                    time.setTimeYearUint(TimeYearUint.TEN_MILLON_YEAR);
                }
                else if(position == TimeYearUint.HUNDRED_MILLON_YEAR.getIndex()){
                    Logger.d("HUNDRED_MILLON_YEAR");
                    time.setTimeYearUint(TimeYearUint.HUNDRED_MILLON_YEAR);
                }
                testShowTime();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setOriSpinner(){



        spnTimeOri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position++;
                linearLayout_3.setVisibility(View.GONE);
                timeOriFlag = position;
                if(timeOriFlag!=TimeOri.AGO.getIndex()
                        &&(timeYearFlag==TimeYearUint.YEAR.getIndex()
                        || timeYearFlag==TimeYearUint.CENTURY.getIndex()))
                    linearLayout_3.setVisibility(View.VISIBLE);

                    if(position== TimeOri.A_D.getIndex()){
                       Logger.d("A_D");
                        adapterYearUnit.clear();
                        adapterYearUnit.addAll(mYearUnitItem_1);
                        adapterYearUnit.notifyDataSetChanged();
                        spnYearUnit.setSelection(0);
                        timeOriFlag = TimeOri.A_D.getIndex();
                        time.setTimeOri(TimeOri.A_D);
                    }
                    else if(position== TimeOri.B_C.getIndex()){
                        Logger.d("B_C");
                        adapterYearUnit.clear();
                        adapterYearUnit.addAll(mYearUnitItem_2);
                        adapterYearUnit.notifyDataSetChanged();
                        timeOriFlag = TimeOri.B_C.getIndex();
                        time.setTimeOri(TimeOri.B_C);
                        spnYearUnit.setSelection(0);
                    }
                    else if(position== TimeOri.AGO.getIndex()){
                        Logger.d("AGO");
                        adapterYearUnit.clear();
                        adapterYearUnit.addAll(mYearUnitItem_2);
                        adapterYearUnit.notifyDataSetChanged();
                        timeOriFlag = TimeOri.AGO.getIndex();
                        time.setTimeOri(TimeOri.AGO);
                    }

                testShowTime();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void testShowTime(){
        tvTimeDesc.setText(time.toDescribe());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_fgt, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_memfgt) {
            Intent i = new Intent();
            Bundle b  = new Bundle();
            b.putInt("tFlag",
                    time.getTimeFlag());
            b.putString("detail",
                    time.getTimeDetail());
            setResult(1110, i.putExtras(b));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        Bundle b  = new Bundle();
        b.putInt("tFlag",
                time.getTimeFlag());
        b.putString("detail",
                time.getTimeDetail());
        setResult(1110, i.putExtras(b));
        super.onBackPressed();
    }


}

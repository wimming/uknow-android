package com.xuewen.xuewen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;

public class LoginInfoActivity extends AppCompatActivity {

    private static  final  String[] schools_Data = {"中山大学", "清华大学", "北京大学"};
    private static  final  String[] major_Data = {"软件工程", "计算机科学"};
    private static final String[] years_Data = {"入学年份", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015"};
    private static final  String[] education_Data = {"本科生", "硕士生", "博士生"};


//    @BindView(R.id.years)
//    Spinner years;
//
//    @BindView(R.id.years_text)
//    TextView years_text;
//
//    @BindView(R.id.education)
//    Spinner education;
//
//    @BindView(R.id.education_text)
//    TextView education_text;

    private ArrayAdapter<String> major_auto_complete_adapter;
    private ArrayAdapter<String> school_auto_complete_adaper;

    private ArrayAdapter<String>  years_adapter;
    private ArrayAdapter<String> education_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_info);

        Spinner years  = (Spinner) findViewById(R.id.years);
        Spinner education = (Spinner) findViewById(R.id.education);
        AutoCompleteTextView school_auto_complete = (AutoCompleteTextView) findViewById(R.id.school_auto_complete);
        AutoCompleteTextView major_auto_complete = (AutoCompleteTextView) findViewById(R.id.major_auto_complete);
        //autocomplemeet
        major_auto_complete_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, major_Data);
        school_auto_complete_adaper = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, schools_Data);
        school_auto_complete.setAdapter(school_auto_complete_adaper);
        major_auto_complete.setAdapter(major_auto_complete_adapter);


        //spin
        years_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, years_Data);
        education_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, education_Data);

        years_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        education_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        years.setAdapter(years_adapter);
        education.setAdapter(education_adapter);

    }



}

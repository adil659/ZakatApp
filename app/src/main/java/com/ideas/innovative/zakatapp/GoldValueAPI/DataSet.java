package com.ideas.innovative.zakatapp.GoldValueAPI;

import android.widget.ArrayAdapter;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by adil6 on 2018-10-28.
 */

@SuppressWarnings("serial")
public class DataSet implements Serializable{

    public int id;
    public String dataset_code;
    public String database_code;
    public String newest_available_date;
    public  String oldest_available_date;
    public String start_date;
    public String end_date;

    public ArrayList<String> column_names;
    public ArrayList<ArrayList<Object>> data;


}

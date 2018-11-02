package com.examp.three.adapter.expandable_adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;

import com.examp.three.R;
import com.examp.three.activity.Shared_Modules.Shared_Common_All_Tax.MakePayment;


public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context _context;
    public static List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    public static boolean isExpand;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    public ExpandableListAdapter(Context _context){
        this._context = _context;
    }
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final String childText = (String) getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
 
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.list_item);
 
        txtListChild.setText(childText);
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {

        Resources resources = _context.getResources();
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        if(isExpanded) {
            convertView.setBackgroundColor(resources.getColor(R.color.bg_gray));
            isExpand = isExpanded;
            System.out.println("isExpand????????????? = " + isExpand);
            System.out.println("isExpanded????????????? = " + isExpanded);
        }
        else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
            isExpand = isExpanded;
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.list_header);
        lblListHeader.setTypeface(MakePayment.TamilFont);
        lblListHeader.setText(headerTitle);
        lblListHeader.setTextColor(Color.DKGRAY);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void removeView(int groupPosition){
        _listDataHeader.remove(getGroup(groupPosition));

    }
}
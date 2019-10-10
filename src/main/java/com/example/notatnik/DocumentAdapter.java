package com.example.notatnik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DocumentAdapter extends BaseAdapter {

    private Context mContext;
    private List<Document> mDocumentList;
    private LayoutInflater mInflater;

    public DocumentAdapter(Context mContext, List<Document> myDocumentList) {
        this.mContext = mContext;
        this.mDocumentList = myDocumentList;
        mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.mDocumentList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mDocumentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.listview_detail_layout, null);
        TextView name = (TextView) v.findViewById(R.id.nameTextView);
        TextView type = (TextView) v.findViewById(R.id.typeTextView);
        TextView itemsCount = (TextView) v.findViewById(R.id.itemsCountTextView);

        name.setText(String.format("%s", mDocumentList.get(position).getName()));
        type.setText(String.format("%s", mDocumentList.get(position).getType()));
        itemsCount.setText(String.valueOf(mDocumentList.get(position).getItemsCount()));

        v.setTag(mDocumentList.get(position).getId());

        return v;
    }
}

package Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.blooddonationapplication.R;

import java.util.HashMap;
import java.util.List;

import Model.Request;

public class RequestAdupter extends ArrayAdapter {

    Activity contex;
    List requestes;
    public RequestAdupter(@NonNull Context context, List requests) {
        super(context, R.layout.showrequest,requests);
        this.contex = (Activity) context;
        this.requestes =  requests;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = contex.getLayoutInflater();
        @SuppressLint("ViewHolder") View listViewItem = inflater.inflate(R.layout.showrequest, null, true);
        TextView txtName = listViewItem.findViewById(R.id.textname);
        TextView txtAdd = listViewItem.findViewById(R.id.textaddress);
        TextView txtMno = listViewItem.findViewById(R.id.Phone);
        TextView blood=listViewItem.findViewById(R.id.textblood);

        Request request = (Request) requestes.get(position);
        txtName.setText(request.getName());
        txtAdd.setText(request.getCity());
        txtMno.setText(request.getPhone());
        blood.setText(request.getBlood());

        return listViewItem;
    }
}

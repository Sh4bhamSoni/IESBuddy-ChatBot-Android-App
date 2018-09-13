package com.ssgmail.shubhammsoni.iesbuddy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by shubham soni on 21-Jun-18.
 */

public class Location_recycler_adapter extends RecyclerView.Adapter<Location_recycler_adapter.ViewHolder> {


    private static final String TAG = "Location_recycler_adapt";

//   ArrayList<String> type = new ArrayList<>();
   ArrayList<Location_item_model> items= new ArrayList<>();
//   ArrayList<String> desc= new ArrayList<>();
   Context context;
   CustomAdapterClickListener clickListener;

    public Location_recycler_adapter(ArrayList<Location_item_model> items, Context context,final CustomAdapterClickListener customAdapterClickListener) {
        this.items = items;
        this.context = context;
        this.clickListener=customAdapterClickListener;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_location_item_layout,parent,false);
        ViewHolder holder =new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: called");

        holder.l_type_txt.setText(items.get(position).getType());
        holder.l_desc_txt.setText(items.get(position).getDesc());

        holder.location_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,items.get(position).getLatLng().toString(), Toast.LENGTH_SHORT).show();
                view.setTag(position);
                clickListener.onItemClick(view,(int)view.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
   {
       TextView l_type_txt,l_desc_txt;
       RelativeLayout location_item_layout;
       public ViewHolder(View itemView) {

           super(itemView);
           l_desc_txt=itemView.findViewById(R.id.loc_desc_txt);
           l_type_txt=itemView.findViewById(R.id.loc_type_txt);
           location_item_layout=itemView.findViewById(R.id.rv_item_layout);

       }
   }
}

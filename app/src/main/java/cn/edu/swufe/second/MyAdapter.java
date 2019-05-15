package cn.edu.swufe.second;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter extends ArrayAdapter{
    private static final String TAG ="MyAdapter";
    public MyAdapter(Context context,
                     int resource,
                     ArrayList<HashMap<String,String>> list){
        super(context,resource,list);
    }

   // @Override
    public View getView(int position,View convertView,View parent){
        View itemView=convertView;
        if(itemView==null){
            itemView= (View) LayoutInflater.from(getContext()).inflate(R.layout.list_item , (ViewGroup) parent, false);
        }
        Map<String,String>map=(Map<String,String>)getItem(position);
        TextView title =(TextView)itemView.findViewById(R.id.itemTitle);
        TextView detail =(TextView)itemView.findViewById(R.id.itemDetail);

        title.setText("Title:"+map.get("ItemTitle"));
        detail.setText("detail:"+map.get("ItemDetail"));

        return itemView;
    }

}


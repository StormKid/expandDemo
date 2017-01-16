package com.layout.ke_li.linearlayoutrefreshdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.layout.ke_li.linearlayoutrefreshdemo.liberary.ExpandableContainer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        ListView listView = (ListView) findViewById(R.id.show_contain);
        list = new ArrayList<>();
        for (int i=0;i<40;i++)
        list.add(getResources().getString(R.string.longstring));
        listView.setAdapter(new ViewAdapter());

        

    }



    private class ViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.value,null);
                holder = new ViewHolder(convertView);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(list.get(position),position);
            return convertView;
        }
    }

    public  class ViewHolder{
        ExpandableContainer textView;

        public ViewHolder(View view){
            view.setTag(this);
            textView = (ExpandableContainer) view.findViewById(R.id.value);
        }
    }



}

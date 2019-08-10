package com.example.projectv10;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class Adapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<User> userList;

        public Adapter(Activity activity, List<User> userList) {
            inflater=(LayoutInflater) activity.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            this.userList = userList;
        }

        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public Object getItem(int i) {
            return userList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View lineView;

            lineView=inflater.inflate(R.layout.dinamic_data,null);
            TextView username=lineView.findViewById(R.id.username);
            TextView countGps=lineView.findViewById(R.id.GpsCount);

            User user=userList.get(i);
            username.setText(user.getUsername());
            countGps.setText(String.valueOf(user.getCountOfGps()));

            return lineView;
        }
    }



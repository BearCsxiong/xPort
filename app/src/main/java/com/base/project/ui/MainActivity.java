package com.base.project.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.project.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    RecyclerView mRv;

    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list.add("asdfa");
        list.add("fasdf");
        list.add("adsf");
        list.add("aghdgfh");
        list.add("qwrqwr");
        list.add("twfgs");
        list.add("sbxvb");
        list.add("fgsfd");
        list.add("sgfdgsf");
        list.add("sfdgs");
        list.add("sdfg");
        mRv = findViewById(R.id.rv);

        mRv.setLayoutManager(new SelectItemLayoutManager(this));
        mRv.addItemDecoration(new SelectItemDecoration());
        mRv.setAdapter(new RecyclerView.Adapter() {

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_page, viewGroup, false);
                return new BaseViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                TextView textView = viewHolder.itemView.findViewById(R.id.tv_position);
                String str = list.get(i);
                textView.setText(str);
            }

            @Override
            public int getItemCount() {
                return list.size();
            }
        });
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}

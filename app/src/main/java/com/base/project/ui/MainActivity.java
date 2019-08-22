package com.base.project.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.base.project.R;
import com.base.project.databinding.ActivityMainBinding;
import com.base.project.ui.main.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import me.csxiong.library.base.XActivity;

public class MainActivity extends XActivity<ActivityMainBinding, MainViewModel> {

    RecyclerView mRv;

    private List<Integer> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.add(R.mipmap.icon_card);
        list.add(R.mipmap.icon_card_1);
        list.add(R.mipmap.icon_card_2);
        list.add(R.mipmap.icon_card_3);
        mRv = findViewById(R.id.rv);
//        mRv.setPadding(XDisplayUtil.getScreenWidth() / 2 - XDisplayUtil.dpToPxInt(98) / 2, 0, XDisplayUtil.getScreenWidth() / 2 - XDisplayUtil.dpToPxInt(98) / 2, 0);

//        SelectItemDecoration itemde = new SelectItemDecoration(mRv);
//        itemde.setOnPageListener(new SelectItemDecoration.OnPageListener() {
//            @Override
//            public void onPageAttach(int position) {
//                Log.e("onPage", "attach" + position);
//            }
//
//            @Override
//            public void onPageDetach(int position) {
//                Log.e("onPage", "detach" + position);
//            }
//        });
        mRv.setLayoutManager(new AdvertisingLayoutManager(mRv, 0.02f, 20));
//        mRv.addItemDecoration(itemde);
        mRv.setAdapter(new RecyclerView.Adapter() {

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_page, viewGroup, false);
                return new BaseViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                ImageView iv = viewHolder.itemView.findViewById(R.id.iv_content);
                Integer str = list.get(i);
                iv.setImageResource(str);
            }

            @Override
            public int getItemCount() {
                return list.size();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mBinding.setStudent("CloseingBackGroup");
        mBinding.setViewModel(mViewModel);
    }

    @Override
    public void initData() {

    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}

package com.base.project.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.databinding.ActivityMainBinding;
import com.base.project.ui.main.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import me.csxiong.library.base.XActivity;
import me.csxiong.library.utils.XDisplayUtil;

public class MainActivity extends XActivity<ActivityMainBinding, MainViewModel> {

    RecyclerView mRv;

    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mRv.setPadding(XDisplayUtil.getScreenWidth() / 2 - XDisplayUtil.dpToPxInt(98) / 2, 0, XDisplayUtil.getScreenWidth() / 2 - XDisplayUtil.dpToPxInt(98) / 2, 0);

        SelectItemDecoration itemde = new SelectItemDecoration(mRv);
        itemde.setOnPageListener(new SelectItemDecoration.OnPageListener() {
            @Override
            public void onPageAttach(int position) {
                Log.e("onPage", "attach" + position);
            }

            @Override
            public void onPageDetach(int position) {
                Log.e("onPage", "detach" + position);
            }
        });
        mRv.setLayoutManager(new SelectItemLayoutManager(this));
        mRv.addItemDecoration(itemde);
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mBinding.setStudent("CloseingBackGroup");
        mBinding.setViewModel(mViewModel);
        mViewModel.getShowLoadingEvent();
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

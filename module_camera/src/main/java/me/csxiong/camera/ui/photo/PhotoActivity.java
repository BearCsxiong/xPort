package me.csxiong.camera.ui.photo;

import com.alibaba.android.arouter.facade.annotation.Route;

import me.csxiong.camera.R;
import me.csxiong.camera.databinding.ActivityPhotoBinding;
import me.csxiong.library.base.BaseActivity;

@Route(path = "/camera/photo", name = "相片界面")
public class PhotoActivity extends BaseActivity<ActivityPhotoBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {

    }
}

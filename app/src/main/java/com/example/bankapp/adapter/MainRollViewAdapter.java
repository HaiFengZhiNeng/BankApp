package com.example.bankapp.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bankapp.R;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

/**
 * 首页 轮播图
 *@author Guanluocang
 *created at 2017/12/25 10:09
*/
public class MainRollViewAdapter extends StaticPagerAdapter {


    private int[] imgs = {
            R.mipmap.ic_main_banner,
            R.mipmap.ic_main_banner_two,
            R.mipmap.ic_main_banner,
            R.mipmap.ic_main_banner_two,
            R.mipmap.ic_main_banner,
    };

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setImageResource(imgs[position]);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }
}

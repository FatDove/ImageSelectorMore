package com.donkingliang.imageselector.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class IntruderViewPagerAdapter extends PagerAdapter {
 
    private Context mContext;
    private List<Integer> mDrawableResIdList;
 
    public IntruderViewPagerAdapter(Context context, List<Integer> resIdList) {
        super();
        mContext = context;
        mDrawableResIdList = resIdList;
    }
 
 
 
    @Override
    public int getCount() {
        if (mDrawableResIdList != null) {
            return mDrawableResIdList.size();
        }
        return 0;
    }
 
    @Override
    public int getItemPosition(Object object) {
        if (object != null && mDrawableResIdList != null) {
            Integer resId = (Integer)((ImageView)object).getTag();
            if (resId != null) {
                for (int i = 0; i < mDrawableResIdList.size(); i++) {
                    if (resId.equals(mDrawableResIdList.get(i))) {
                        return i;
                    }
                }
            }
        }
        return ViewPager.SCROLLBAR_POSITION_DEFAULT;
    }
 
    @Override
    public Object instantiateItem(View container, int position) {
        if (mDrawableResIdList != null && position < mDrawableResIdList.size()) {
            Integer resId = mDrawableResIdList.get(position);
            if (resId != null) {
                ImageView itemView = new ImageView(mContext);
//                itemView.setDrawableResource(resId);

                //此处假设所有的照片都不同，用resId唯一标识一个itemView；也可用其它Object来标识，只要保证唯一即可
                itemView.setTag(resId);
 
                ((ViewPager) container).addView(itemView);
                return itemView;
            }
        }
        return null;
    }
 
    @Override
    public void destroyItem(View container, int position, Object object) {
        //注意：此处position是ViewPager中所有要显示的页面的position，与Adapter mDrawableResIdList并不是一一对应的。
        //因为mDrawableResIdList有可能被修改删除某一个item，在调用notifyDataSetChanged()的时候，ViewPager中的页面
        //数量并没有改变，只有当ViewPager遍历完自己所有的页面，并将不存在的页面删除后，二者才能对应起来
        if (object != null) {
            ViewGroup viewPager = ((ViewGroup) container);
            int count = viewPager.getChildCount();
            for (int i = 0; i < count; i++) {
                View childView = viewPager.getChildAt(i);
                if (childView == object) {
                    viewPager.removeView(childView);
                    break;
                }
            }
        }
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
 
    @Override
    public Parcelable saveState() {
        return null;
    }
 
    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }
 
    @Override
    public void startUpdate(View container) {
    }
    @Override
    public void finishUpdate(View container) {
    }
 
    public void updateData(List<Integer> itemsResId) {
        if (itemsResId == null) {
            return;
        }
        mDrawableResIdList = itemsResId;
        this.notifyDataSetChanged();
    }
}

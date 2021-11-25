package com.xj.financeinspection.dialog;


import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.view.WheelView;
import com.xj.financeinspection.R;
import com.xj.financeinspection.base.BaseDialog;
import com.xj.financeinspection.util.ToastManager;

import java.util.List;

import butterknife.BindView;

public class SelectItemDialog<T> extends BaseDialog {

    public static <T> void showDialog(FragmentManager fragmentManager,
                                      List<T> data,
                                      OnItemSelectListener<T> onItemSelectListener) {
        if (data == null || data.size() == 0) {
            ToastManager.getInstance().toast("数据错误");
            return;
        }
        new SelectItemDialog<T>()
                .setOnItemSelectListener(onItemSelectListener)
                .setData(data)
                .show(fragmentManager, "select_item");
    }

    @BindView(R.id.select_wv)
    WheelView select_wv;
    @BindView(R.id.select_cancel)
    TextView select_cancel;
    @BindView(R.id.select_submit)
    TextView select_submit;

    private List<T> data;

    private OnItemSelectListener<T> onItemSelectListener;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_select_item;
    }

    @Override
    public void initView() {
        select_wv.setCyclic(false);
        select_wv.setAdapter(new SelectItemAdapter());
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        select_cancel.setOnClickListener(v -> dismissAllowingStateLoss());
        select_submit.setOnClickListener(v -> {
            if (onItemSelectListener != null) {
                onItemSelectListener.onItemSelect(data.get(select_wv.getCurrentItem()));
            }
            dismissAllowingStateLoss();
        });
    }

    public SelectItemDialog<T> setOnItemSelectListener(OnItemSelectListener<T> onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
        return this;
    }

    public SelectItemDialog<T> setData(List<T> data) {
        this.data = data;
        return this;
    }

    @Override
    protected boolean getCanceled() {
        return true;
    }

    @Override
    protected boolean isBottom() {
        return true;
    }

    @Override
    protected boolean isMatch() {
        return true;
    }

    public interface OnItemSelectListener<T> {
        void onItemSelect(T t);
    }

    protected class SelectItemAdapter implements WheelAdapter<T> {

        @Override
        public int getItemsCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public T getItem(int index) {
            return data.get(index);
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }
    }
}

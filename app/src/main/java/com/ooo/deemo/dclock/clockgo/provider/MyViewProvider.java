package com.ooo.deemo.dclock.clockgo.provider;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;



import com.ooo.deemo.dclock.R;
import com.ooo.deemo.dclock.clockgo.data.MyData;
import com.ooo.deemo.dclock.timeselector.interf.IViewProvider;

public class MyViewProvider implements IViewProvider<MyData> {
    @Override
    public int resLayout() {
        return R.layout.my_scroll_picker_item_layout;
    }

    @Override
    public void onBindView(@NonNull View view, @Nullable MyData itemData) {
        TextView tv = view.findViewById(R.id.tv_content);
        tv.setText(itemData == null ? null : itemData.num);
        view.setTag(itemData);
    }

    @Override
    public void updateView(@NonNull View itemView, boolean isSelected) {
        TextView tv = itemView.findViewById(R.id.tv_content);
        tv.setTextColor(Color.parseColor(isSelected ? "#589AAA" : "#342434"));
    }
}

package com.aisino.simpleexample.swipe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aisino.simpleexample.R;
import com.hq.tool.widget.view.swipe.SwipeLayout;

public class NestedExample extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complicate_layout);
         SwipeLayout swipeLayout = (SwipeLayout)findViewById(R.id.test_swipe_swipe);
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(getApplicationContext(), "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        swipeLayout.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

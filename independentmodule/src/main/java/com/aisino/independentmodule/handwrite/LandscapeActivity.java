package com.aisino.independentmodule.handwrite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.aisino.independentmodule.R;


//2017-9-26
public class LandscapeActivity extends Activity {
    private LinePathView pathView;
    private Button mClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_write);
        pathView = (LinePathView) findViewById(R.id.view);
        mClear = (Button) findViewById(R.id.clear1);
        Button mSave = (Button) findViewById(R.id.save1);
        setResult(50);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pathView.getTouched()) {
//                    ModuleManager.Companion.getMap().save(Companion.getMODULE_CALLBACK(), pathView.getBitMap());
                    Intent resultIntent = new Intent();
                    Bundle bundle=new Bundle();
                    bundle.putParcelable("result", pathView.getBitMap());
                    resultIntent.putExtras(bundle);
                    setResult(RESULT_OK,resultIntent);
                    finish();
                } else {
                    Toast.makeText(LandscapeActivity.this, "您没有签名~", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathView.clear();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pathView.setDrawingCacheEnabled(false);
    }
}

package com.example.imagelrucacherdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private  String [] imageThumbleUrl = Images.imageThumbUrls;    //数据源
    private  ImageAdapter adapter;
    private  FileUtils fileUtils;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fileUtils = new FileUtils(this);
        gridView = (GridView) findViewById(R.id.gridView);

        adapter = new ImageAdapter(this,imageThumbleUrl,gridView);
        gridView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        menu.add("删除手机中的图片缓存");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                fileUtils.delete();
                Toast.makeText(this, "图片缓存清除成功！！", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        adapter.cancelTask();
        super.onDestroy();
    }
}

package com.codepath.gridimagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.SearchSetting;

public class SettingActivity extends Activity {
    
    private EditText etSiteFilter;
    private Spinner spImageType;
    private Spinner spImageSize;
    private Spinner spColorFilter;
    private Button btnSave;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setupViews();
    }
    
    public void setupViews() {
        spImageType = (Spinner) findViewById(R.id.spImageType);
        spImageSize = (Spinner) findViewById(R.id.spImageSize);
        spColorFilter = (Spinner) findViewById(R.id.spColorFilter);
        
        ArrayAdapter<CharSequence> imageSizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.image_size_array, android.R.layout.simple_spinner_item);
        imageSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImageSize.setAdapter(imageSizeAdapter);
        
        ArrayAdapter<CharSequence> imageTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.image_type_array, android.R.layout.simple_spinner_item);
        imageTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImageType.setAdapter(imageTypeAdapter);
        
        ArrayAdapter<CharSequence> ColorFilterAdapter = ArrayAdapter.createFromResource(this,
                R.array.color_filter_array, android.R.layout.simple_spinner_item);
        imageTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spColorFilter.setAdapter(ColorFilterAdapter);

        etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
        etSiteFilter.setText("yahoo.com");

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOnClick();
            }
        });
    }
    
    public void saveOnClick() {
        String imageSize = spImageSize.getSelectedItem().toString().trim();
        String colorFilter = spColorFilter.getSelectedItem().toString().trim();
        String imageType = spImageType.getSelectedItem().toString().trim();
        String siteFilter = etSiteFilter.getText().toString().trim();
        
        SearchSetting setting = new SearchSetting();
        setting.colorFilter = colorFilter;
        setting.imageType = imageType;
        setting.imageSize = imageSize;
        setting.siteFilter = siteFilter;
               
        Intent i = new Intent(this, SearchActivity.class);
        i.putExtra("image_query", setting);
        startActivity(i);    
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.walmartlabs.agupta.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.walmartlabs.agupta.gridimagesearch.R;

public class FilterActivity extends AppCompatActivity {



    Spinner sizeSpinner;
    Spinner colorSpinner;
    Spinner typeSpinner;
    EditText etSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        sizeSpinner = (Spinner) findViewById(R.id.sizeSpinner);
        colorSpinner = (Spinner) findViewById(R.id.colorSpinner);
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSizes = ArrayAdapter.createFromResource(this,
                R.array.sizes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSizes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sizeSpinner.setAdapter(adapterSizes);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterColors = ArrayAdapter.createFromResource(this,
                R.array.colors, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterColors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        colorSpinner.setAdapter(adapterColors);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterTypes = ArrayAdapter.createFromResource(this,
                R.array.types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        typeSpinner.setAdapter(adapterTypes);
        etSite = (EditText) findViewById(R.id.etSiteFilter);
    }

    public void onSubmit(View v) {
        Intent data = new Intent();
        data.putExtra("size", sizeSpinner.getSelectedItem().toString());
        data.putExtra("type", colorSpinner.getSelectedItem().toString());
        data.putExtra("color", colorSpinner.getSelectedItem().toString());
        data.putExtra("site", etSite.getText().toString());
        setResult(RESULT_OK, data);
        this.finish();
    }

    public void onSaveSettings(View view) {
        onSubmit(view);
    }
}

package com.examp.three.activity.QuickPay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.examp.three.R;
import com.examp.three.activity.paymentgateway.utility.Params;

public class QuickPayActivity extends AppCompatActivity {

    LinearLayout linProperty, linProfession, linWater, linMiscellaneous;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quickypay);

        linProperty = (LinearLayout) findViewById(R.id.button_property);
        linProfession = (LinearLayout) findViewById(R.id.button_profession);
        linWater = (LinearLayout) findViewById(R.id.button_water);
        linMiscellaneous = (LinearLayout) findViewById(R.id.button_nonTax);

        initToolbar();
        buttonTax();

    }

    //This method is for initializing the toolbar
    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quick Pay");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void buttonTax() {

        linProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuickPayActivity.this, PaymentInstantActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Params.taxType, Params.property_tax);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        linProfession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuickPayActivity.this, PaymentInstantActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Params.taxType, Params.profession_tax);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        linWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuickPayActivity.this, PaymentInstantActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Params.taxType, Params.water_tax);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        linMiscellaneous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(QuickPayActivity.this, "Non tax Under Construction", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

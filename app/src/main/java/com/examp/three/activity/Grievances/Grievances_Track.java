package com.examp.three.activity.Grievances;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.examp.three.R;
import com.examp.three.activity.Grievances.Helpher.GrievancesTrackHelpher;
import com.examp.three.common.DateSelect;

public class Grievances_Track extends AppCompatActivity {

    @Nullable
    @BindView(R.id.grievance_track_toolbar)
    Toolbar grievance_track_toolbar;

    @Nullable
    @BindView(R.id.et_grvno)
    EditText et_grvno;
    @Nullable
    @BindView(R.id.li_parent_lay)
    LinearLayout li_parent_lay;

    @Nullable
    @BindView(R.id.et_fromdate)
    EditText et_fromdate;

    @Nullable
    @BindView(R.id.et_todate)
    EditText et_todate;

    @Nullable
    @BindView(R.id.btn_submit)
    Button btn_submit;

    //Object declarations
    DateSelect dateSelect;
    GrievancesTrackHelpher grievancesTrackHelpher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievances_registration);
        ButterKnife.bind(this);
        setSupportActionBar(grievance_track_toolbar);
        grievance_track_toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_grievances_Track);

        //Object creations
        dateSelect = new DateSelect(Grievances_Track.this);
        grievancesTrackHelpher = GrievancesTrackHelpher.getInstance(Grievances_Track.this, li_parent_lay);
        et_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSelect.getDate(et_fromdate);
            }
        });
        et_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSelect.getDate(et_todate);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (et_grvno.getText().toString().isEmpty()) {
                    Snackbar.make(li_parent_lay, "Please Enter Grievances Number", Snackbar.LENGTH_SHORT).show();

                    return;

                }
                grievancesTrackHelpher.getGrievancesByDate(et_fromdate.getText().toString(), et_todate.getText().toString(), et_grvno.getText().toString());

              /*  if((!et_fromdate.getText().toString().isEmpty() &&
                        !et_fromdate.getText().toString().equalsIgnoreCase(" ")) &&
                        !et_todate.getText().toString().isEmpty() &&
                        !et_todate.getText().toString().equalsIgnoreCase(" ")){

                    if(grievancesTrackHelpher.checkDate(et_fromdate.getText().toString(),et_todate.getText().toString())){
                        grievancesTrackHelpher.getGrievancesByDate(et_fromdate.getText().toString(),et_todate.getText().toString(),et_grvno.getText().toString());
                    }
                } else{

                }*/

            }
        });

    }
}

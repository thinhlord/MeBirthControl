package com.meboo.birthcontrol;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ngocbeo1121 on 3/10/16.
 * Project: MeBirthControl
 */
public class SetReminderActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.medic_name)
    EditText medicNameEditText;

    @Bind(R.id.spinner_pill_type)
    Spinner pillTypeSpinner;

    @Bind(R.id.timePicker)
    TimePicker timePicker;

    @Bind(R.id.date)
    TextView dateTextView;

    @Bind(R.id.set_button)
    Button setButton;

    @Bind(R.id.placebo_checkBox)
    CheckBox placeboCheckbox;

    @Bind(R.id.card_schedule)
    View setDateButton;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    SharedPreferences preferences;
    BirthControlPill pill = new BirthControlPill();

    @Override
    @SuppressWarnings("Deprecated")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final Calendar c = Calendar.getInstance();

        String pillJsonString = preferences.getString(BirthControlPill.TAG, "");
        if (!pillJsonString.isEmpty()) {
            pill = new Gson().fromJson(pillJsonString, BirthControlPill.class);
            medicNameEditText.setText(pill.name);
            placeboCheckbox.setChecked(pill.shouldTakePlaceboPillsOnBreakDays);
            c.setTime(new Date(pill.startDate));
            dateTextView.setText(dateFormat.format(new Date(pill.startDate)));

            timePicker.setCurrentHour(pill.remindTimeInDay / 3600000);
            timePicker.setCurrentMinute((pill.remindTimeInDay / 60000) % 60);
        } else {
            dateTextView.setText(dateFormat.format(new Date()));

            timePicker.setCurrentHour(19);
            timePicker.setCurrentMinute(55);
        }

        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(SetReminderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear + 1, dayOfMonth);
                        dateTextView.setText(dateFormat.format(c.getTime()));
                    }
                },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                pickerDialog.show();
            }
        });
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int remindTime = timePicker.getCurrentHour() * 3600 + timePicker.getCurrentMinute() * 60;
                int pillDay, breakDay;
                switch (pillTypeSpinner.getSelectedItemPosition()) {
                    case 0:
                        pillDay = 21;
                        breakDay = 7;
                        break;
                    case 1:
                        pillDay = 27;
                        breakDay = 4;
                        break;
                    default:
                        pillDay = 28;
                        breakDay = 0;
                        break;
                }

                boolean placebo = placeboCheckbox.isChecked();

                pill.init(medicNameEditText.getText().toString(),
                        remindTime, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH),
                        c.get(Calendar.YEAR), pillDay, breakDay, placebo);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(BirthControlPill.TAG, new Gson().toJson(pill));
                editor.apply();

                AlarmHelper.scheduleNotification(getApplicationContext(), pill.getNextRemindTime());
                SetReminderActivity.this.finish();
            }
        });
    }
}

package com.mirko.sample;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import mirko.android.datetimepicker.date.DatePickerDialog;
import mirko.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import mirko.android.datetimepicker.time.RadialPickerLayout;
import mirko.android.datetimepicker.time.TimePickerDialog;
import mirko.android.datetimepicker.time.TimePickerDialog.OnTimeSetListener;
import com.inscription.CreditsDialog;

public class MainActivity extends Activity {

	private TextView tvDisplayTime;
	private TextView tvDisplayDate;
	private Switch m24;

	private final Calendar mCalendar = Calendar.getInstance();

	private int hourOfDay = mCalendar.get(Calendar.HOUR_OF_DAY);

	private int minute = mCalendar.get(Calendar.MINUTE);

	private int day = mCalendar.get(Calendar.DAY_OF_MONTH);

	private int month = mCalendar.get(Calendar.MONTH);

	private int year = mCalendar.get(Calendar.YEAR);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvDisplayTime = (TextView) findViewById(R.id.tvTime);
		tvDisplayDate = (TextView) findViewById(R.id.tvDate);

		resetDate();
		resetTime();

		tvDisplayDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// nothing to do, just to let onTouchListener work 

			}
		});

		tvDisplayDate.setOnTouchListener(new SwipeDismissTouchListener(tvDisplayDate,
				null,
				new SwipeDismissTouchListener.DismissCallbacks() {
			@Override
			public boolean canDismiss(Object token) {
				return true;
			}

			@Override
			public void onDismiss(View view, Object token) {
				resetDate();
			}
		}));

		tvDisplayTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// nothing to do, just to let onTouchListener work 

			}
		});

		tvDisplayTime.setOnTouchListener(new SwipeDismissTouchListener(tvDisplayTime,
				null,
				new SwipeDismissTouchListener.DismissCallbacks() {
			@Override
			public boolean canDismiss(Object token) {
				return true;
			}

			@Override
			public void onDismiss(View view, Object token) {
				resetTime();
			}
		}));

		m24 = (Switch) findViewById(R.id.switch1);

		m24.setChecked(true);

		m24.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean is24hmode) {

				if(m24.isChecked()){
					buttonView.setChecked(true);
					is24hmode=true;
				}
				else{
					is24hmode=false;

				}

			}

		});

		final TimePickerDialog timePickerDialog12h = TimePickerDialog.newInstance(new OnTimeSetListener() {

			@Override
			public void onTimeSet(RadialPickerLayout view, int hourOfDay,
					int minute) {

				Object c = pad3(hourOfDay);

				tvDisplayTime.setText(
						new StringBuilder().append(pad2(hourOfDay))
						.append(":").append(pad(minute)).append(c));
				tvDisplayTime.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
			}
		}, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false);

		final TimePickerDialog timePickerDialog24h = TimePickerDialog.newInstance(new OnTimeSetListener() {

			@Override
			public void onTimeSet(RadialPickerLayout view, int hourOfDay,
					int minute) {

				tvDisplayTime.setText(
						new StringBuilder().append(pad(hourOfDay))
						.append(":").append(pad(minute)));

				tvDisplayTime.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
			}
		}, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);

		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new OnDateSetListener() {

			public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

				tvDisplayDate.setText(
						new StringBuilder().append(pad(day))
						.append(" ").append(pad(month+1)).append(" ").append(pad(year)));
				tvDisplayDate.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
			}

		}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

		findViewById(R.id.btnChangeDate).setOnClickListener(new OnClickListener() {

			private String tag;

			@Override
			public void onClick(View v) {
				datePickerDialog.show(getFragmentManager(), tag);;
			}
		});

		findViewById(R.id.btnChangeTime).setOnClickListener(new OnClickListener() {

			private String tag;

			@Override
			public void onClick(View v) {

				if(m24.isChecked()){
					timePickerDialog24h.show(getFragmentManager(), tag);

				}
				else {
					timePickerDialog12h.show(getFragmentManager(), tag);

				}
			}});
	}

	private void resetTime() {
		tvDisplayTime.setText(new StringBuilder().append(pad(hourOfDay))
				.append(":").append(pad(minute)));
		tvDisplayTime.setTextColor(getResources().getColor(android.R.color.darker_gray));

	}

	private void resetDate() {
		tvDisplayDate.setText(new StringBuilder().append(pad(day))
				.append(" ").append(pad(month+1)).append(" ").append(pad(year)));
		tvDisplayDate.setTextColor(getResources().getColor(android.R.color.darker_gray));

	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private static String pad2(int c) {
		if (c == 12)
			return String.valueOf(c) ;
		if (c == 00)
			return String.valueOf(c+12) ;
		if (c > 12)
			return String.valueOf(c-12) ;
		else
			return String.valueOf(c);
	}

	private static String pad3(int c) {
		if (c == 12)
			return " PM";
		if (c == 00)
			return " AM";
		if (c > 12)
			return " PM";
		else
			return " AM";
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		boolean previouslyStarted = prefs.getBoolean(getString(R.string.app_name), false);
		if(!previouslyStarted){
			SharedPreferences.Editor edit = prefs.edit();
			edit.putBoolean(getString(R.string.app_name), Boolean.TRUE);
			edit.commit();
			LaunchHint();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){

		case R.id.action_hint:
			LaunchHint();
			break;
		case R.id.action_about:
			LaunchAbout();

		}
		return super.onOptionsItemSelected(item);
	}

	private void LaunchAbout() {
		// TODO Auto-generated method stub
		final CreditsDialog cd = new CreditsDialog(this);
		cd.show();
	}

	private void LaunchHint() {
		Intent launchNewIntent = new Intent(MainActivity.this, Hint.class);
		startActivityForResult(launchNewIntent, 0);

	}

}
package com.example.hs20150126.day06_work_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

	GridView monthView;
	CalendarMonthAdapter monthViewAdapter;

	TextView monthText;

	int curYear;
	int curMonth;

	int curPosition;
	EditText scheduleInput;
	Button saveButton;

	ListView scheduleList;
	ScheduleListAdapter scheduleAdapter;
	ArrayList<ScheduleListItem> outScheduleList;

	public static final int REQUEST_CODE_SCHEDULE_INPUT = 1001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		monthView = (GridView) findViewById(R.id.monthView);
		monthViewAdapter = new CalendarMonthAdapter(this);
		monthView.setAdapter(monthViewAdapter);

		// set listener
		monthView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MonthItem curItem = (MonthItem) monthViewAdapter.getItem(position);
				int day = curItem.getDay();

				Toast.makeText(getApplicationContext(), day + "일이 선택되었습니다.", Toast.LENGTH_LONG).show();

				showScheduleInput();

				monthViewAdapter.setSelectedPosition(position);
				monthViewAdapter.notifyDataSetChanged();

				// set schedule to the TextView
				curPosition = position;

				outScheduleList = monthViewAdapter.getSchedule(position);
				if (outScheduleList == null) {
					outScheduleList = new ArrayList<ScheduleListItem>();
				}
				scheduleAdapter.scheduleList = outScheduleList;

				scheduleAdapter.notifyDataSetChanged();
			}
		});

		monthText = (TextView) findViewById(R.id.monthText);
		setMonthText();

		Button monthPrevious = (Button) findViewById(R.id.monthPrevious);
		monthPrevious.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				monthViewAdapter.setPreviousMonth();
				monthViewAdapter.notifyDataSetChanged();

				setMonthText();
			}
		});

		Button monthNext = (Button) findViewById(R.id.monthNext);
		monthNext.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				monthViewAdapter.setNextMonth();
				monthViewAdapter.notifyDataSetChanged();

				setMonthText();
			}
		});

		curPosition = -1;

		scheduleList = (ListView)findViewById(R.id.scheduleList);
		scheduleAdapter = new ScheduleListAdapter(this);
		scheduleList.setAdapter(scheduleAdapter);

	}


	private void setMonthText() {
		curYear = monthViewAdapter.getCurYear();
		curMonth = monthViewAdapter.getCurMonth();

		monthText.setText(curYear + "년 " + (curMonth+1) + "월");
	}

	private void showScheduleInput() {
		Intent intent = new Intent(this, ScheduleInputActivity.class);
		startActivityForResult(intent, REQUEST_CODE_SCHEDULE_INPUT);
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (intent != null) {
			if (requestCode == REQUEST_CODE_SCHEDULE_INPUT) {
				String time = intent.getStringExtra("time");
				String message = intent.getStringExtra("message");

				if (message != null) {
					Toast toast = Toast.makeText(getBaseContext(), "result code : " + resultCode + ", time : " + time + ", message : " + message, Toast.LENGTH_LONG);
					toast.show();

					ScheduleListItem aItem = new ScheduleListItem(time, message);


					if (outScheduleList == null) {
						outScheduleList = new ArrayList<ScheduleListItem>();
					}
					outScheduleList.add(aItem);

					monthViewAdapter.putSchedule(curPosition, outScheduleList);

					scheduleAdapter.scheduleList = outScheduleList;
					scheduleAdapter.notifyDataSetChanged();
				}
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}

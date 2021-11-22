package com.example.qresent.calendar;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qresent.R;
import com.example.qresent.databinding.FragmentCalendarBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {

    private FragmentCalendarBinding binding;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private Button previousMonthButton;
    private Button nextMonthButton;
    private DatabaseReference databaseReference;
    private ArrayList<String> courses = new ArrayList<>();
    private ArrayList<Date> timings = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_calendar,
                container,
                false
        );

        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();

        previousMonthButton.setOnClickListener(this::previousMonthAction);
        nextMonthButton.setOnClickListener(this::nextMonthAction);

        databaseReference = FirebaseDatabase.getInstance("https://qresent-926c3-default-rtdb.europe-west1.firebasedatabase.app/").
                getReference("Calendar");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        courses = new ArrayList<>();
        timings = new ArrayList<>();
        databaseReference.get().addOnSuccessListener(dataSnapshot -> {
            Iterator iterator = dataSnapshot.getChildren().iterator();

            while (iterator.hasNext()) {
                DataSnapshot nextSnapshot = (DataSnapshot) iterator.next();
                try {
                    courses.add(nextSnapshot.getKey());
                    timings.add(sdf.parse((String) nextSnapshot.getValue()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        return binding.getRoot();
    }

    private void initWidgets() {
        calendarRecyclerView = binding.calendarRecyclerView;
        monthYearText = binding.monthYearTV;
        previousMonthButton = binding.previousMonthButton;
        nextMonthButton = binding.nextMonthButton;
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(View v, int position, String dayText) {
        Date fullDate = new Date();
        fullDate.setYear(selectedDate.getYear());
        fullDate.setMonth(selectedDate.getMonthValue() + 1);

        fullDate.setDate(Integer.parseInt(dayText));
        Calendar c = Calendar.getInstance();
        c.setTime(fullDate); // yourdate is an object of type Date

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if (selectedDate.getMonthValue() == 12) {
            dayOfWeek--;
        }
        Integer count = 0;
        ArrayList<String> bufferCourses = new ArrayList<>();
        ArrayList<Integer> bufferTimings = new ArrayList<>();
        for (Date tempTime : timings) {
            if (tempTime.getDay() == dayOfWeek) {
                bufferCourses.add(courses.get(count));
                bufferTimings.add(tempTime.getHours());
            }
            count++;
        }
        if (bufferCourses.isEmpty()) {
            bufferCourses.add("Liber");
            bufferTimings.add(0);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("courses", bufferCourses); // Serializable Object
        bundle.putSerializable("timings", bufferTimings);
        Navigation.findNavController(v).navigate(R.id.action_calendarFragment_to_scheduleCoursesFragment, bundle);

    }

}
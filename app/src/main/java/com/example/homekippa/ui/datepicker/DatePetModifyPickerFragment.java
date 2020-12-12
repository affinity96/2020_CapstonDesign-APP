package com.example.homekippa.ui.datepicker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.homekippa.AddPetDesActivity;
import com.example.homekippa.ModifyPetBirthActivity;

import java.util.Calendar;

public class DatePetModifyPickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        ModifyPetBirthActivity activity = (ModifyPetBirthActivity) getActivity();
        activity.processDatePickerResult(year, month, day);
    }
}

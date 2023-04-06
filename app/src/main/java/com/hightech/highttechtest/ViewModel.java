package com.hightech.highttechtest;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ViewModel extends BaseObservable {
    Model model;

    @Bindable
    public String getCountryName() {
        return model.getCountryName();
    }

    @Bindable
    public String getCurrencyValue() {
        return model.getCurrencyValue();
    }
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.w("tag", "onTextChanged " + s);
    }
    private int selectedItemPosition;

    @Bindable
    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
        notifyPropertyChanged(BR.selectedItemPosition);
    }

}

package com.foodeze.rider.Utils.RelateToFragment_OnBack;


import androidx.fragment.app.Fragment;

/**
 * Created by AQEEL on 3/30/2018.
 */

public class RootFragment extends Fragment implements OnBackPressListener {

    @Override
    public boolean onBackPressed() {

        return new BackPressImplimentation(this).onBackPressed();
    }
}
package com.triocodes.medivision.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triocodes.medivision.Interface.VolleyCallback;
import com.triocodes.medivision.ParentFragment;
import com.triocodes.medivision.R;

import java.util.HashMap;

/**
 * Created by admin on 29-04-16.
 */
public class TargetFragment extends ParentFragment implements VolleyCallback {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.target_fragment_layout,null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void volleyOnSuccess() {

    }

    @Override
    public void volleyOnError() {

    }

    @Override
    public HashMap<String, Object> getExtras() {
        return null;
    }
}

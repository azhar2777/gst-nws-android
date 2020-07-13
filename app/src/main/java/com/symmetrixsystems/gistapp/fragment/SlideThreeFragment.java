package com.symmetrixsystems.gistapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.symmetrixsystems.gistapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SlideThreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlideThreeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_slide_three, container, false);



        return v;
    }

    public static SlideThreeFragment newInstance(String text) {

        SlideThreeFragment f = new SlideThreeFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}

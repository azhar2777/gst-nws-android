package com.symmetrixsystems.gistapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.symmetrixsystems.gistapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SlideFourFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlideFourFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_slide_four, container, false);



        return v;
    }

    public static SlideFourFragment newInstance(String text) {

        SlideFourFragment f = new SlideFourFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}

package com.symmetrixsystems.gistapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.symmetrixsystems.gistapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SlideTwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlideTwoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_slide_two, container, false);



        return v;
    }

    public static SlideTwoFragment newInstance(String text) {

        SlideTwoFragment f = new SlideTwoFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}

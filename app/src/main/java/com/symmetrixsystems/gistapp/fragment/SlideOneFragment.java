package com.symmetrixsystems.gistapp.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.symmetrixsystems.gistapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SlideOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlideOneFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Context mContext;

    private ImageView ivDot1, ivDot2, ivDot3, ivDot4, ivDot5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_slide_one, container, false);
        mContext = getContext();

        initView();



        return rootView;
    }

    public static SlideOneFragment newInstance(String text) {

        SlideOneFragment f = new SlideOneFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }


    private void initView(){
        ivDot1 = rootView.findViewById(R.id.iv_dot1);
        ivDot1.setOnClickListener(this);
        ivDot2 = rootView.findViewById(R.id.iv_dot2);
        ivDot2.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_dot1:


                break;
            case R.id.iv_dot2:
                //Toast.makeText(mContext, "Dot 2 ", Toast.LENGTH_SHORT).show();

                /*FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.add(((ViewGroup)getView().getParent()).getId(), SlideTwoFragment.newInstance("SecondFragment, Instance 2"));
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();*/


                break;
            default:
                break;
        }
    }


}

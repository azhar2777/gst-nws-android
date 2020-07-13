package com.symmetrixsystems.gistapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.symmetrixsystems.gistapp.R;
import com.symmetrixsystems.gistapp.activity.RegisterActivity;
import com.symmetrixsystems.gistapp.model.RegisterUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SlideFiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlideFiveFragment extends Fragment {
    private View rootView;
    private Context mContext;
    private TextView tvGistNotapp, tvGetStarted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_slide_five, container, false);


        mContext    =   getContext();
        initView();
        return rootView;
    }

    public static SlideFiveFragment newInstance(String text) {

        SlideFiveFragment f = new SlideFiveFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    private void initView(){

        tvGistNotapp    =   rootView.findViewById(R.id.tv_gis_notapp);
        String text = "<font color=#213261> GIST IS</font> <font color=#D4312C>NOT</font><font color=#213261><br/>AN APP</font>";
        tvGistNotapp.setText(Html.fromHtml(text));

        tvGetStarted = rootView.findViewById(R.id.tv_get_started);
        tvGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);

            }
        });
    }
}

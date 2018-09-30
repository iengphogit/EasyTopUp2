package ocr.avaboy.com.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ocr.avaboy.com.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends BaseFragment {

    private static BaseFragment fragment;
    public static BaseFragment newInstance() {
        Bundle args = new Bundle();
        if(fragment == null){
            fragment = new ServiceFragment();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_, container, false);
    }

}

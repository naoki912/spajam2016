package wcdi.spajam2016;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ModeSelectingFragment extends Fragment {
    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";
    public static final String ARG_PARAM3 = "param3";
    public Integer groupId;
    public Integer password;
    public Integer userId;

    private OnFragmentInteractionListener mListener;

    public ModeSelectingFragment() {
    }

    public static ModeSelectingFragment newInstance(
            Integer groupId,
            Integer password,
            Integer userId
    ) {
        ModeSelectingFragment fragment = new ModeSelectingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, groupId);
        args.putInt(ARG_PARAM2, password);
        args.putInt(ARG_PARAM3, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupId = getArguments().getInt(ARG_PARAM1);
            password = getArguments().getInt(ARG_PARAM2);
            userId = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_mode_selecting, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

package wcdi.spajam2016;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.IOException;


public class QuestionInputFragment extends Fragment {

    private static final String EXTRA_INT__STATE_GROUP_ID = "state_group_id";
    private static final String EXTRA_INT__USER_ID = "user_id";

    private OnFragmentInteractionListener mListener;

    private int state_group_id;
    private int user_id;

    public QuestionInputFragment() {
    }

    public static QuestionInputFragment newInstance(int state_group_id, int user_id) {
        QuestionInputFragment fragment = new QuestionInputFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_INT__STATE_GROUP_ID, state_group_id);
        bundle.putInt(EXTRA_INT__USER_ID, user_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            this.state_group_id = bundle.getInt(EXTRA_INT__STATE_GROUP_ID);
            this.user_id = bundle.getInt(EXTRA_INT__USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question_input, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText editText = (EditText) view.findViewById(R.id.fragment_question_input__text_edit_view);

        view.findViewById(R.id.fragment_question_input__decision_button).setOnClickListener((View v) -> {
            if (editText.getText().toString().isEmpty()) {
                return;
            }

            final String question_text = editText.getText().toString();

            getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<String>() {
                @Override
                public Loader<String> onCreateLoader(int id, Bundle args) {
                    return new AsyncTaskLoader<String>(getContext()) {
                        @Override
                        public String loadInBackground() {
                            try {
                                return utils.okPostURL(
                                    "http://spajam.hnron.net:8080/question/" +
                                        String.valueOf(state_group_id) + '/' +
                                        String.valueOf(user_id),
                                    question_text
                                );
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    };
                }

                @Override
                public void onLoadFinished(Loader<String> loader, String data) {
                    getLoaderManager().destroyLoader(0);

/*                    if (data == null) {
                        return;
                    }
                    try {
                        JSONObject object = new JSONObject(data);

                        onEventListener.onJoin(
                                object.getInt("group_id"),
                                password,
                                object.getInt("user_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                }

                @Override
                public void onLoaderReset(Loader<String> loader) {
                }
            }).forceLoad();
        });
    }

/*    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventListener) {
            mListener = (OnEventListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEventListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

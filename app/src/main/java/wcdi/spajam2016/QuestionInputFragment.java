package wcdi.spajam2016;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.IOException;


public class QuestionInputFragment extends Fragment {
    private static final String EXTRA_INT__GROUP_ID = "group_id";
    private static final String EXTRA_INT__PASSWORD = "password";
    private static final String EXTRA_INT__STATE_GROUP_ID = "state_group_id";
    private static final String EXTRA_INT__USER_ID = "user_id";

    private int group_id;
    private int password;
    private int state_group_id;
    private int user_id;

    private OnEventListener mListener;

    public QuestionInputFragment() {
    }

    public static QuestionInputFragment newInstance(Integer groupId, Integer password, Integer userId, Integer stateGroupId) {
        QuestionInputFragment fragment = new QuestionInputFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_INT__GROUP_ID, groupId);
        args.putInt(EXTRA_INT__PASSWORD, password);
        args.putInt(EXTRA_INT__USER_ID, userId);
        args.putInt(EXTRA_INT__STATE_GROUP_ID, stateGroupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            this.group_id = bundle.getInt(EXTRA_INT__GROUP_ID);
            this.password = bundle.getInt(EXTRA_INT__PASSWORD);
            this.user_id = bundle.getInt(EXTRA_INT__USER_ID);
            this.state_group_id = bundle.getInt(EXTRA_INT__STATE_GROUP_ID);
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

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Sending...")
                .setCancelable(false)
                .create();

        new Thread(() -> {
            while (mListener != null) {
                try {
                    String data = utils.getURL(
                            "http://spajam.hnron.net:8080/state_group/input/" +
                                    String.valueOf(group_id) + "/" +
                                    String.valueOf(state_group_id)
                    );

                    JSONObject object = new JSONObject(data);

                    if (object.getInt("boolean") == 1) {
                        alertDialog.cancel();
                        mListener.onQuestionResultChanged(group_id, password, user_id, state_group_id);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        final EditText editText = (EditText) view.findViewById(R.id.fragment_question_input__text_edit_view);

        view.findViewById(R.id.fragment_question_input__decision_button).setOnClickListener((View v) -> {
            if (editText.getText().toString().isEmpty()) {
                return;
            }

            final String question_text = editText.getText().toString();

            alertDialog.setMessage(question_text);
            alertDialog.show();

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
                                        "question_text=" + question_text
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventListener) {
            mListener = (OnEventListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnEventListener {
        void onQuestionResultChanged(Integer groupId, Integer password, Integer userId, Integer stateGroupId);
    }
}

package wcdi.spajam2016;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class QuestionsFragment extends Fragment {
    private static final String EXTRA_INT__GROUP_ID = "group_id";
    private static final String EXTRA_INT__PASSWORD = "password";
    private static final String EXTRA_INT__USER_ID = "user_id";
    private static final String EXTRA_INT__STATE_GROUP_ID = "state_group_id";

    private int group_id;
    private int password;
    private int userId;
    private int state_group_id;

    private OnEventListener mListener;

    public QuestionsFragment() {
    }

    public static QuestionsFragment newInstance(Integer groupId, Integer password, Integer userId, Integer state_group_id) {
        QuestionsFragment fragment = new QuestionsFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_INT__GROUP_ID, groupId);
        args.putInt(EXTRA_INT__PASSWORD, password);
        args.putInt(EXTRA_INT__STATE_GROUP_ID, userId);
        args.putInt(EXTRA_INT__STATE_GROUP_ID, state_group_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.group_id = getArguments().getInt(EXTRA_INT__GROUP_ID);
            this.password = getArguments().getInt(EXTRA_INT__PASSWORD);
            this.userId = getArguments().getInt(EXTRA_INT__USER_ID);
            this.state_group_id = getArguments().getInt(EXTRA_INT__STATE_GROUP_ID);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), R.layout.fragment_item_question, R.id.fragment_item_question__text_view
        );

        ListView listView = (ListView) view.findViewById(R.id.fragment_question_list__text_list);
        listView.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<String>() {
            @Override
            public Loader<String> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<String>(getContext()) {
                    @Override
                    public String loadInBackground() {
                        try {
                            return utils.getURL("http://spajam.hnron.net:8080/question/"
                                    + String.valueOf(state_group_id)
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

                if (data == null) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(data);

                    if (object.getJSONArray("question_texts") == null) {
                        return;
                    }

                    JSONArray jsonArray = object.getJSONArray("question_texts");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        adapter.add(jsonArray.getString(i));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onLoaderReset(Loader<String> loader) {
            }
        }).forceLoad();

        view.findViewById(R.id.fragment_question_list__back_mode_selecting_button)
                .setOnClickListener((View v) -> {
                    getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<String>() {
                        @Override
                        public Loader<String> onCreateLoader(int id, Bundle args) {
                            return new AsyncTaskLoader<String>(getContext()) {
                                @Override
                                public String loadInBackground() {
                                    try {
                                        return utils.okPostURL("http://spajam.hnron.net:8080/create_none/"
                                                + String.valueOf(group_id), "a");
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
                            mListener.onModeSelectingChanged(group_id, password, userId);
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
        void onModeSelectingChanged(Integer groupId, Integer password, Integer userId);
    }
}

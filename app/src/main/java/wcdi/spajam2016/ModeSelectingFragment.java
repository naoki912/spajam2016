package wcdi.spajam2016;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ModeSelectingFragment extends Fragment {

    public interface OnEventListener {
        void onQuestionModeSelected(Integer groupId, Integer password, Integer userId, Integer sessionId);

        void onComingOutModeSelected(Integer groupId, Integer password, Integer userId, Integer sessionId);
    }

    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";
    public static final String ARG_PARAM3 = "param3";
    public Integer groupId;
    public Integer password;
    public Integer userId;
    public OnEventListener listener;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView groupIdView = (TextView) view.findViewById(R.id.group_id);
        groupIdView.setText(password.toString());

        final TextView numberOfMembersView = (TextView) view.findViewById(R.id.number_of_member);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (numberOfMembersView != null) {
                    try {
                        String data = utils.getURL(
                            "http://spajam.hnron.net:8080/group/users/" + groupId.toString()
                        );

                        if (data == null) {
                            continue;
                        }

                        new Handler(Looper.getMainLooper()).post(() -> {
                            try {
                                numberOfMembersView.setText(String.valueOf(
                                    new JSONObject(data).getInt("number_of_people")
                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        view.findViewById(R.id.fragment_mode_selecting__select_question_button).setOnClickListener((View v) -> {
            getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<String>() {
                @Override
                public Loader<String> onCreateLoader(int id, Bundle args) {
                    return new AsyncTaskLoader<String>(getContext()) {
                        @Override
                        public String loadInBackground() {
                            try {
                                return utils.postURL(
                                    "http://spajam.hnron.net:8080/create_question/" + groupId.toString()
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

                        listener.onQuestionModeSelected(
                            groupId,
                            password,
                            userId,
                            object.getInt("question_group_id")
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onLoaderReset(Loader<String> loader) {
                }
            }).forceLoad();
        });

        view.findViewById(R.id.fragment_mode_selecting__select_coming_out_button).setOnClickListener((View v) -> {
            getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<String>() {
                @Override
                public Loader<String> onCreateLoader(int id, Bundle args) {
                    return new AsyncTaskLoader<String>(getContext()) {
                        @Override
                        public String loadInBackground() {
                            try {
                                return utils.postURL(
                                    "http://spajam.hnron.net:8080/create_coming_out/" + groupId.toString()
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

                        listener.onComingOutModeSelected(
                            groupId,
                            password,
                            userId,
                            object.getInt("coming_out_group_id")
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
            listener = (OnEventListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }
}

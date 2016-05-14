package wcdi.spajam2016;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivityFragment extends Fragment {

    public static interface OnEventListener {
        public abstract void onJoin(int groupId, int password, int userId);
    }

    OnEventListener onEventListener;

    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        onEventListener = (OnEventListener) context;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText editText = (EditText) view.findViewById(R.id.edit_text);

        view.findViewById(R.id.join_button).setOnClickListener((View v) -> {
            if (editText.getText().toString().isEmpty()) {
                return;
            }

            final int password = Integer.parseInt(editText.getText().toString());

            getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<String>() {
                @Override
                public Loader<String> onCreateLoader(int id, Bundle args) {
                    return new AsyncTaskLoader<String>(getContext()) {
                        @Override
                        public String loadInBackground() {
                            try {
                                return utils.getURL("http://spajam.hnron.net:8080/join_group/" + String.valueOf(password));
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    };
                }

                @Override
                public void onLoadFinished(Loader<String> loader, String data) {
                    if (data == null) {
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
                    }
                }

                @Override
                public void onLoaderReset(Loader<String> loader) {
                }
            }).forceLoad();
        });

        view.findViewById(R.id.create_button).setOnClickListener((View v) -> {
            getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<String>() {
                @Override
                public Loader<String> onCreateLoader(int id, Bundle args) {
                    return new AsyncTaskLoader<String>(getContext()) {
                        @Override
                        public String loadInBackground() {
                            try {
                                return utils.postURL("http://spajam.hnron.net:8080/create_group");
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    };
                }

                @Override
                public void onLoadFinished(Loader<String> loader, String data) {
                    if (data == null) {
                        return;
                    }

                    try {
                        JSONObject object = new JSONObject(data);
                        onEventListener.onJoin(
                                object.getInt("group_id"),
                                object.getInt("password"),
                                object.getInt("user_id")
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

}

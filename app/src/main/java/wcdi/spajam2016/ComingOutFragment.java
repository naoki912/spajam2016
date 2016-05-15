package wcdi.spajam2016;

import android.net.Uri;
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
import java.util.ArrayList;


public class ComingOutFragment extends Fragment {
    private static final String EXTRA_INT__STATE_GROUP_ID = "state_group_id";

    private int state_group_id;

    private OnFragmentInteractionListener mListener;

    public ComingOutFragment() {
    }

    public static ComingOutFragment newInstance(int state_group_id) {
        ComingOutFragment fragment = new ComingOutFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_INT__STATE_GROUP_ID, state_group_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            state_group_id = getArguments().getInt(EXTRA_INT__STATE_GROUP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coming_out_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> list = new ArrayList<>();

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

                            if (object.getJSONArray("coming_out_texts") == null) {
                                return;
                            }

                            JSONArray jsonArray = object.getJSONArray("coming_out_texts");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(jsonArray.getString(i));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            @Override
            public void onLoaderReset(Loader<String> loader) {
            }
        }).forceLoad();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), R.layout.fragment_item_coming_out, R.id.fragment_item_coming_out__text_view
        );

        ListView listView = (ListView) view.findViewById(R.id.fragment_coming_out_list__text_list);
        listView.setAdapter(adapter);
        adapter.addAll(list);
    }

/*    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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

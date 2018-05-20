package com.python.cricket.weedstore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.python.cricket.weedstore.models.Route;
import com.python.cricket.weedstore.models.Routes;
import com.python.cricket.weedstore.services.APIStore;
import com.python.cricket.weedstore.services.RetrofitMan;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RouteMapFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private RouteMapInteractionListener mListener;

    RetrofitMan rf;
    APIStore api;
    ArrayList<Route> route_list = new ArrayList<>();
    MyRouteMapRecyclerViewAdapter mcrva = new MyRouteMapRecyclerViewAdapter(route_list, mListener);
    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RouteMapFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RouteMapFragment newInstance(int columnCount) {
        RouteMapFragment fragment = new RouteMapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rf.init(getActivity());
        api = rf.get();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        route_list = new ArrayList<Route>();

        View view = inflater.inflate(R.layout.fragment_routemap_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(mcrva);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fillRoutes();
    }

    private void fillRoutes() {
        api.getEmployeeRoute(DataApplication.userID).enqueue(new Callback<ArrayList<Route>>() {
            @Override
            public void onResponse(Call<ArrayList<Route>> call, Response<ArrayList<Route>> response) {
                if(response.isSuccessful()){
                    route_list = response.body();
                    mcrva = new MyRouteMapRecyclerViewAdapter(route_list, mListener);
                    recyclerView.setAdapter(mcrva);
                }else{
                    Toast.makeText(getActivity(), "Response error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Route>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = new RouteMapInteractionListener();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Routes item);
    }

    public class RouteMapInteractionListener implements RouteMapFragment.OnListFragmentInteractionListener {

        @Override
        public void onListFragmentInteraction(Routes item) {
            //Toast.makeText(getActivity(), "Ruta "+item.getIdPath(), Toast.LENGTH_SHORT).show();
            Intent intMap = new Intent(getActivity(), MapsActivity.class);
            intMap.putExtra("routeID", item.getIdPath());
            startActivity(intMap);
        }
    }
}

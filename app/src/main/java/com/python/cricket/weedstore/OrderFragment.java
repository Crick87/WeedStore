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

import com.python.cricket.weedstore.models.Customer;
import com.python.cricket.weedstore.models.Order;
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
public class OrderFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OrderInteractionListener mListener;

    RetrofitMan rf;
    APIStore api;
    ArrayList<Order> order_list = new ArrayList<>();
    ArrayList<Customer> customer_list = new ArrayList<>();
    MyOrderRecyclerViewAdapter mcrva = new MyOrderRecyclerViewAdapter(order_list, customer_list, mListener);
    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OrderFragment newInstance(int columnCount) {
        OrderFragment fragment = new OrderFragment();
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

        order_list = new ArrayList<Order>();

        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

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
        fillOrders();
    }

    private void fillOrders() {
        api.getOrders().enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                if(response.isSuccessful()){
                    order_list = response.body();

                    api.getCustomers().enqueue(new Callback<ArrayList<Customer>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Customer>> call, Response<ArrayList<Customer>> response) {
                            if(response.isSuccessful()){
                                customer_list = response.body();
                                mcrva = new MyOrderRecyclerViewAdapter(order_list, customer_list, mListener);
                                recyclerView.setAdapter(mcrva);
                            }else{
                                Toast.makeText(getActivity(), "Response error", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Customer>> call, Throwable t) {
                            Toast.makeText(getActivity(), "Server error", Toast.LENGTH_LONG).show();
                        }
                    });

                }else{
                    Toast.makeText(getActivity(), "Response error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = new OrderInteractionListener();
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
        void onListFragmentInteraction(Order item);
    }

    public class OrderInteractionListener implements OrderFragment.OnListFragmentInteractionListener {

        @Override
        public void onListFragmentInteraction(Order item) {
            //Intent i = new Intent(getActivity(), OrderActivity.class);
            //i.putExtra("productID", Integer.toString(item.getId()));
            //startActivity(i);
        }
    }
}

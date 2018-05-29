package com.python.cricket.weedstore;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.python.cricket.weedstore.dummy.DummyContent;
import com.python.cricket.weedstore.dummy.DummyContent.DummyItem;
import com.python.cricket.weedstore.models.Product;
import com.python.cricket.weedstore.services.APIStore;
import com.python.cricket.weedstore.services.RetrofitMan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemOrderFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ItemOrderInteractionListener mListener;

    APIStore api;
    ArrayList<Product> products_list = new ArrayList<>();
    MyItemOrderRecyclerViewAdapter mcrva = new MyItemOrderRecyclerViewAdapter(products_list, mListener);
    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemOrderFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemOrderFragment newInstance(int columnCount) {
        ItemOrderFragment fragment = new ItemOrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = RetrofitMan.get();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        products_list = new ArrayList<Product>();

        View view = inflater.inflate(R.layout.fragment_itemorder_list, container, false);

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

    private void fillOrders(){
        products_list = new ArrayList<>(Arrays.asList(DataApplication.basketList));
        mcrva = new MyItemOrderRecyclerViewAdapter(products_list, mListener);
        recyclerView.setAdapter(mcrva);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = new ItemOrderInteractionListener();
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
        void onListFragmentInteraction(Product item);
    }

    public class ItemOrderInteractionListener implements OnListFragmentInteractionListener{

        @Override
        public void onListFragmentInteraction(Product item) {
            //Toast.makeText(getActivity(), item.getName(), Toast.LENGTH_SHORT).show();
            /*Intent i = new Intent(getActivity(), CustomerActivity.class);
            i.putExtra("customerID", Integer.toString(item.getId()));
            startActivity(i);*/
        }
    }
}

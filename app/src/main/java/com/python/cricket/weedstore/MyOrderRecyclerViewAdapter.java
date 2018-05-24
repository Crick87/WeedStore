package com.python.cricket.weedstore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.python.cricket.weedstore.OrderFragment.OnListFragmentInteractionListener;
import com.python.cricket.weedstore.models.Customer;
import com.python.cricket.weedstore.models.Order;
import com.python.cricket.weedstore.services.APIStore;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyOrderRecyclerViewAdapter extends RecyclerView.Adapter<MyOrderRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Order> mValues;
    private final ArrayList<Customer> mCustomers;
    private final OnListFragmentInteractionListener mListener;

    public MyOrderRecyclerViewAdapter(ArrayList<Order> items, ArrayList<Customer> customers, OnListFragmentInteractionListener listener) {
        mValues = items;
        mCustomers = customers;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Customer customerT = new Customer();
        for(int i=0; i<mCustomers.size(); i++){
            if (mCustomers.get(i).getId() == mValues.get(position).getCustomerId()){
                customerT = mCustomers.get(i);
            }
        }

        holder.mIdView.setText("Orden de "+customerT.getName());

        String orderDate = "";
        Order order = mValues.get(position);
        if (order.isStatus()){
            orderDate += "\uD83D\uDD35 ";
        }else {
            orderDate += "\uD83D\uDD34 ";
        }
        if (order.getProductList().length>1){
            orderDate += order.getProductList().length+" productos • ";
        }else if (order.getProductList().length == 1){
            orderDate += "1 producto • ";
        }
        orderDate += order.getOrderdate().getDayOfMonth()+"/"+
                order.getOrderdate().getMonthValue()+"/"+
                order.getOrderdate().getYear();

        holder.mContentView.setText(orderDate);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Order mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

package com.python.cricket.weedstore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.python.cricket.weedstore.RouteMapFragment.OnListFragmentInteractionListener;
import com.python.cricket.weedstore.models.Route;
import com.python.cricket.weedstore.models.Routes;

import java.util.ArrayList;

public class MyRouteMapRecyclerViewAdapter extends RecyclerView.Adapter<MyRouteMapRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Routes> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyRouteMapRecyclerViewAdapter(ArrayList<Route> items, OnListFragmentInteractionListener listener) {
        mValues = filterItems(items);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_routemap, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText("Ruta "+mValues.get(position).getIdPath());
        holder.mContentView.setText(mValues.get(position).getNoItems()+" clientes.");

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
        public Routes mItem;

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

    private ArrayList<Routes> filterItems(ArrayList<Route> routes){
        ArrayList<Routes> routesList = new ArrayList<>();
        int idRoute = 0;
        int noItems = 0;
        for( int i=0; i<routes.size(); i++ ){
            if ( i == 0 )
                idRoute = routes.get(i).getIdPath();
            if( idRoute == routes.get(i).getIdPath() ){
                noItems++;
            }
            if( idRoute != routes.get(i).getIdPath() || (i+1)==routes.size() ){
                Routes route = new Routes();
                route.setIdPath(idRoute);
                route.setNoItems(noItems);
                routesList.add(route);

                idRoute = routes.get(i).getIdPath();
                noItems = 1;
            }
        }
        return routesList;
    }
}

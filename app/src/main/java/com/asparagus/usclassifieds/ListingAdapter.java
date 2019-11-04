package com.asparagus.usclassifieds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListingAdapter extends ArrayAdapter<Listing> {
    public ListingAdapter (Context context, ArrayList<Listing> listings) { super(context, 0, listings); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listing_row_view, parent, false);
        }

        Listing listing = getItem(position);

        // Grab view object from listing_row_view to insert listing data
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);

        // Convert listing object data into viewable text
        tvTitle.setText(listing.getTitle());
        tvDescription.setText(listing.getDescription());
        tvPrice.setText(String.format("$ %.2f",listing.getPrice()));

        // Return completed view to render on screen
        return convertView;

    }
}

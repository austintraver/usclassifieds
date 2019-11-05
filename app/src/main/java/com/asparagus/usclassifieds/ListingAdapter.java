package com.asparagus.usclassifieds;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ListingAdapter extends ArrayAdapter<Listing> {
    public ListingAdapter (Context context, ArrayList<Listing> listings)
    {
        super(context, 0, listings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listing_row_view, parent, false);
        }

        Listing listing = getItem(position);

        // Grab view object from listing_row_view to insert listing data
        Button bTitle = convertView.findViewById(R.id.bTitle);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        TextView tvPrice = convertView.findViewById(R.id.tvSinglePrice);

        // Convert listing object data into viewable text
        bTitle.setText(listing.getTitle());
        bTitle.setTag(listing);
        bTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                System.out.println("Clicked View " + view.getId());
                Listing listing= (Listing) view.getTag();
                System.out.println("Listing data is " + listing.getTitle());
;
                Intent intent = new Intent(view.getContext(), single_listing.class);
                intent.putExtra("listing",listing);
                view.getContext().startActivity(intent);
            }
        });
        tvDescription.setText(listing.getDescription());
        tvPrice.setText(String.format("$ %.2f",listing.getPrice()));

        // Return completed view to render on screen
        return convertView;
    }

}

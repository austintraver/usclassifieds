package com.asparagus.usclassifieds;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;
import static androidx.core.app.ActivityCompat.startActivityForResult;

public class ListingAdapter extends ArrayAdapter<Listing> {
    ListingAdapter(Context context, ArrayList<Listing> listings) {
        super(context, 0, listings);
    }
    private static final int searching = 1045;

    @NotNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listing_row_view, parent, false);
        }
        Listing listing = getItem(position);
        // Grab view object from listing_row_view to insert listing data
        Button bTitle = convertView.findViewById(R.id.bTitle);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        TextView tvPrice = convertView.findViewById(R.id.detail_price);
        // Convert listing object data into viewable text
        if (listing != null) {
            bTitle.setText(listing.getTitle());
            bTitle.setTag(listing);
        }
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                Listing listing = (Listing) view.getTag();
                Intent intent = new Intent(view.getContext(), SingleListingActivity.class);
                intent.putExtra("listing", listing);
                view.getContext().startActivity(intent);
                //view.getContext().startActivityForResult(intent, searching);
            }
        };
        bTitle.setOnClickListener(onClickListener);
        if (listing != null) {
            tvDescription.setText(listing.getDescription());
            tvPrice.setText(String.format("$ %.2f", listing.getPrice()));
        }
        notifyDataSetChanged();
        // Return completed view to render on screen
        return convertView;
    }

}

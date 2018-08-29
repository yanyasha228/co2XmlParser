package com.example.test.testproj.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.test.testproj.R;
import com.example.test.testproj.models.Offer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * Custom RecyclerView adapter extends {@link RecyclerView.Adapter}
 *
 *
 *
 *
 * @author Ruslan Zosimov
 * @version 1.0
 */


public class ShowsListAdapter extends RecyclerView.Adapter<ShowsListAdapter.ShowViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    List<Offer> offerList = Collections.emptyList();
    OfferClickListener offerClickListener;

    public ShowsListAdapter(Context context, List<Offer> showList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.offerList = showList;
    }

    @Override
    public ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.show_item, parent, false);
        ShowViewHolder holder = new ShowViewHolder(view);
        return holder;
    }

    public void setOfferClickListener(OfferClickListener offerClickListener) {
        this.offerClickListener = offerClickListener;
    }

    @Override
    public void onBindViewHolder(ShowsListAdapter.ShowViewHolder holder, int position) {
        Offer currentOffer = offerList.get(position);
        Glide.with(context).load(currentOffer.getImage()).into(holder.offerImage);
        holder.offerName.setText(currentOffer.getName());
        holder.quantityCard.setText(String.valueOf(currentOffer.getStock_quantity()));
        holder.offerPrice.setText(String.valueOf(currentOffer.getPrice()) + " " + currentOffer.getCurrencyId());
        holder.offersVendor.setText(String.valueOf(currentOffer.getVendor()));
        if (currentOffer.getFav() == 1){ holder.offerFavorite.setImageResource(R.drawable.heart_like);}
        else {holder.offerFavorite.setImageResource(R.drawable.heart_unlike);}
        if (currentOffer.getOffer_changed() == 1){ holder.offerChanged.setImageResource(R.drawable.circgreen);}
        else {holder.offerChanged.setImageResource(R.drawable.circred);}

    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton offerImage;
        TextView offerName;
        TextView offerPrice;
        ImageButton offerFavorite;
        RelativeLayout offerItemLayout;
        TextView quantityCard;
        TextView offersVendor;
        ImageButton offerChanged;

        public ShowViewHolder(View itemView) {
            super(itemView);
            offersVendor =(TextView) itemView.findViewById(R.id.cardVendor);
            offerItemLayout = (RelativeLayout) itemView.findViewById(R.id.showItemLayout);
            offerImage = (ImageButton) itemView.findViewById(R.id.offerImage);
            offerName = (TextView) itemView.findViewById(R.id.offerName);
            offerPrice = (TextView) itemView.findViewById(R.id.offerPrice);
            offerFavorite = (ImageButton) itemView.findViewById(R.id.offerFavorite);
            quantityCard = (TextView) itemView.findViewById(R.id.quantityCard);
            offerChanged =(ImageButton) itemView.findViewById(R.id.hbc);
            offerItemLayout.setOnClickListener(this);
            offerFavorite.setOnClickListener(this);
            offerImage.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (offerClickListener != null) {
                offerClickListener.showClicked(view, getPosition());
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setFilter(List<Offer> filteShowList) {
        offerList = new ArrayList<Offer>();
        offerList.addAll(filteShowList);
        notifyDataSetChanged();

    }

    //Inteface for event handling in fragments
    public interface OfferClickListener {
        public void showClicked(View view, int position);
    }
}

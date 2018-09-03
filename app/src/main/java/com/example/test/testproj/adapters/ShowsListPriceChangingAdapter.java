package com.example.test.testproj.adapters;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.example.test.testproj.R;
import com.example.test.testproj.models.Offer;

import java.util.List;

public class ShowsListPriceChangingAdapter extends ShowsListAdapter{

    private Context context;

    public ShowsListPriceChangingAdapter(Context context, List<Offer> showList) {
        super(context, showList);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ShowViewHolder holder, int position, List<Object> payloads) {
        Offer currentOffer = offerList.get(position);
        Glide.with(context).load(currentOffer.getImage()).into(holder.offerImage);
        holder.offerName.setText(currentOffer.getName());
        holder.quantityCard.setText(String.valueOf(currentOffer.getStock_quantity()));
        holder.offerPrice.setText(String.valueOf(currentOffer.getPrice()) + " " + currentOffer.getCurrencyId());
        holder.offersVendor.setText(String.valueOf(currentOffer.getVendor()));
        if (currentOffer.isSelectedForChangingPrice()){ holder.offerFavorite.setImageResource(R.drawable.heart_like);}
        else {holder.offerFavorite.setImageResource(R.drawable.heart_unlike);}
        if (currentOffer.getOffer_changed() == 1){ holder.offerChanged.setImageResource(R.drawable.circgreen);}
        else {holder.offerChanged.setImageResource(R.drawable.circred);}
    }
}

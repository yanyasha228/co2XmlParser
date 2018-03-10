package com.example.test.testproj;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.test.testproj.adapters.DBAdapter;
import com.example.test.testproj.models.Offer;

public class ChangeOfferActivity extends AppCompatActivity implements View.OnClickListener {
    private DBAdapter dbAdapter;
    private Offer infoOffer;
    private ImageView offersImage;
    private EditText changeVendor;
    private EditText changeQuantity;
    private EditText changeName;

    private Button buttonSave;
    private Button buttonSurf;
    private EditText changePrice;
    private LinearLayout mainLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_offer);
        mainLay = (LinearLayout) findViewById(R.id.activity_change_offer_lin);
        //buttonSave = (Button) findViewById(R.id.saveChanges);
        //buttonSurf = (Button) findViewById(R.id.surf);
        changeName = (EditText) findViewById(R.id.newName);
        offersImage = (ImageView) findViewById(R.id.offersImage);
        changeVendor = (EditText) findViewById(R.id.newVendor);
        changeQuantity = (EditText) findViewById(R.id.newQuantity);
        changePrice = (EditText) findViewById(R.id.newOffersPrice);
        dbAdapter = new DBAdapter(this);
        Intent intent = getIntent();
        infoOffer = getOfferById(intent.getLongExtra("offersId", 0));
        Glide.with(this).load(infoOffer.getImage()).into(offersImage);
        changeName.setText(infoOffer.getName());
        changeVendor.setText(infoOffer.getVendor());
        changeQuantity.setText(String.valueOf(infoOffer.getStock_quantity()));
        changePrice.setText(String.valueOf(infoOffer.getPrice()));
        addMainButtons(mainLay);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveChanges:
                updateOffer();
                break;
            case R.id.surf:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(infoOffer.getUrl()));
                startActivity(intent);
                break;
        }

    }

    void addMainButtons(LinearLayout mainLay){
        LinearLayout newLay = new LinearLayout(this);
        newLay.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams newLayParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newLay.setLayoutParams(newLayParams);
        buttonSave = new Button(this);
        buttonSurf = new Button(this);
        LinearLayout.LayoutParams mainButtonsParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mainButtonsParams.weight = 1;
        buttonSurf.setLayoutParams(mainButtonsParams);
        buttonSave.setLayoutParams(mainButtonsParams);
        buttonSave.setText("Save Changes");
        buttonSurf.setText("Watch Offer full info");
        buttonSave.setId(R.id.saveChanges);
        buttonSurf.setId(R.id.surf);
        buttonSave.setOnClickListener(this);
        buttonSurf.setOnClickListener(this);
        newLay.addView(buttonSave);
        newLay.addView(buttonSurf);
        mainLay.addView(newLay);
    }

    private void updateOffer() {
        if (isDigit(String.valueOf(changeQuantity.getText())))
            infoOffer.setStock_quantity(Integer.valueOf(String.valueOf(changeQuantity.getText())));
        if (isDigit(String.valueOf(changePrice.getText())))
            infoOffer.setPrice(Double.valueOf(String.valueOf(changePrice.getText())));
        infoOffer.setVendor(String.valueOf(changeVendor.getText()));
        infoOffer.setName(String.valueOf(changeName.getText()));
        dbAdapter.open();
        dbAdapter.update(infoOffer);
        dbAdapter.close();
        finish();
    }

    private static boolean isDigit(String s) throws NumberFormatException {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Offer getOfferById(long id) {
        Offer fOffer = new Offer();
        dbAdapter.open();
        fOffer = dbAdapter.getOfferById(id);
        dbAdapter.close();
        return fOffer;
    }
}

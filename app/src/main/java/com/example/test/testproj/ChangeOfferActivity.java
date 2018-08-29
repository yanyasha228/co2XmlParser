package com.example.test.testproj;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.test.testproj.adapters.DBAdapter;
import com.example.test.testproj.models.Offer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class ChangeOfferActivity extends AppCompatActivity implements View.OnClickListener {
    private DBAdapter dbAdapter;
    private Offer infoOffer;
    private ImageView offersImage;
    private EditText changeVendor;
    private EditText changeQuantity;
    private EditText changeName;
    private EditText changeDescription;
    private CheckBox availableCheckBox;
    private Button buttonSave;
    private Button buttonSurf;
    private EditText changePrice;
    private LinearLayout mainLay;
    private List<LinearLayout> paramsLayList = new ArrayList<LinearLayout>();
    private Document offerXmlParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_offer);
        mainLay = (LinearLayout) findViewById(R.id.activity_change_offer_lin);
        changeDescription = (EditText) findViewById(R.id.changeDescription);
        changeName = (EditText) findViewById(R.id.newName);
        offersImage = (ImageView) findViewById(R.id.offersImage);
        changeVendor = (EditText) findViewById(R.id.newVendor);
        changeQuantity = (EditText) findViewById(R.id.newQuantity);
        changePrice = (EditText) findViewById(R.id.newOffersPrice);
        availableCheckBox = (CheckBox) findViewById(R.id.availableCheckBox);
        dbAdapter = new DBAdapter(this);
        Intent intent = getIntent();
        infoOffer = getOfferById(intent.getLongExtra("offersId", 0));
        if (infoOffer.getOffer_available() == 1) availableCheckBox.setChecked(true);
            offerXmlParams = infoOffer.getParams_xml();
        Glide.with(this).load(infoOffer.getImage()).into(offersImage);
        changeDescription.setText(infoOffer.getDescription());
        changeName.setText(infoOffer.getName());
        changeVendor.setText(infoOffer.getVendor());
        changeQuantity.setText(String.valueOf(infoOffer.getStock_quantity()));
        changePrice.setText(String.valueOf(infoOffer.getPrice()));
        addParamLay(mainLay);
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


    private void addMainButtons(LinearLayout mainLay) {
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
        buttonSave.setText("Сохранить изменения");
        buttonSurf.setText("Полная информация");
        buttonSave.setId(R.id.saveChanges);
        buttonSurf.setId(R.id.surf);
        buttonSave.setOnClickListener(this);
        buttonSurf.setOnClickListener(this);
        newLay.addView(buttonSave);
        newLay.addView(buttonSurf);
        mainLay.addView(newLay);
    }

    private void addParamLay(LinearLayout mainLay) {

        Element rootElement = offerXmlParams.getDocumentElement();
        NodeList paramList = rootElement.getElementsByTagName("param");
        Node currentParam = null;
        NamedNodeMap paramAttributes = null;
        Node attribute = null;
        if (paramList != null) {
            for (int i = 0; i < paramList.getLength(); i++) {
                currentParam = paramList.item(i);
                paramAttributes = currentParam.getAttributes();
                attribute = paramAttributes.getNamedItem("name");
                LinearLayout newLay = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_change_offer_param, null);
                newLay.setId(i);
                TextView newTextView = (TextView) newLay.getChildAt(0);
                newTextView.setText(attribute.getTextContent());
                EditText newEditText = (EditText) newLay.getChildAt(1);
                newEditText.setText(currentParam.getTextContent());
                paramsLayList.add(newLay);
                mainLay.addView(newLay);
            }

        }
    }

    private void updateOffer() {
        if (isDigit(String.valueOf(changeQuantity.getText())))
            infoOffer.setStock_quantity(Integer.valueOf(String.valueOf(changeQuantity.getText())));
        if (isDigit(String.valueOf(changePrice.getText())))
            infoOffer.setPrice(Double.valueOf(String.valueOf(changePrice.getText())));
        infoOffer.setVendor(String.valueOf(changeVendor.getText()));
        infoOffer.setName(String.valueOf(changeName.getText()));
        infoOffer.setDescription(String.valueOf(changeDescription.getText()));

        if(availableCheckBox.isChecked()){
            infoOffer.setOffer_available(1);
        }else infoOffer.setOffer_available(0);


        Element rootElement = offerXmlParams.getDocumentElement();
        NodeList paramList = rootElement.getElementsByTagName("param");
        Node currentParam = null;
        NamedNodeMap paramAttributes = null;
        Node attribute = null;

        if (paramList != null) {
            for (LinearLayout layParam : paramsLayList) {
                TextView paramTextView = (TextView) layParam.getChildAt(0);
                EditText paramEditText = (EditText) layParam.getChildAt(1);

                for (int i = 0; i < paramList.getLength(); i++) {
                    currentParam = paramList.item(i);
                    paramAttributes = currentParam.getAttributes();
                    attribute = paramAttributes.getNamedItem("name");

                    if (attribute.getTextContent().equalsIgnoreCase(String.valueOf(paramTextView.getText())))
                        currentParam.setTextContent(String.valueOf(paramEditText.getText()));

                }

            }
        }

        infoOffer.setParams_xml(offerXmlParams);
        infoOffer.setOffer_changed(1);

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

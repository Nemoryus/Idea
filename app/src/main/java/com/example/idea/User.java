package com.example.idea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class User extends AppCompatActivity {
    private LinearLayout dataLinearLayout;
    private Database database = new Database(User.this);
    private ArrayList<Idea> selectedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        database.initialise();

        dataLinearLayout = findViewById(R.id.idLinearLayoutData);

//        Menu categoriesMenu = null;
//        getMenuInflater().inflate(R.menu.categories_menu, categoriesMenu);
//        categoriesMenu.findItem(R.id.category).setTitle(Html.fromHtml("<font color='#000000'>Settings</font>"));
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.categories_menu);

        selectedData = new ArrayList<Idea>(database.selectData());

        if(!selectedData.isEmpty()) {
            processingSelectedData(selectedData, dataLinearLayout);

            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.categoryGastronomia:
                            selectedData = new ArrayList<Idea>(database.selectCategoryData("Gastronómia"));
                            processingSelectedData(selectedData, dataLinearLayout);
                            return true;
                        case R.id.categoryEshop:
                            selectedData = new ArrayList<Idea>(database.selectCategoryData("E-shop"));
                            processingSelectedData(selectedData, dataLinearLayout);
                            return true;
                        case R.id.categorySluzby:
                            selectedData = new ArrayList<Idea>(database.selectCategoryData("Služby"));
                            processingSelectedData(selectedData, dataLinearLayout);
                            return true;
                        case R.id.categoryManualnapraca:
                            selectedData = new ArrayList<Idea>(database.selectCategoryData("Manuáln práca"));
                            processingSelectedData(selectedData, dataLinearLayout);
                            return true;
                        case R.id.categoryStavebnictvo:
                            selectedData = new ArrayList<Idea>(database.selectCategoryData("Stavebníctvo"));
                            processingSelectedData(selectedData, dataLinearLayout);
                            return true;
                        case R.id.categoryAll:
                            selectedData = new ArrayList<Idea>(database.selectData());
                            processingSelectedData(selectedData, dataLinearLayout);
                            return true;
                        default:
                            return false;
                    }
                }
            });
        }else {
            addErrorTextView(dataLinearLayout);
        }

    }

    private void processingSelectedData(ArrayList<Idea> aSelectedData, LinearLayout aDataLinearLayout) {
        aDataLinearLayout.removeAllViews();
        for (int count = 0; count < aSelectedData.size(); count++) {
            Idea idea = aSelectedData.get(count);
            String name = idea.getName();
            String body = idea.getBody();
            addTextView(aDataLinearLayout, count + 1, name, body);

            int countOfIdeas = aSelectedData.size();
            int notLast = aSelectedData.size() - 1;

            if (notLast != count || countOfIdeas == 1) {
                addImageView(aDataLinearLayout);
            }
        }

    }

    private void addTextView(LinearLayout aLayout, int aCount, String aName, String aBody) {
        TextView headTextView = new TextView(this);
        TextView bodyTextView = new TextView(this);
        LinearLayout.LayoutParams headParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        headParams.setMargins(0, 0, 0, 20);
        headTextView.setLayoutParams(headParams);
        Spanned headHtml = Html.fromHtml(
                "#<b>" + aCount + "    " + aName + "</b> "
        );
        Typeface typefaceHead = ResourcesCompat.getFont(this, R.font.open_sans_regular);
        headTextView.setTypeface(typefaceHead);
        headTextView.setTextColor( Color.parseColor("#000000"));
        headTextView.setTextSize(20);
        headTextView.setText(headHtml);
        headTextView.setGravity(Gravity.CENTER);
        aLayout.addView(headTextView);


        Spanned bodyHtml = Html.fromHtml(
                aBody
        );
        LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        bodyTextView.setLayoutParams(bodyParams);
        Typeface bodyTypeface = ResourcesCompat.getFont(this, R.font.open_sans_regular);
        bodyTextView.setTypeface(bodyTypeface);
        bodyTextView.setTextColor( Color.parseColor("#000000"));
        bodyTextView.setTextSize(12);
        bodyTextView.setText(bodyHtml);
        bodyTextView.setGravity(Gravity.CENTER);
        aLayout.addView(bodyTextView);
    }

    private void addErrorTextView(LinearLayout aLayout) {
        TextView rowTextView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 10);
        rowTextView.setLayoutParams(params);
        rowTextView.setText("Nenašli sa žiadne dáta!!!");
        aLayout.addView(rowTextView);
    }

    private void addImageView(LinearLayout aLayout) {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
        params.setMargins(00,50,0,0);
        imageView.setLayoutParams(params);
        int resource = getResources().getIdentifier("@drawable/rocket", null, this.getPackageName());
        imageView.setImageResource(resource);
        aLayout.addView(imageView);
    }
}
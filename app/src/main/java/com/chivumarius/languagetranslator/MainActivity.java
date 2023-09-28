package com.chivumarius.languagetranslator;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class MainActivity extends AppCompatActivity {

    // ▼ "DECLARATION" OF "WIDGETS IDS" ▼
    private Spinner fromSpinner, toSpinner;
    private TextInputEditText sourceEdt;
    private ImageView micIV;
    private MaterialButton translateBtn;
    private TextView translatedTV;


    // ▼ "FROM" → THE "SOURCE ARRAY" OF "STRINGS" - "SPINNER'S DATA" ▼
    String[] fromLanguages = {"from", "Romanian", "English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Czech", "Dutch", "French", "Urdu"};

    // ▼ "TO" → THE "SOURCE ARRAY" OF "STRINGS" - "SPINNER'S DATA" ▼
    String[] toLanguages = {"to","Romanian", "English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Czech", "Dutch", "French", "Urdu"};


    // ▼ "REQUEST PERMISSION CODE" ▼
    private static final int REQUEST_PERMISSION_CODE = 1;
    String languageCode, fromLanguageCode, toLanguageCode;




    // ▼ "ON CREATE()" METHOD ▼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // ▼ "INITIALIZATION" OF "WIDGETS IDS" ▼
        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);
        sourceEdt = findViewById(R.id.idEdtSource);
        micIV = findViewById(R.id.idIVMic);
        translateBtn = findViewById(R.id.idBtnTranslate);
        translatedTV = findViewById(R.id.idTvTranslatedTV);




        // ▼ "FUNCTIONALITY"
        //      → ADDING "DATA" → TO "SPINNERS" ▼

        // (1) "SPINNER 1"
        // ▼ SETTING "ON ITEM SELECTED LISTENER" → TO "FROM SPINNER" ▼
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // ▼ "ON ITEM SELECTED()" METHOD ▼
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                fromLanguageCode = GetLanguageCode(fromLanguages[i]);
            }


            // ▼ "ON NOTHING SELECTED()" METHOD ▼
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // ▼ "FROM ADAPTER"
        //      → FOR "BINDING" THE "DATA"
        //      → FROM THE "ARRAY"
        //      → TO THE "SPINNER" ▼
        ArrayAdapter fromAdapter = new ArrayAdapter(this, R.layout.spinner_item, fromLanguages);

        // ▼ SETTING "DROP DOWN VIEW RESOURCE" ▼
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // ▼ SETTING "ADAPTER" ▼
        fromSpinner.setAdapter(fromAdapter);





        // (2) "SPINNER 2"
        // ▼ SETTING "ON ITEM SELECTED LISTENER" → TO "TO SPINNER" ▼
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // ▼ "ON ITEM SELECTED()" METHOD ▼
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                toLanguageCode = GetLanguageCode(toLanguages[i]);
            }

            // ▼ "ON NOTHING SELECTED()" METHOD ▼
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // ▼ "TO ADAPTER" ▼
        ArrayAdapter toAdapter = new ArrayAdapter(this, R.layout.spinner_item, toLanguages);

        // ▼ SETTING "DROP DOWN VIEW RESOURCE" ▼
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // ▼ SETTING "ADAPTER" ▼
        toSpinner.setAdapter(toAdapter);




        //
        translateBtn.setOnClickListener(new View.OnClickListener(){

            // ▼ "ON CLICK()" METHOD ▼
            @Override
            public void onClick(View view){

                // ▼ "SETTING" THE "TRANSLATED TEXT" TO "NULL" ▼
                translatedTV.setText("");


                // ▼ CHECKING IF THE "SOURCE TEXT" IS EMPTY
                if(sourceEdt.getText().toString().isEmpty()){
                    // ▼ SHOWING A "TOAST MESSAGE" ▼
                    Toast.makeText(MainActivity.this, "Please enter your text to translate", Toast.LENGTH_SHORT).show();

                } else if(fromLanguageCode.isEmpty()){
                    // ▼ SHOWING A "TOAST MESSAGE" ▼
                    Toast.makeText(MainActivity.this, "Please select source language", Toast.LENGTH_SHORT).show();

                } else if(toLanguageCode.isEmpty()){
                    // ▼ SHOWING A "TOAST MESSAGE" ▼
                    Toast.makeText(MainActivity.this, "Please language to translate", Toast.LENGTH_SHORT).show();

                } else {
                    // ▼ CALLING THE "METHOD" ▼
                    TranslateText(fromLanguageCode, toLanguageCode, sourceEdt.getText().toString());
                }

            }
        });

    }




    // ▼ "TRANSLATE TEXT()" METHOD ▼
    private void TranslateText(String fromLanguageCode, String toLanguageCode, String src) {

        // ▼ "SETTING" THE "TRANSLATED TEXT" TO "NULL" ▼
        translatedTV.setText("Downloading Modal...");


        try{
            TranslatorOptions options = new TranslatorOptions.Builder()
                    .setSourceLanguage(fromLanguageCode)
                    .setTargetLanguage(toLanguageCode)
                    .build();

            Translator translator = Translation.getClient(options);

            DownloadConditions conditions = new DownloadConditions.Builder().build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {

                // ▼ "ON SUCCESS()" METHOD ▼
                @Override
                public void onSuccess(Void unused) {
                    // ▼ SETTING THE "TRANSLATED TEXT"
                    translatedTV.setText("Translating...");

                    // ▼ "ON SUCCESS LISTENER" ▼
                    translator.translate(src).addOnSuccessListener(new OnSuccessListener<String>() {

                        //▼ "ON SUCCESS()" METHOD ▼
                        @Override
                        public void onSuccess(String s) {
                            translatedTV.setText(s);
                        }

                        // ▼ "ON FAILURE LISTENER" ▼
                    }).addOnFailureListener(new OnFailureListener() {

                        //▼ "ON FAILURE()" METHOD ▼
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // ▼ SHOWING A "TOAST MESSAGE" ▼
                            Toast.makeText(MainActivity.this, "Fail to translate: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                // ▼ "ON FAILURE LISTENER" ▼
            }).addOnFailureListener(new OnFailureListener() {

                // ▼ "ON FAILURE()" METHOD ▼
                @Override
                public void onFailure(@NonNull Exception e) {
                    // ▼ SHOWING A "TOAST MESSAGE" ▼
                    Toast.makeText(MainActivity.this, "Fail to download the language: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }

    }






    // ▼ "GET LANGUAGE CODE()" METHOD ▼
    private String GetLanguageCode(String language) {

        // ▼ ATTEMPT ▼
        String languageCode;


        // ▼ "SWITCH CASE" FOR "LANGUAGE" ▼
        switch (language){
            case "Romanian":
                languageCode = TranslateLanguage.ROMANIAN;
                break;

            case "English":
                languageCode = TranslateLanguage.ENGLISH;
                break;

            case "Afrikaans":
                languageCode = TranslateLanguage.AFRIKAANS;
                break;
            case "Arabic":
                languageCode = TranslateLanguage.ARABIC;
                break;
            case "Belarusian":
                languageCode = TranslateLanguage.BELARUSIAN;
                break;
            case "Bengali":
                languageCode = TranslateLanguage.BENGALI;
                break;
            case "Catalan":
                languageCode = TranslateLanguage.CATALAN;
                break;

            case "Czech":
                languageCode = TranslateLanguage.CZECH;
                break;

            case "Welsh":
                languageCode = TranslateLanguage.DUTCH;
                break;

            case "Hindi":
                languageCode = TranslateLanguage.FRENCH;
                break;

            case "Urdu":
                languageCode = TranslateLanguage.URDU;
                break;

            default:
                languageCode = "";
        }
        return languageCode;
    }

}
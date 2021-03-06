package com.example.isit_mp3c.projet.patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.RadioButton;

import com.example.isit_mp3c.projet.R;
import com.example.isit_mp3c.projet.camera.CameraActivity;
import com.example.isit_mp3c.projet.database.SQLiteDBHelper;
import com.example.isit_mp3c.projet.database.User;

import java.util.ArrayList;
import java.util.List;

public class AddPatientAnonym extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener{

    private Toast mToast = null;
    private EditText height, weight, hemoglobin,
            vgm, tcmh, idr_cv, hypo, ret_he, platelet, ferritin, transferrin, serum_iron, cst,
            fibrinogen, crp, other, pseudo, age;
    private RadioButton rbCertain, rbAbsence, rbIncertain;
    private Spinner genderSpinner, ironSpinner;
    private List<User> users;
    private SQLiteDBHelper dbH = SQLiteDBHelper.getInstance(this);
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_anonym);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pseudo = (EditText)findViewById(R.id.pseudo);
        genderSpinner = (Spinner) findViewById(R.id.sexe_patient);
        ironSpinner =(Spinner)findViewById(R.id.iron_unit);
        height = (EditText)findViewById(R.id.height_patient);
        weight = (EditText)findViewById(R.id.weight_patient);
        hemoglobin = (EditText)findViewById(R.id.hb);
        vgm = (EditText)findViewById(R.id.vgm);
        tcmh = (EditText)findViewById(R.id.tcmh);
        idr_cv = (EditText)findViewById(R.id.idr_cv);
        hypo = (EditText)findViewById(R.id.hypo);
        ret_he = (EditText)findViewById(R.id.ret_he);
        platelet = (EditText)findViewById(R.id.platelet);
        ferritin = (EditText)findViewById(R.id.ferritin);
        transferrin = (EditText)findViewById(R.id.transferrin);
        serum_iron = (EditText)findViewById(R.id.srum_iron);
        cst = (EditText)findViewById(R.id.cst);
        fibrinogen = (EditText)findViewById(R.id.fibrinogen);
        crp = (EditText)findViewById(R.id.crp);
        other = (EditText)findViewById(R.id.other);
        age = (EditText)findViewById(R.id.age_patient);

        rbCertain = (RadioButton) findViewById(R.id.radioDeficiencyClear);
        rbAbsence = (RadioButton) findViewById(R.id.radioNoDeficiency);
        rbIncertain = (RadioButton) findViewById(R.id.radioDeficiencyUnclear);

        pseudo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(pseudo.length() >= 2) {
                    menu.getItem(0).setVisible(true);
                }
                else {
                    menu.getItem(0).setVisible(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(pseudo.length() >= 2) {
                    menu.getItem(0).setVisible(true);
                }
                else {
                    menu.getItem(0).setVisible(false);
                }
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.setGender, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genderSpinner.setAdapter(genderSpinnerAdapter);
        genderSpinner.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> ironSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.IronUnit, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        ironSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        ironSpinner.setAdapter(ironSpinnerAdapter);
        ironSpinner.setOnItemSelectedListener(this);
        ironSpinner.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                serum_iron.requestFocus();
                return false;
            }
        });

        Button cancel =(Button)findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        users = getPatient();

        final long lastID;

        if(users.size() != -1) {
            lastID = users.size()+1;
        } else {
            lastID = 0;
        }

        Button save = (Button)findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (isInputValid()) {
                    addNewPatient();
                    saveDialog(new View(getBaseContext()), lastID);
                }
            }
        });
    }

    //get all patients
    private List<User> getPatient() {
        List<User> users = new ArrayList<>();

        if(dbH.openDatabase()){
            users = dbH.getPatient();
        }
        dbH.close();

        return users;
    }

    private long addNewPatient() {
        long lastID =0;

        String HEIGHT = height.getText().toString();
        String WEIGHT = weight.getText().toString();
        String HEMOGLOBIN = hemoglobin.getText().toString();
        String VGM = vgm.getText().toString();
        String TCMH = tcmh.getText().toString();
        String IDR_CV = idr_cv.getText().toString();
        String HYPO = hypo.getText().toString();
        String RET_HE = ret_he.getText().toString();
        String PLATELET = platelet.getText().toString();
        String FERRITIN = ferritin.getText().toString();
        String TRANSFERRIN = transferrin.getText().toString();
        String SERUM_IRON = serum_iron.getText().toString();
        String CST = cst.getText().toString();
        String FIBRINOGEN = fibrinogen.getText().toString();
        String CRP = crp.getText().toString();
        String OTHER = other.getText().toString();
        String PSEUDO = pseudo.getText().toString().replace(" ","");

        String GENDER = String.valueOf(genderSpinner.getSelectedItem());
        String UNIT = String.valueOf(ironSpinner.getSelectedItem());
        String AGE = age.getText().toString();

        // Récupération de la carence
        String DEFICIENCY = getDeficiencyType();

        if(!HEIGHT.isEmpty()) {
            if (Float.parseFloat(HEIGHT) > 100) {
                HEIGHT = HEIGHT.substring(0, 1) + "." + HEIGHT.substring(1);
            }
        }

        if(dbH.openDatabase()) {
            lastID = dbH.addPatient(new User( GENDER, HEIGHT, WEIGHT, HEMOGLOBIN,
                    VGM, TCMH, IDR_CV, HYPO, RET_HE, PLATELET, FERRITIN,
                    TRANSFERRIN, SERUM_IRON, UNIT, CST, FIBRINOGEN, CRP, OTHER, "TRUE", PSEUDO, DEFICIENCY, AGE));
        }

        dbH.close();
        return lastID;
    }

    private void saveDialog(View view, final long lastID){
        final long id = lastID; // Not necessary. Could be deleted.

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);

        builder.setTitle(R.string.save_dialog_title)
                .setMessage(R.string.save_dialog_question)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent profil = new Intent(AddPatientAnonym.this,ProfilAnonymPatient.class);
                        profil.putExtra("last_ID", Integer.parseInt(String.valueOf(lastID)));
                        finish();
                        startActivity(profil);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        getMenuInflater().inflate(R.menu.menu_patient, menu);
        menu.getItem(0).setEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        final long lastID2;

        switch (item.getItemId()){
            case android.R.id.home:
                askToLeave();
                break;
            case R.id.save:
                users = getPatient();

                if(users.size() != -1) {
                    lastID2 = users.size()+1;
                } else {
                    lastID2 = 0;
                }

                if (isInputValid()) {
                    addNewPatient();
                    saveDialog(new View(getBaseContext()), lastID2);
                }
                break;
            case R.id.takePicture:
                long id = 0;
                User user = new User();

                if(isInputValid()) {
                    users = getPatient();

                    if(users.size() != -1) {
                        lastID2 = users.size()+1;
                    } else {
                        lastID2 = 0;
                    }

                    id = addNewPatient();
                    user = dbH.getPatientWithId(Integer.parseInt((Long.toString(id))));
                    saveDialog(new View(getBaseContext()), lastID2);

                    Intent intent = new Intent(AddPatientAnonym.this, CameraActivity.class);
                    Bundle b = new Bundle();
                    b.putString("pseudo", id + "-" + user.getPseudo());
                    intent.putExtras(b);
                    startActivity(intent);

                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Méthode nécessaire au bon fonctionnement des radioButtons
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioDeficiencyClear:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.radioNoDeficiency:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.radioDeficiencyUnclear:
                if (checked)
                    // AH
                    break;
        }
    }

    private boolean isInputValid() {
        ArrayList<Boolean> test = new ArrayList<>();
        boolean isValid = true;

        String tmp_pseudo = pseudo.getText().toString();
        String tmp_age = age.getText().toString();
        String tmp_height = height.getText().toString();
        String tmp_weight = weight.getText().toString();
        String tmp_idr_cv = idr_cv.getText().toString();
        String tmp_hypo = hypo.getText().toString();
        String tmp_cst = cst.getText().toString();

        if (tmp_pseudo.isEmpty()) {
            test.add(false);
            pseudo.setError(getString(R.string.condition_pseudo));
        }
        else{

            if (tmp_pseudo.matches("[a-zA-Z0-9 ]*")) {
                test.add(true);
            } else {
                test.add(false);
                pseudo.setError(getString(R.string.condition_pseudo_incorrectChars));
            }

        }

        if (tmp_age.isEmpty()) {
            test.add(true);
        } else {
            if (Float.parseFloat(tmp_age) <= 0 || Float.parseFloat(tmp_age) >= 130 ) {
                test.add(false);
                age.setError(getString(R.string.condition_age));
            } else {
                test.add(true);
            }
        }

        if (tmp_height.isEmpty()) {
            test.add(true);
        }
        else{
            if (Float.parseFloat(tmp_height) > 100) {
                tmp_height = tmp_height.substring(0, 1) + "." + tmp_height.substring(1);
            }
            if (Float.parseFloat(tmp_height) > 2.3) {
                test.add(false);
                height.setError(getString(R.string.condition_height));
            } else {
                test.add(true);
            }
        }

        if (tmp_weight.isEmpty()) {
            test.add(true);
        }
        else {
            if ((Float.parseFloat(tmp_weight) > 400 || Float.parseFloat(tmp_weight) < 20)) {
                test.add(false);
                weight.setError(getString(R.string.condition_weight));
            } else {
                test.add(true);
            }
        }

        if (tmp_idr_cv.isEmpty()) {
            test.add(true);
        }
        else {
            if (Float.parseFloat(tmp_idr_cv) > 100) {
                test.add(false);
                idr_cv.setError(getString(R.string.condition_idr_cv));
            } else {
                test.add(true);
            }
        }

        if (tmp_cst.isEmpty()) {
            test.add(true);
        }
        else {
            if (Float.parseFloat(tmp_cst) > 100) {
                test.add(false);
                cst.setError(getString(R.string.condition_cst));
            } else {
                test.add(true);
            }
        }

        if (tmp_hypo.isEmpty()) {
            test.add(true);
        } else {
            if (Float.parseFloat(tmp_hypo) > 100) {
                test.add(false);
                hypo.setError(getString(R.string.condition_hypo));
            } else {
                test.add(true);
            }
        }

        for (Boolean iter : test) {
            if(!iter){
                isValid=false;

                if (mToast != null) mToast.cancel();
                mToast = Toast.makeText(AddPatientAnonym.this, getString(R.string.error), Toast.LENGTH_SHORT);
                mToast.show();

                break;
            }
        }

        return isValid ? true : false;
    }

    private String getDeficiencyType()
    {
        if (rbCertain.isChecked())
            return "Carence certaine";
        else if(rbAbsence.isChecked())
            return "Absence de carence";
        else if(rbIncertain.isChecked())
            return "Carence incertaine";
        else
            return "";
    }

    @Override
    public void onBackPressed() {
        askToLeave();
    }

    private void askToLeave(){
        if ( !height.getText().toString().equals("") ||
                !weight.getText().toString().equals("") ||
                !hemoglobin.getText().toString().equals("") ||
                !vgm.getText().toString().equals("") ||
                !tcmh.getText().toString().equals("") ||
                !idr_cv.getText().toString().equals("") ||
                !hypo.getText().toString().equals("") ||
                !ret_he.getText().toString().equals("") ||
                !platelet.getText().toString().equals("") ||
                !ferritin.getText().toString().equals("") ||
                !transferrin.getText().toString().equals("") ||
                !serum_iron.getText().toString().equals("") ||
                !cst.getText().toString().equals("") ||
                !fibrinogen.getText().toString().equals("") ||
                !crp.getText().toString().equals("") ||
                !other.getText().toString().equals("") ||
                !pseudo.getText().toString().equals("")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddPatientAnonym.this);
            alertDialogBuilder.setMessage(" Voulez vous vraiment annuler votre saisie ?  ");
            alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });

            alertDialogBuilder.setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }else {
            finish();
        }
    }
}

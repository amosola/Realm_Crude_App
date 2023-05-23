package com.example.realmcrudapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.realmcrudapp.databinding.ActivityMainBinding;

import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMainBinding binding;
    Realm realm;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //------------> get realm instance <--------------
        realm = Realm.getDefaultInstance();

        binding.btninsert.setOnClickListener((View.OnClickListener) this);
        binding.btnUpdate.setOnClickListener((View.OnClickListener) this);
        binding.btnRead.setOnClickListener((View.OnClickListener) this);
        binding.btnDelete.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btninsert){
            Log.d(TAG, "onClick: insert");
            showInsertDialog();
        }
        if (v.getId() == R.id.btnUpdate){
            Log.d(TAG, "onClick: Update");
        }
        if (v.getId() == R.id.btnRead){
            Log.d(TAG, "onClick: Read");
            showData();
        }
        if (v.getId() == R.id.btnDelete){
            Log.d(TAG, "onClick: Delete");
        }
    }

    private void showInsertDialog() {
        AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.user_data, null);
        al.setView(view);

        EditText name = view.findViewById(R.id.etName);
        EditText age = view.findViewById(R.id.etAge);
        Spinner gender = view.findViewById(R.id.spGender);
        Button save = view.findViewById(R.id.btnSave);
        final AlertDialog alertDialog = al.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                DataModel dataModel = new DataModel();

                //-------> accessing the last value of id in datamodel <---------
                Number current_id = realm.where(DataModel.class).max("id");
                long nextId;
                if (current_id == null){
                    nextId = 1;
                }else {
                    nextId = current_id.intValue() + 1;
                }

                dataModel.setId(nextId);
                dataModel.setName(name.getText().toString());
                dataModel.setAge(Integer.parseInt(age.getText().toString()));
                dataModel.setGender(gender.getSelectedItem().toString());

                //----> calling realm.exectu... and passing datamodel object to copyTo Ream <---
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyFromRealm(dataModel);
                    }
                });
            }
        });
    }


    private void showData() {
        List<DataModel> dataModels = realm.where(DataModel.class).findAll();
        for (int i = 0; i < dataModels.size(); i++) {
            binding.tvShowDetails.setText(
                    "Id"+dataModels.get(i).getId()+" " +
                            "Name "+ dataModels.get(i).getName()+" " +
                            "Age "+ dataModels.get(i).getAge()+" " +
                            "Gender "+ dataModels.get(i).getGender()+"\n");
        }
    }

}
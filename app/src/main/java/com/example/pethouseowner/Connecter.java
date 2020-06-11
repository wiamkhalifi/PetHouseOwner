package com.example.pethouseowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pethouseowner.Common.Common;
import com.example.pethouseowner.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Connecter extends AppCompatActivity {
    EditText petTel,petMdp;
    Button btnConnecter;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecter);
        petMdp=(MaterialEditText)findViewById(R.id.petMdp);
        petTel=(MaterialEditText)findViewById(R.id.petTel);
        btnConnecter=(Button) findViewById(R.id.btnConnecter);

         db=FirebaseDatabase.getInstance();
        users=db.getReference("User");
        btnConnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser(petTel.getText().toString(),petMdp.getText().toString());
            }
        });
    }

    private void signInUser(final String phone, String password)
    {
        final String localPhone=phone;
        final String localPassword=password;
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(localPhone).exists())
                {
                    User user=dataSnapshot.child(localPhone).getValue(User.class);
                    user.setPhone(localPhone);
                    if(Boolean.parseBoolean(user.getIsStaff()))
                    {
                        if(user.getPassword().equals(localPassword))
                        {
                            Intent i=new Intent(Connecter.this,Home.class);
                            Common.currentUser=user;
                            startActivity(i);
                            finish();
                        }
                        else
                            Toast.makeText(Connecter.this, "Mot de passe incorrect", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(Connecter.this, "Connectez vous avec le compte admin", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(Connecter.this, "L'utilisateur est inexistant", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

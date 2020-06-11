package com.example.pethouseowner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pethouseowner.Common.Common;
import com.example.pethouseowner.Interface.ItemClickListener;
import com.example.pethouseowner.Model.Category;
import com.example.pethouseowner.Model.Pro;
import com.example.pethouseowner.ViewHolder.ProViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class ProList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;

    FirebaseDatabase db;
    DatabaseReference prolist;
    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId="";
    FirebaseRecyclerAdapter<Pro, ProViewHolder> adapter;

    MaterialEditText edtName,edtDescription,edtPrice;
    Button btnSelect,btnUpload;
    Pro newPro;
    RelativeLayout rootLayout;
    Uri saveUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_list);

        db=FirebaseDatabase.getInstance();
        prolist=db.getReference("Pro");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_pro);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);

        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddProDialog();

            }
        });
        if(getIntent()!=null)
            categoryId=getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty())
            loadListPro(categoryId);
    }

    private void showAddProDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(ProList.this);
        alertDialog.setTitle("Ajouter un nouveau produit/animal");
        alertDialog.setMessage("Veuillez remplir tous les champs");
        LayoutInflater inflater=this.getLayoutInflater();

        View add_menu_layout=inflater.inflate(R.layout.add_new_pro_layout,null);
        edtName=add_menu_layout.findViewById(R.id.edtName);
        edtDescription=add_menu_layout.findViewById(R.id.edtDescription);
        edtPrice=add_menu_layout.findViewById(R.id.edtPrice);
        btnSelect=add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload=add_menu_layout.findViewById(R.id.btnUpload);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(newPro!=null)
                {
                    prolist.push().setValue(newPro);
                    Snackbar.make(rootLayout,"Le nouveau produit/animal " + newPro.getName() + " a été ajouté",Snackbar.LENGTH_SHORT)
                            .show();


                }
            }
        });
        alertDialog.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void loadListPro(String categoryId) {
        adapter=new FirebaseRecyclerAdapter<Pro, ProViewHolder>(
                Pro.class,
                R.layout.pro_item,
                ProViewHolder.class,
                prolist.orderByChild("menuId").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(ProViewHolder proViewHolder, Pro pro, int i) {
                proViewHolder.pro_name.setText(pro.getName());
                Picasso.with(getBaseContext())
                        .load(pro.getImage())
                        .into(proViewHolder.pro_image);

                proViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
    private void chooseImage() {
        //Intent intent=new Intent();
        // intent.setType("images/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        // startActivityForResult(Intent.createChooser(intent,"Selectionnez une photo"),PICK_IMAGE_REQUEST);
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, Common.PICK_IMAGE_REQUEST);
    }
    private void uploadImage() {

        if(saveUri!=null)
        {
            String imageName= UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ProList.this, "Image importée !", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newPro=new Pro();
                                    newPro.setName(edtName.getText().toString());
                                    newPro.setDescription(edtDescription.getText().toString());
                                    newPro.setPrice(edtPrice.getText().toString());
                                    newPro.setMenuId(categoryId);
                                    newPro.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Common.PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            saveUri=data.getData();
            btnSelect.setText("Image selectionnée !");
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateProDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
           deletePro(adapter.getRef(item.getOrder()).getKey());

        }

        return super.onContextItemSelected(item);
    }

    private void deletePro(String key) {
        prolist.child(key).removeValue();
    }

    private void showUpdateProDialog(final String key, final Pro item) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(ProList.this);
        alertDialog.setTitle("Modifier un produit/animal");
        alertDialog.setMessage("Veuillez remplir tous les champs");
        LayoutInflater inflater=this.getLayoutInflater();

        View add_menu_layout=inflater.inflate(R.layout.add_new_pro_layout,null);
        edtName=add_menu_layout.findViewById(R.id.edtName);
        edtDescription=add_menu_layout.findViewById(R.id.edtDescription);
        edtPrice=add_menu_layout.findViewById(R.id.edtPrice);


        edtName.setText(item.getName());
        edtPrice.setText(item.getPrice());
        edtDescription.setText(item.getDescription());
        btnSelect=add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload=add_menu_layout.findViewById(R.id.btnUpload);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImage(item);
            }
        });
        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                    item.setName(edtName.getText().toString());
                    item.setPrice(edtPrice.getText().toString());
                    item.setDescription(edtDescription.getText().toString());
                    prolist.child(key).setValue(item);
                    Snackbar.make(rootLayout,"Le produit/animal " + item.getName() + " a été modifié ",Snackbar.LENGTH_SHORT)
                            .show();


                }

        });
        alertDialog.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void changeImage( final Pro item) {
        if(saveUri!=null)
        {
            String imageName= UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ProList.this, "Image importée !", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    item.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });


        }

    }
}

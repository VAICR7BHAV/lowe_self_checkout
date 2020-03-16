package com.example.lowe_self_checkout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BarCode extends AppCompatActivity
{
    String filepath;
    Button Checkout;
    DatabaseReference dref;
    String TotalCost="0";
    TextView cost;
    private RecyclerView mRecyclerView;
    ArrayList<MyListData> results2 = new ArrayList<MyListData>();
    ArrayList<MyListData> database=new ArrayList<>();
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private RecyclerView.Adapter mAdapter;

    public void BarcodeScanner(Bitmap bitmap)
    {
        Log.d("Barcode","ScanningBarcode");
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                                FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
                        .build();
        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector();
        Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> barcodes)
                    {
                        // Task completed successfully
                        // ...
                        try{
                        //Log.d("Barcode_Func","I am here"+barcodes.get(0).getDisplayValue());
                        Log.d("Barcode_Func","I am here"+barcodes.get(0).getRawValue());
                        Log.d("Barcode_Func","I am here"+barcodes.get(0).toString());
                        Log.d("Barcode_Func","I am here"+barcodes.get(0).getUrl());
                        for(int i=0;i<database.size();i++)
                        {
                            MyListData temp=database.get(i);

                            if(database.get(i).getBarcode().equals(barcodes.get(0).getRawValue()))
                            {
                                MyListData obj = new MyListData(temp.getBarcode(),temp.getName(),temp.getWeight(),temp.getPrice());
                                TotalCost=Integer.parseInt(TotalCost)+obj.getWeight();
                                cost.setText(TotalCost);
                                mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                                results2.add(obj);
                                Log.d("Barcode_Func",obj.getBarcode()+" "+obj.getName());
                                //Log.d("Barcode_Func",results2.get(0).getmText1());
                                mAdapter=new MyListAdapter(results2);
                                //mAdapter = new MyRecyclerViewAdapter(results2);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        }

                        }
                        catch (Exception e)
                        {
                            Log.d("Barcode_Func","Exception");
                            e.printStackTrace();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                        Log.d("Barcode_Func","Failed");
                    }
                });
    }

    private void SaveImage(Bitmap finalBitmap)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        Log.d("Address is",root);
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        Log.d("Final_address",fname);
        filepath=myDir+"/"+fname;
        Log.d("Final_address",filepath);
        if (file.exists ()) file.delete ();
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }


        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Bitmap bm_to_send;
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);
            BarcodeScanner(photo);
            SaveImage(photo);
            Log.d("before_bm_to_send",filepath);
            bm_to_send= BitmapFactory.decodeFile(filepath);
            Log.d("After_bm_to_send",filepath);

        }
    }
    private void getDatabase() {
      Log.d("Snapshot","In get databsae");
        dref = FirebaseDatabase.getInstance().getReference();//.child("self-2de53");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String guidelnines ="";
                for(DataSnapshot snapshot :dataSnapshot.getChildren())
                {
                    //MyListData g_hindi = snapshot.getValue(MyListData.class);
                    //Log.d("Snapshot_","Barcode="+g_hindi.getName());
                    String barcode=snapshot.getKey();
                    String name = snapshot.child("Name").getValue().toString();
                    String price = snapshot.child("Price").getValue().toString();
                    String weight = snapshot.child("Weight").getValue().toString();
                    //Log.d("Snapshot",barcode+name+price+weight);
                    MyListData temp=new MyListData(barcode,name,price,weight);
                    database.add(temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });


    }
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);
        getDatabase();
        Checkout=(Button)findViewById(R.id.Checkout_button);
        Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(BarCode.this,Thanks.class);
                String total=(String)cost.getText();
                i.putExtra("message","Thank you for shopping with us. Your total bill is "+total+"Rs. You" +
                        "You will get your bil delivered to your doorstep.");
                results2.clear();
                mAdapter=new MyListAdapter(results2);
                //mAdapter = new MyRecyclerViewAdapter(results2);
                mRecyclerView.setAdapter(mAdapter);
                startActivity(i);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        cost=(TextView)(findViewById(R.id.cost));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton photoButton = (FloatingActionButton) this.findViewById(R.id.Camera_Button);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)
                {

                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
    }
}

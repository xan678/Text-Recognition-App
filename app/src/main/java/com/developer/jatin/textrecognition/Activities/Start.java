package com.developer.jatin.textrecognition.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.jatin.textrecognition.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;

public class Start extends AppCompatActivity {

    private ImageView b12;
    private TextView tv;
    private Button b1;
    private Button b2;
    private static final int REQUESTCODE=1;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        b12=findViewById(R.id.b12);
        tv=findViewById(R.id.text);
        b1=findViewById(R.id.button1);
        b2=findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,REQUESTCODE);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detect();
            }
        });



    }

    private void detect() {
        FirebaseVisionImage image=FirebaseVisionImage.fromBitmap(bmp);
        FirebaseVisionTextDetector detector= FirebaseVision.getInstance().getVisionTextDetector();
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                processthetext(firebaseVisionText);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    private void processthetext(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.Block> block=firebaseVisionText.getBlocks();
        if(block.size()==0){
            Toast.makeText(this, "No Image Detected", Toast.LENGTH_SHORT).show();
        }
        else{
            String txt="";
            for(FirebaseVisionText.Block  newBlock:firebaseVisionText.getBlocks()){
                txt=txt+newBlock.getText()+"\n";

            }
            tv.setText(txt);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUESTCODE && resultCode==RESULT_OK){
            Bundle bundle=data.getExtras();
            bmp=(Bitmap) bundle.get("DATA");
            b12.setImageBitmap(bmp);
        }
    }
}

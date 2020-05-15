package com.sarkar.fake_news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button button;
    EditText editText;
    TextView textView;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         button=findViewById(R.id.btn);
         editText=findViewById(R.id.et);
         textView=findViewById(R.id.result);
         progressDialog=new ProgressDialog(this);
         progressDialog.setIndeterminate(true);
         progressDialog.setCancelable(false);
        progressDialog.setMessage("Analyzing....");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string="99";
                if(editText.getText()!=null){
                    string=editText.getText().toString();
                    String Reqid=update(string);
                    resultfetch(Reqid);
                }

            }
        });
    }

    private void resultfetch(final String reqid) {
        databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child("id").getValue(String.class).equals(reqid)){
                        if(!snapshot.child("result").getValue(String.class).equals("null")){
                            progressDialog.dismiss();
                            textView.setText(snapshot.child("result").getValue(String.class));
                        }
                        else {
                            progressDialog.show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,"there's some problem:-(",Toast.LENGTH_SHORT);
            }
        });
    }

    private String update(String string) {
        String ReqId=idgenerator();
        databaseReference= FirebaseDatabase.getInstance().getReference(ReqId);
        databaseReference.child("id").setValue(ReqId);
        databaseReference.child("article_info").setValue(string);
        databaseReference.child("result").setValue("null");
        return ReqId;
    }
    public String idgenerator() {
        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int count=10;
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}

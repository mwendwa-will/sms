package com.example.readsms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView ListView;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS=100;
    ArrayList<String> smslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView=(ListView) findViewById(R.id.idList);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if (permissionCheck== PackageManager.PERMISSION_GRANTED){
            showContacts();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS},PERMISSIONS_REQUEST_READ_CONTACTS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==PERMISSIONS_REQUEST_READ_CONTACTS){
            showContacts();
        }else {
            Toast.makeText(this, "Until you grant permission we can not display the names", Toast.LENGTH_SHORT).show();
        }
    }

    private void showContacts() {
        Uri inboxUri = Uri.parse("content://sms/inbox");
        smslist = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = getContentResolver().query(inboxUri,null,null,null,null
        );
        while (cursor.moveToNext()){
            String number= cursor.getString(cursor.getColumnIndexOrThrow("address")).toString();
            String body = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString();
            smslist.add("Number"+number+"body"+body);

            cursor.close();
            ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,smslist);

            ListView.setAdapter(adapter);
        }
    }
}

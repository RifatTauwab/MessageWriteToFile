package com.example.rifat_ut_tauwab.filewrite;

import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button get_data_button;
    EditText mobileNo_edittext;
    File file;
    FileOutputStream outputStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get_data_button = (Button) findViewById(R.id.btn_generate);
        mobileNo_edittext = (EditText) findViewById(R.id.edittext_mobileNo);


    }
    public void onClick_btn_generate(View view)
    {
        String mobileNo = mobileNo_edittext.getText().toString();
        String address,millisec,msgtype;
        long millisecond;
        //Uri uri = Uri.parse("content://sms/inbox");
        //Uri uri = Uri.parse("content://mms-sms/conversations/");
        Uri uri = Uri.parse("content://sms/");

        //Cursor c= getContentResolver().query(uri, null,null,null,null);
        Cursor c = getContentResolver().query(uri, new String[] { "_id", "thread_id", "address", "date", "type", "body"}, null , null, "date" + " ASC");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        Calendar calendar = Calendar.getInstance();
        String finalDateString;
        try {
            file = new File(Environment.getExternalStorageDirectory(), "/sms/MyCache.txt");

            outputStream = new FileOutputStream(file);

            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {
                    millisec = c.getString(c.getColumnIndexOrThrow("date")).toString();
                    millisecond = Long.parseLong(millisec);
                    calendar.setTimeInMillis(millisecond);
                    finalDateString = formatter.format(calendar.getTime());
                    address = c.getString(c.getColumnIndex("address")).toString();
                    //address = c.getString(2);
                    if((address.equalsIgnoreCase(mobileNo)))
                    {
                         msgtype = c.getString(c.getColumnIndex("type")).toString();
                        outputStream.write((finalDateString+"\n").getBytes());
                        if(msgtype.equalsIgnoreCase("1"))
                            outputStream.write(("person ----"+"\n").getBytes());
                        else
                            outputStream.write(("me ----"+"\n").getBytes());
                        outputStream.write((c.getString(c.getColumnIndexOrThrow("body")).toString()+"\n").getBytes());
                        outputStream.write(("--------"+"\n").getBytes());



                    }
                    c.moveToNext();
                }
            }
            outputStream.close();
        }catch(Exception e)
        {

        }finally
        {

            c.close();
        }

    }

    }

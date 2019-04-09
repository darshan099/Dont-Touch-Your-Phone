package com.example.darshanpc.focus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button lock,disable,enable;
    EditText locktime;
    TextView textView;
    String[] strspeech=new String[] {"s1","s2","s3","s4","s5"};
    long time;
    ClassSpeak speaktext;
    public static final int ADMIN_ENABLE=1;
    public static final int TTS_AVAILABLE=2;
    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        devicePolicyManager=(DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
        componentName=new ComponentName(this,MyAdmin.class);
        lock=findViewById(R.id.lock);
        enable=findViewById(R.id.enableButton);
        locktime=findViewById(R.id.locktime);
        textView=findViewById(R.id.checkresume);
        disable=findViewById(R.id.disableButton);
        speaktext=new ClassSpeak(getApplicationContext());

        Intent checktts=new Intent();
        checktts.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checktts,TTS_AVAILABLE);

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.disclaimer);
                builder.setPositiveButton("You got my trust boii", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentName);
                        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"We need permission to only lock the screen.");
                        startActivityForResult(intent,ADMIN_ENABLE);
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });
        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicePolicyManager.removeActiveAdmin(componentName);
                disable.setVisibility(View.GONE);
                enable.setVisibility(View.VISIBLE);
            }
        });
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean active=devicePolicyManager.isAdminActive(componentName);
                if(TextUtils.isEmpty(locktime.getText()))
                {
                    Toast.makeText(MainActivity.this, "Give me a number", Toast.LENGTH_SHORT).show();
                }
                else if(active){
                    time=System.currentTimeMillis()+(Integer.parseInt(locktime.getText().toString())*60000);
                    devicePolicyManager.lockNow();
                }
                else{
                    Toast.makeText(MainActivity.this, "Enable Administration Access", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        boolean isactive=devicePolicyManager.isAdminActive(componentName);
        disable.setVisibility(isactive ? View.VISIBLE : View.GONE);
        enable.setVisibility(isactive ? View.GONE : View.VISIBLE);
        if(isactive && System.currentTimeMillis() < time)
        {
            int random=new Random().nextInt(5);
            int resid=getResources().getIdentifier(strspeech[random],"string","com.example.darshanpc.focus");
            speaktext.Speak(getResources().getString(resid));
            devicePolicyManager.lockNow();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode)
        {
            case ADMIN_ENABLE:
            {
                if(resultCode==Activity.RESULT_OK)
                {
                    Toast.makeText(MainActivity.this, "You have enabled admin features", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Problems in enabling admin features", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case TTS_AVAILABLE:
            {
                if(resultCode==TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL)
                {
                    AlertDialog.Builder validatetts=new AlertDialog.Builder(MainActivity.this);
                    validatetts.setMessage(R.string.no_tts);
                    validatetts.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent installtts=new Intent();
                            installtts.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                            startActivity(installtts);
                        }
                    });
                    AlertDialog showvalidatetts=validatetts.create();
                    showvalidatetts.show();
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

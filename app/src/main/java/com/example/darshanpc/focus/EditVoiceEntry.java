package com.example.darshanpc.focus;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class EditVoiceEntry extends AppCompatActivity {
    Switch voicetoggle;
    EditText addentry;
    ListView entrylistView;
    List entrylist;
    Button btnaddentry;
    ArrayAdapter arrayAdapter;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_voice_entry);
        setTitle("Add/Edit Entry");
        voicetoggle=findViewById(R.id.togglevoice);
        btnaddentry=findViewById(R.id.btnaddvoiceentry);
        addentry=findViewById(R.id.addvoiceentry);
        entrylistView=findViewById(R.id.entrylist);
        entrylist=new ArrayList();
        databaseHelper=new DatabaseHelper(this);

        if(databaseHelper.get_voice_toggle_value().equals("1"))
        {
            voicetoggle.setChecked(true);
        }
        else
        {
            voicetoggle.setChecked(false);
        }

        Cursor cursor=databaseHelper.get_entry();
        while (cursor.moveToNext())
        {
            entrylist.add(cursor.getString(0));
        }
        arrayAdapter=new ArrayAdapter(EditVoiceEntry.this,android.R.layout.simple_list_item_1,entrylist);
        entrylistView.setAdapter(arrayAdapter);

        final Runnable run=new Runnable() {
            @Override
            public void run() {
                arrayAdapter.notifyDataSetChanged();
            }
        };
        btnaddentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entry=addentry.getText().toString();
                entrylist.clear();
                databaseHelper.add_entry(entry);
                Cursor cursor=databaseHelper.get_entry();
                while (cursor.moveToNext())
                {
                    entrylist.add(cursor.getString(0));
                }
                runOnUiThread(run);
                addentry.setText("");
            }

        });

        entrylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(EditVoiceEntry.this);
                builder.setMessage(R.string.deleteEntry);
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int number) {
                        String removeentry=entrylist.get(i).toString();
                        databaseHelper.delete_entry(removeentry);
                        entrylist.clear();
                        Cursor cursor=databaseHelper.get_entry();
                        while (cursor.moveToNext())
                        {
                            entrylist.add(cursor.getString(0));
                        }
                        runOnUiThread(run);
                    }
                });

                builder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                final AlertDialog alertDialog=builder.create();
                alertDialog.show();

            }
        });
        voicetoggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    databaseHelper.voice_toggle("1");
                }
                else
                {
                    databaseHelper.voice_toggle("0");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.help:
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setMessage(R.string.EntryHelp);
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

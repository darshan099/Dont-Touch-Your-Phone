package com.example.darshanpc.focus;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class ClassSpeak {
    TextToSpeech tts;
    public ClassSpeak(Context context){
        tts=new TextToSpeech(context,new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR)
                {
                    tts.setLanguage(Locale.UK);
                }
            }
        });
    }
    public void Speak(final String speech)
    {
        tts.speak(speech,TextToSpeech.QUEUE_FLUSH,null);
    }
}

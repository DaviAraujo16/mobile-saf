package br.senai.sp.agendacontatos.conversores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Imagem {

    public static Bitmap arrayToBitmap(byte[]imageArray){
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageArray,0,imageArray.length);
        return bitmap;
    }
}

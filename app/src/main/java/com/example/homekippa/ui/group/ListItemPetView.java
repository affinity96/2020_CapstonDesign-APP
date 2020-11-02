//package com.example.homekippa.ui.group;
//
//import android.content.Context;
//import android.text.Layout;
//import android.view.LayoutInflater;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import com.example.homekippa.R;
//
//
//public class ListItemPetView extends LinearLayout {
//    TextView petName;
//    ImageView petImage;
//
//    public ListItemPetView(Context context){
//        super(context);
//        init(context);
//    }
//
//    private void init(Context context) {
//        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.listitem_pet, this, true);
//        petName=findViewById(R.id.listitem_PetName);
//        petImage=findViewById(R.id.listitem_PetIamge);
//    }
//    public void setPetName(String name){
//        petName.setText(name);
//    }
//    public void setPetImage(int resID){
//        petImage.setImageResource(resID);
//    }
//}

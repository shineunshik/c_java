package com.example.cj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Video extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    TextView text1,text2,text3;
    RecyclerView list;
    ArrayList<Ob_List> arraylist;
    DatabaseReference databaseReference_video;
    RecyclerView.Adapter adapter;

    DatabaseReference databaseReference_p;

    String value_p;
    TextView check;

    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference_auto;
    String value1,value2,value3,value4;

    Dialog dialog;
    TextView power;

    TextView option;

    String mt;
    DatabaseReference databaseReference_motion;
    TextView stop;
    TextView all_delete;
    long backKeyPressedTime = 0; //뒤로가기 버튼을 누른 시간

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);


        dialog = new Dialog(Video.this);  //다이어로그 초기화
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 제거
        dialog.setContentView(R.layout.power_dialog);
        dialog.setCanceledOnTouchOutside(false); //다이얼로그 바깥화면 터치 비활성화 코드
        //    dialog.setCancelable(false); //뒤로가기 비활성화
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //투명
        power = dialog.findViewById(R.id.power);
        all_delete = (TextView)findViewById(R.id.all_delete);
        stop = (TextView)findViewById(R.id.stop);
        stop.setVisibility(View.GONE);
        check = (TextView)findViewById(R.id.check);
        check.setVisibility(View.INVISIBLE);
        text1 = (TextView)findViewById(R.id.text1);
        text2 = (TextView)findViewById(R.id.text2);
        text3  = (TextView)findViewById(R.id.text3);
        option = (TextView)findViewById(R.id.option);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Video.this,Option.class);
                startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
        databaseReference1 =database.getReference("system").child("stop").child("power");
        databaseReference2 =database.getReference("system").child("stop").child("raspi");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    value1 = snapshot.getValue().toString();
                }
                catch (NullPointerException nullPointerException){


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    value2 = snapshot.getValue().toString();

                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        list = (RecyclerView)findViewById(R.id.list1);
        list.setLayoutManager(new GridLayoutManager(this,3));
        list.setHasFixedSize(true);
        arraylist = new ArrayList<>();
        databaseReference_video = database.getReference("video").child("list");
        databaseReference_video.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    arraylist.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        arraylist.add(dataSnapshot.getValue(Ob_List.class));
                    }

                    adapter.notifyDataSetChanged();

                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new CustomAdapter_Video(arraylist,Video.this);
        list.setAdapter(adapter);



        databaseReference_p = database.getReference("photo").child("photo").child("power");
        databaseReference_p.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    value_p = snapshot.getValue().toString();

                    //동영상
                    database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
                    databaseReference = database.getReference("video").child("video").child("power");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                value3 = snapshot.getValue().toString();
                                text1.setText(value3);

                                databaseReference_auto =database.getReference("video_auto").child("video_auto").child("power");
                                databaseReference_auto.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        try {
                                            value4 = snapshot.getValue().toString();
                                            text2.setText(value4);



                                            databaseReference_motion = database.getReference("motion").child("motion").child("power");
                                            databaseReference_motion.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    try {
                                                        mt = snapshot.getValue().toString();
                                                        text3.setText(mt);

                                                        if (value3.equals("ON")||value4.equals("ON")||mt.equals("ON")){
                                                            stop.setVisibility(View.VISIBLE);
                                                            text1.setVisibility(View.GONE);
                                                            text2.setVisibility(View.GONE);
                                                            text3.setVisibility(View.GONE);
                                                        }
                                                        else if(value3.equals("OFF")&&value4.equals("OFF")&&mt.equals("OFF")){
                                                            stop.setVisibility(View.GONE);
                                                            text1.setVisibility(View.VISIBLE);
                                                            text2.setVisibility(View.VISIBLE);
                                                            text3.setVisibility(View.VISIBLE);
                                                        }

                                                        stop.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                databaseReference.setValue("OFF");
                                                                databaseReference_auto.setValue("OFF");
                                                                databaseReference_motion.setValue("OFF");
                                                            }
                                                        });

                                                        all_delete.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if(System.currentTimeMillis() > backKeyPressedTime + 2000){
                                                                    backKeyPressedTime = System.currentTimeMillis();
                                                                    Toast.makeText(Video.this, "한 번 더 누르면 모두 삭제됩니다.", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                                //한 번의 뒤로가기 버튼이 눌린 후 0~2초 사이에 한 번더 눌리게 되면 현재 엑티비티를 호출
                                                                if(System.currentTimeMillis() <= backKeyPressedTime + 2000){

                                                                    databaseReference_video.removeValue();
                                                                    Toast.makeText(Video.this, "모두 삭제 완료", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });

                                                        //영상 촬영 중 바
                                                        if(value4.equals("ON")||value3.equals("ON")||mt.equals("ON"))
                                                        {
                                                            check.setVisibility(View.VISIBLE);
                                                        }
                                                        else if(value4.equals("OFF")||value3.equals("OFF")||mt.equals("OFF")){
                                                            check.setVisibility(View.INVISIBLE);
                                                        }

                                                        //동작 감지 모드
                                                        text3.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                //블랙박스 연결확인
                                                                if (value1.equals(value2))
                                                                {
                                                                    if (value_p.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Video.this, "사진 촬영 중에는 녹화 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else if(value4.equals("ON")||value3.equals("ON")){
                                                                        Toast.makeText(Video.this, "녹화 중에는 동작 감지 불가", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                    else{
                                                                        if(mt.equals("OFF"))
                                                                        {
                                                                            Toast.makeText(Video.this, "동작 감지 모드 시작", Toast.LENGTH_SHORT).show();
                                                                            databaseReference_motion.setValue("ON");
                                                                        }
                                                                    }

                                                                    if (mt.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Video.this, "잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }

                                                                else{
                                                                    dialog.show();
                                                                    power.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            if (value1.equals(value2)){
                                                                                dialog.dismiss();
                                                                                Toast.makeText(Video.this, "연결 성공", Toast.LENGTH_SHORT).show();

                                                                            }
                                                                            else {
                                                                                Toast.makeText(Video.this, "블랙박스 전원이 꺼져있습니다.", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                        }

                                                                    });
                                                                    Toast.makeText(Video.this, "블랙박스를 연결해 주세요.", Toast.LENGTH_SHORT).show();
                                                                }



                                                            }
                                                        });




                                                        //일반 동영상
                                                        text1.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                //블랙박스 연결확인
                                                                if (value1.equals(value2))
                                                                {
                                                                    if (value_p.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Video.this, "사진 촬영 중에는 영상 녹화 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else if(value4.equals("ON")){
                                                                        Toast.makeText(Video.this, "자동 녹화 중에는 일반 녹화 불가", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                    else if(mt.equals("ON")){
                                                                        Toast.makeText(Video.this, "동작 감지 모드 중에는 녹화 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else{
                                                                        if(value3.equals("OFF"))
                                                                        {
                                                                            Toast.makeText(Video.this, "촬영 시작", Toast.LENGTH_SHORT).show();
                                                                            databaseReference.setValue("ON");
                                                                        }
                                                                    }

                                                                    if (value3.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Video.this, "잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }

                                                                else{
                                                                    dialog.show();
                                                                    power.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            if (value1.equals(value2)){
                                                                                dialog.dismiss();
                                                                                Toast.makeText(Video.this, "연결 성공", Toast.LENGTH_SHORT).show();

                                                                            }
                                                                            else {
                                                                                Toast.makeText(Video.this, "블랙박스 전원이 꺼져있습니다.", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                        }

                                                                    });
                                                                    Toast.makeText(Video.this, "블랙박스를 연결해 주세요.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });




                                                        //자동 동영상
                                                        text2.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                //블랙박스 연결확인
                                                                if (value1.equals(value2))
                                                                {
                                                                    if (value_p.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Video.this, "사진 촬영 중에는 영상 녹화 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else if(value3.equals("ON")){
                                                                        Toast.makeText(Video.this, "일반 녹화 중에는 자동 녹화 불가", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                    else if(mt.equals("ON")){
                                                                        Toast.makeText(Video.this, "동작 감지 모드 중에는 녹화 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else{
                                                                        if(value4.equals("OFF"))
                                                                        {
                                                                            Toast.makeText(Video.this, "촬영 시작", Toast.LENGTH_SHORT).show();
                                                                            databaseReference_auto.setValue("ON");
                                                                        }
                                                                    }

                                                                    if (value4.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Video.this, "잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }

                                                                else{
                                                                    dialog.show();
                                                                    power.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            if (value1.equals(value2)){
                                                                                dialog.dismiss();
                                                                                Toast.makeText(Video.this, "연결 성공", Toast.LENGTH_SHORT).show();

                                                                            }
                                                                            else {
                                                                                Toast.makeText(Video.this, "블랙박스 전원이 꺼져있습니다.", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                        }

                                                                    });
                                                                    Toast.makeText(Video.this, "블랙박스를 연결해 주세요.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });








                                                    }
                                                    catch (NullPointerException nullPointerException){


                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });






                                        }
                                        catch (NullPointerException nullPointerException){
                                            databaseReference_auto.setValue("OFF");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });





                            }
                            catch (NullPointerException nullPointerException){
                                databaseReference.setValue("OFF");
                            }
                            catch (RuntimeException runtimeException){
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });






                }
                catch (NullPointerException nullPointerException){

                }
                catch (RuntimeException runtimeException){
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });




    }
}
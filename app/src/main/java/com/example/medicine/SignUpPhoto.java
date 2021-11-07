package com.example.medicine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class SignUpPhoto extends Activity implements View.OnClickListener{

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

    private Uri mImageCaptureUri;
    private ImageView iv_UserPhoto;

    private int id_view;

    File imageFile;
    String fileName = "img_"+System.currentTimeMillis();
    String fileFormat = ".jpg";
    String filePath = "";
    String dirPath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_photo);

        iv_UserPhoto = this.findViewById(R.id.user_image);
        Button btn_agreeJoin = this.findViewById(R.id.btn_UploadPicture);
        btn_agreeJoin.setOnClickListener(this);

        File storageDir = new File(this.getExternalFilesDir(null) + "/images/");
        if(!storageDir.exists()){
            storageDir.mkdir();
            this.dirPath = storageDir.getAbsolutePath();
        }
        if(storageDir.exists()){
            Log.d("storage exists", "true");
        }
        Log.d("storage URI", dirPath);

        imageFile = new File(storageDir, fileName + fileFormat);
        Log.d("imageFile URI" ,imageFile.getAbsolutePath());
        filePath = imageFile.getAbsolutePath();


    }

    @Override
    public void onClick(View v) {

        id_view = v.getId();

        if(v.getId() == R.id.btn_signupfinish) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("filePath", filePath);
            setResult(RESULT_OK, resultIntent);
            finish();
            Toast.makeText(this, "사진생성이 완료되었습니다.", Toast.LENGTH_SHORT).show();

        }else if(v.getId() == R.id.btn_UploadPicture) {

            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakePhotoAction();
                }
            };

            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakeAlbumAction();
                }
            };

            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            new AlertDialog.Builder(this)
                    .setTitle("업로드할 이미지 선택")
                    .setPositiveButton("사진촬영", cameraListener)
                    .setNeutralButton("앨범선택", albumListener)
                    .setNegativeButton("취소", cancelListener)
                    .show();
        }
    }

    /***카메라에서 사진 촬영*/
    public void doTakePhotoAction() {   // 카메라 촬영 후 이미지 가져오기
       // PhotoCaptureActivity p = new PhotoCaptureActivity();
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // 임시로 사용할 파일의 경로를 생성
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                mImageCaptureUri = FileProvider.getUriForFile(this, this.getPackageName() + ".fileprovider", imageFile);
            }else{
                mImageCaptureUri = Uri.fromFile(imageFile);
            }

            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    /**앨범에서 이미지 가져오기*/
    public void doTakeAlbumAction() {   // 앨범에서 이미지 가져오기
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean flag = true;
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode != RESULT_OK)
            return;

        switch(requestCode) {
            case PICK_FROM_ALBUM: {
                mImageCaptureUri = data.getData();
                Log.d("album uri",mImageCaptureUri.getPath());
                flag = false;
            }
            case PICK_FROM_CAMERA: {
                // 리사이즈 이후 크롭 세팅
               Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");
                Uri imageUri = mImageCaptureUri;
                Log.d("capture URI", mImageCaptureUri.getPath());

                if(flag){
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), imageBitmap, fileName + fileFormat, null);
                        imageUri = Uri.parse(path);
                        intent.setDataAndType(imageUri, "image/*");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(imageUri != null){
                    // CROP할 이미지를 200*200 크기로 저장
                    intent.putExtra("crop", "true");
                    intent.putExtra("outputX", 200); // CROP한 이미지의 x축 크기
                    intent.putExtra("outputY", 200); // CROP한 이미지의 y축 크기
                    intent.putExtra("aspectX", 1); // CROP 박스의 X축 비율
                    intent.putExtra("aspectY", 1); // CROP 박스의 Y축 비율
                    intent.putExtra("scale", true);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, CROP_FROM_iMAGE);
                }

                break;
            }

            case CROP_FROM_iMAGE: {
                // 크롭이 된 이후의 이미지
                if(resultCode != RESULT_OK) {
                    return;
                }

                final Bundle extras = data.getExtras();

                if(extras != null){
                    Bitmap photo = extras.getParcelable("data"); // CROP된 BITMAP
                    iv_UserPhoto.setImageBitmap(photo); // 레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌
                    storeCropImage(photo, filePath); // CROP된 이미지를 외부저장소, 앨범에 저장한다.
                    break;
                }
            }
        }
    }

    /*
    * Bitmap을 저장하는 부분
    */
    private void storeCropImage(Bitmap bitmap, String filePath) {

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            // sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(copyFile)));

            this.filePath = copyFile.getAbsolutePath();
            Log.d("copyFile uri", this.filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

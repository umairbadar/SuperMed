package managment.protege.supermed.Fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.R;
import managment.protege.supermed.Tools.GlobalHelper;

import static android.app.Activity.RESULT_OK;


public class UploadPriscribtionFragment extends Fragment {

    View view;

    private Button btn_gallery, btn_submit, btn_camera, btn_whatsapp;
    private ImageView img;
    final int CODE_GALLERY_REQUEST = 999;

    private static final int REQUEST_WRITE_PERMISSION = 786;
    Bitmap bitmap = null;
    int a = 0;

    public UploadPriscribtionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        view = inflater.inflate(R.layout.fragment_upload_priscribtion, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.upload_prescription), view);

        btn_gallery = view.findViewById(R.id.btn_gallery);
        btn_whatsapp = view.findViewById(R.id.btn_whatsapp);
        btn_camera = view.findViewById(R.id.btn_camera);
        btn_submit = view.findViewById(R.id.btn_submit);

        img = view.findViewById(R.id.img);

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = 0;
                requestPermission();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bitmap != null) {
                    sendRequest();
                } else {
                    Toast.makeText(getContext(), "Please select image first",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CODE_GALLERY_REQUEST);
                } else {
                    a = 1;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                }
            }
        });

        btn_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null) {
                    String toNumber = "923102911911"; // contains spaces.
                    toNumber = toNumber.replace("+", "").replace(" ", "");
                    onClickApp("com.whatsapp", bitmap, toNumber);
                } else {
                    Toast.makeText(getContext(), "Please select image first",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (a == 1) {
            bitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bitmap);
        } else {

            if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
                Uri filePath = data.getData();

                try {
                    InputStream inputStream = getContext().getContentResolver().openInputStream(filePath);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    img.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            openFilePicker();
        }
    }

    private void openFilePicker() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
    }

    private void sendRequest() {
        Main_Apps.hud.show();
        String URL = Register.Base_URL + "prescription";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            String message = jsonObject.getString("message");
                            if (status) {
                                Main_Apps.hud.dismiss();
                                Toast.makeText(getContext(), message,
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Main_Apps.hud.dismiss();
                                Toast.makeText(getContext(), message,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Main_Apps.hud.dismiss();
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String imageData = imagetoString(bitmap);
                map.put("userId", GlobalHelper.getUserProfile(getContext()).getProfile().getId());
                map.put("upload_prescription", "data:image/jpeg;base64," + imageData);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(req);
    }

    private String imagetoString(Bitmap bitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        ;
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void onClickApp(String pack, Bitmap bitmap, String phone) {
        PackageManager pm = getActivity().getPackageManager();
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Title", null);
            Uri imageUri = Uri.parse(path);

            @SuppressWarnings("unused")
            PackageInfo info = pm.getPackageInfo(pack, PackageManager.GET_META_DATA);

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("image/*");
            waIntent.setPackage(pack);
            waIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
            waIntent.putExtra(Intent.EXTRA_TEXT, pack);
            waIntent.putExtra("jid", phone + "@s.whatsapp.net");
            getActivity().startActivity(waIntent);
        } catch (Exception e) {
            Log.e("Error on sharing", e + " ");
            Toast.makeText(getContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
        }
    }
}

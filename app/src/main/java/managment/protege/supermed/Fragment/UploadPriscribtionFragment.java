package managment.protege.supermed.Fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.UploadPrescResponse;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class UploadPriscribtionFragment extends Fragment {

    View view;
    Button uploadPrescription, submit_presc, wapp, btn_camera, btn_gallery;
    ImageView imageup;
    TextView toolbar_text;

    File file;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static int RESULT_LOAD_IMAGE = 1;

    public UploadPriscribtionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        view = inflater.inflate(R.layout.fragment_upload_priscribtion, container, false);
        initialize();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.upload_prescription), view);

        onclick();
        return view;
    }

    private void onclick() {

        imageup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalHelper.getUserProfile(getContext()).getProfile().getFirstName().trim().toLowerCase().equals("guest")) {
                    Main_Apps.getMainActivity().forgotPasswordDialog(getContext());
                } else {
                    PickImageDialog.build(new PickSetup())
                            .setOnPickResult(new IPickResult() {
                                @Override
                                public void onPickResult(PickResult r) {
                                    imageup.setImageBitmap(r.getBitmap());
                                    file = new File(r.getPath());
                                    //TODO: do what you have to...

                                }
                            })
                            .setOnPickCancel(new IPickCancel() {
                                @Override
                                public void onCancelClick() {
                                    //TODO: do what you have to if user clicked cancel

                                }
                            }).show(getActivity().getSupportFragmentManager());
                }
            }
        });
        wapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String toNumber = "923102911911"; // contains spaces.
                toNumber = toNumber.replace("+", "").replace(" ", "");

                if (file != null) {

                    whatsapp(getActivity(), toNumber, file);

                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Upload Prescription for Med Pharma");
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setPackage("com.whatsapp");
                    sendIntent.setType("image/png");

                    if (sendIntent.resolveActivity(getActivity().getPackageManager()) == null) {
                        Toast.makeText(getContext(), "Whatsapp not installed.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        getContext().startActivity(sendIntent);
                    }

                } else {
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Upload Prescription for Med Pharma");
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setPackage("com.whatsapp");
                    sendIntent.setType("text/plain");


                    if (sendIntent.resolveActivity(getActivity().getPackageManager()) == null) {
                        Toast.makeText(getContext(), "Whatsapp not installed.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        startActivity(sendIntent);
                    }

                    if (file == null) {
                        Toasty.error(getContext(), "Select Image First", Toast.LENGTH_SHORT, true).show();
                    } else {
                        UploadPresc(GlobalHelper.getUserProfile(getContext()).getProfile().getId(), file);
                    }
                }
            }
        });

        submit_presc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalHelper.getUserProfile(getContext()).getProfile().getFirstName().trim().toLowerCase().equals("guest")) {
                    Main_Apps.getMainActivity().forgotPasswordDialog(getContext());

                } else {
                    if (file == null) {
                        Toasty.error(getContext(), "Select Image First", Toast.LENGTH_SHORT, true).show();
                    } else {
                        UploadPresc(GlobalHelper.getUserProfile(getContext()).getProfile().getId(), file);
                    }
                }
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageup.setImageBitmap(photo);
            Uri tempUri = getImageUri(getContext(), photo);
            file = new File(getRealPathFromURI(tempUri));
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageup.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            file = new File(picturePath);

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private void initialize() {
        toolbar_text = (TextView) view.findViewById(R.id.toolbar_text);
        wapp = (Button) view.findViewById(R.id.wapp);
        imageup = (ImageView) view.findViewById(R.id.imageup);
        //uploadPrescription = (Button) view.findViewById(R.id.uploadimage);
        submit_presc = (Button) view.findViewById(R.id.submit_presc);
        btn_camera = view.findViewById(R.id.btn_camera);
        btn_gallery = view.findViewById(R.id.btn_gallery);
    }

    public void UploadPresc(final String userid, File file) {
        //  file = new File(file.getPath());
        Main_Apps.hud.show();

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        Log.e("FILE ", file.getPath());
        Log.e("filename", file.getName());
        Log.e("request body:", String.valueOf(reqFile));
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        Log.e("body", String.valueOf(body));
        API api = RetrofitAdapter.createAPI();
        Call<UploadPrescResponse> uploadPrescResponseCall = api.UPLOAD_PRESC_RESPONSE_CALL(userid, body);
        uploadPrescResponseCall.enqueue(new Callback<UploadPrescResponse>() {
            @Override
            public void onResponse(Call<UploadPrescResponse> call, Response<UploadPrescResponse> response) {

                if (response != null) {
                    Main_Apps.hud.dismiss();
                    if (response.body().getStatus()) {
                        Toasty.success(getContext(), "Prescription Uploaded!", Toast.LENGTH_SHORT, true).show();

                    } else {
                        Toasty.error(getContext(), "Error: " + response.body().getMessage(), Toast.LENGTH_SHORT, true).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<UploadPrescResponse> call, Throwable t) {
                Main_Apps.hud.dismiss();

                Log.e("Upload Prescription", "error" + t.getMessage());
            }
        });
    }

    @SuppressLint("NewApi")
    public static void whatsapp(Activity activity, String phone, File file) {
        Uri imgUri = Uri.parse(file.getAbsolutePath());
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra("jid", phone + "@s.whatsapp.net");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Upload Prescription for Med Pharma");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        whatsappIntent.setType("image/jpeg");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            activity.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }

}

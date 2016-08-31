package de.family_networking.de.family_networking.commons;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lars Nitzsche on 21.08.2016.
 */
public class CustomWebChromeClient extends WebChromeClient
{
    private AppCompatActivity activity;

    private ValueCallback< Uri > mUploadMessage;
    private ValueCallback< Uri[] > mUploadMessageForAndroid5;

    private final static int FILECHOOSER_RESULTCODE = 1;
    public static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;

    private static final int REQUEST_GET_THE_THUMBNAIL = 4000;
    public static final int REQUEST_CAMERA = 1;

    private String mCurrentPhotoPath;

    private static CharSequence[] items = {"Pictures", "Camera"};

    public CustomWebChromeClient( AppCompatActivity activity )
    {
        this.activity = activity;
    }

    //3.0++
    public void openFileChooser( ValueCallback< android.net.Uri > uploadMsg, String acceptType )
    {
        openFileChooserImpl(uploadMsg);
    }

    //3.0--
    public void openFileChooser( ValueCallback< android.net.Uri > uploadMsg )
    {
        openFileChooserImpl(uploadMsg);
    }

    public void openFileChooser( ValueCallback< android.net.Uri > uploadMsg, String acceptType, String capture )
    {
        openFileChooserImpl(uploadMsg);
    }

    // For Android > 5.0
    public boolean onShowFileChooser( WebView webView, ValueCallback< android.net.Uri[] > uploadMsg, WebChromeClient.FileChooserParams fileChooserParams )
    {
        openFileChooserImplForAndroid5(uploadMsg);
        return true;
    }

    public void onActivityResult( int requestCode, int resultCode, Intent intent )
    {
        if ( requestCode == FILECHOOSER_RESULTCODE )
        {
            if ( null == mUploadMessage )
                return;
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        }
        else if ( requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5 )
        {
            if ( null == mUploadMessageForAndroid5 )
                return;
            Uri result;

            if ( intent == null || resultCode != Activity.RESULT_OK )
            {
                result = null;
            }
            else
            {
                result = intent.getData();
            }

            if ( result != null )
            {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            }
            else
            {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
        else if ( requestCode == REQUEST_GET_THE_THUMBNAIL )
        {
            if ( resultCode == Activity.RESULT_OK )
            {
                File file = new File(mCurrentPhotoPath);
                Uri localUri = Uri.fromFile(file);
                Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                activity.sendBroadcast(localIntent);

                Uri result = Uri.fromFile(file);
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
                mUploadMessageForAndroid5 = null;
            }
            else
            {
                if ( mCurrentPhotoPath != null )
                {
                    File file = new File(mCurrentPhotoPath);
                    if ( file.exists() )
                    {
                        file.delete();
                    }
                }
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
                mUploadMessageForAndroid5 = null;
            }
        }
    }

    public void onRequestPermissionsResult( int requestCode, String permissions[], int[] grantResults )
    {
        switch ( requestCode )
        {
            case REQUEST_CAMERA:
            {
                // If request is cancelled, the result arrays are empty.
                if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED )
                {
                    dispatchTakePictureIntent();
                }
                else
                {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(activity.getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void openFileChooserImpl( ValueCallback< Uri > uploadMsg )
    {
        mUploadMessage = uploadMsg;
        new AlertDialog.Builder(activity).setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
                if ( items[which].equals(items[0]) )
                {
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");
                    activity.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
                }
                else if ( items[which].equals(items[1]) )
                {
                    dispatchTakePictureIntent();
                }
                dialog.dismiss();
            }
        })
                .setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel( DialogInterface dialog )
                    {
                        mUploadMessage = null;
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void openFileChooserImplForAndroid5( ValueCallback< Uri[] > uploadMsg )
    {
        mUploadMessageForAndroid5 = uploadMsg;

        new AlertDialog.Builder(activity).setItems(items, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        if ( items[which].equals(items[0]) )
                        {
                            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                            contentSelectionIntent.setType("image/*");

                            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

                            activity.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
                        }
                        else if ( items[which].equals(items[1]) )
                        {
                            dispatchTakePictureIntent();
                        }
                        dialog.dismiss();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel( DialogInterface dialog )
                    {
                        //important to return new Uri[]{}, when nothing to do. This can slove input file wrok for once.
                        //InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.
                        mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
                        mUploadMessageForAndroid5 = null;
                        dialog.dismiss();
                    }
                }).show();
    }

    private void dispatchTakePictureIntent()
    {
        // Check permission for CAMERA
        if ( ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED )
        {
            // Check Permissions Now
            // Callback onRequestPermissionsResult interceptado na Activity MainActivity
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
        else
        {
            // permission has been granted, continue as usual
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if ( takePictureIntent.resolveActivity(activity.getPackageManager()) != null )
            {
                Uri imageUri = null;
                try
                {
                    imageUri = Uri.fromFile(createImageFile());
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
                //temp sd card file
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                activity.startActivityForResult(takePictureIntent, REQUEST_GET_THE_THUMBNAIL);
            }
        }

    }


    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/family.networking/");
        if ( !storageDir.exists() )
        {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}

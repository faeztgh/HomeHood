package ir.faez.assignment2.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import ir.faez.assignment2.R;

public class FileAccessHelper {
    //Instance Fields
    public static final int EXTERNAL_STORAGE_NOT_READY_ERROR_CODE = 1;
    public static final int FILE_DOES_NOT_EXIST_ERROR_CODE = 2;
    public static final int FILE_READ_ERROR_CODE = 3;
    public static final int FILE_WRITE_ERROR_CODE = 4;
    public static final int FILE_PATH_NOT_PROVIDED = 5;
    @SuppressLint("StaticFieldLeak")
    private static FileAccessHelper instance = null;
    private Context context;

    // Constructor
    public FileAccessHelper(Context context) {
        this.context = context;
    }

    // make it singleton
    public static FileAccessHelper getInstance(Context context) {
        if (instance == null) {
            instance = new FileAccessHelper(context);
        }
        return instance;
    }

    // Check for external storage availability
    private boolean isExternalStorageAvailable() {
        return (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED));
    }


    public Pair<String, Integer> readFile(Uri fileUri) {
        if (isExternalStorageAvailable()) {
            return new Pair<>(null, EXTERNAL_STORAGE_NOT_READY_ERROR_CODE);
        }

        StringBuilder wholeText = new StringBuilder();

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                wholeText.append(line).append("\n");
            }

            reader.close();
            assert inputStream != null;
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new Pair<>(null, FILE_DOES_NOT_EXIST_ERROR_CODE);
        } catch (IOException e) {
            e.printStackTrace();
            return new Pair<>(null, FILE_READ_ERROR_CODE);
        }
        return new Pair<>(wholeText.toString(), 0);
    }


    public int writeToFile(Uri fileUri, String contents) {
        if (isExternalStorageAvailable()) {
            return EXTERNAL_STORAGE_NOT_READY_ERROR_CODE;
        }

        try {
            ParcelFileDescriptor pdf = context.getContentResolver().openFileDescriptor(fileUri, "w");
            assert pdf != null;
            FileOutputStream outputStream = new FileOutputStream(pdf.getFileDescriptor());
            outputStream.write(contents.getBytes(StandardCharsets.UTF_8));

            outputStream.close();
            pdf.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return FILE_DOES_NOT_EXIST_ERROR_CODE;
        } catch (IOException e) {
            e.printStackTrace();
            return FILE_WRITE_ERROR_CODE;
        }
        return 0;
    }


    // for handling error messages
    public String getFileErrorMessage(int errorCode) {
        String errorMessage;
        switch (errorCode) {
            case EXTERNAL_STORAGE_NOT_READY_ERROR_CODE:
                errorMessage = String.valueOf(R.string.externalStorageNotReadyToUse);
                break;
            case FILE_DOES_NOT_EXIST_ERROR_CODE:
                errorMessage = String.valueOf(R.string.fileNotExist);
                break;
            case FILE_READ_ERROR_CODE:
                errorMessage = String.valueOf(R.string.problemToReadFromFile);
                break;
            case FILE_WRITE_ERROR_CODE:
                errorMessage = String.valueOf(R.string.problemToWriteOnFile);
                break;
            case FILE_PATH_NOT_PROVIDED:
                errorMessage = String.valueOf(R.string.filePathNotFound);
                break;
            default:
                errorMessage = String.valueOf(R.string.errorMessageNotExist);
        }
        return errorMessage;
    }
}


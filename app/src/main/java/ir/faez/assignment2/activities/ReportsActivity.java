package ir.faez.assignment2.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import ir.faez.assignment2.R;
import ir.faez.assignment2.data.model.Expense;
import ir.faez.assignment2.databinding.ActivityReportsBinding;
import ir.faez.assignment2.utils.FileAccessHelper;

public class ReportsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int READ_PERMISSION_REQUEST_CODE = 1;
    private static final int READ_FILE_PICKER_REQUEST_CODE = 1;
    private static final int WRITE_PERMISSION_REQUEST_CODE = 2;
    private static final int WRITE_FILE_PICKER_REQUEST_CODE = 2;
    private ActivityReportsBinding binding;
    private FileAccessHelper fileAccessHelper = null;
    private Uri fileUri = null;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    // initializing things that should be handled at start
    private void init() {
        // initializing binding
        binding = ActivityReportsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        fileAccessHelper = FileAccessHelper.getInstance(getApplicationContext());


        invokeOnClickListeners();
    }

    // invoke OnClickListeners
    private void invokeOnClickListeners() {
        binding.reportsBackIv.setOnClickListener(this);
        binding.exportBtn.setOnClickListener(this);
        binding.importBtn.setOnClickListener(this);

    }

    // ---------------------------------- Import & Export ------------------------------------------


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ------------------------- read
        if (requestCode == READ_FILE_PICKER_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, R.string.filePathNotSet, Toast.LENGTH_SHORT).show();
                return;
            }

            fileUri = (data != null) ? data.getData() : null;
            if (fileUri == null) {
                Toast.makeText(this, R.string.filePathNotSet, Toast.LENGTH_SHORT).show();
                return;
            }
            checkReadPermission();
        } else if (requestCode == WRITE_FILE_PICKER_REQUEST_CODE) {

            //---------------------------- write
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, R.string.filePathNotSet, Toast.LENGTH_SHORT).show();
                return;
            }

            fileUri = (data != null) ? data.getData() : null;
            if (fileUri == null) {
                Toast.makeText(this, R.string.filePathNotSet, Toast.LENGTH_SHORT).show();
                return;
            }
            checkWritePermission();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // ---------------------- read
        if (requestCode != READ_PERMISSION_REQUEST_CODE) {
            return;
        }
        if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            readFileProcess();
        } else {
            Toast.makeText(this, R.string.youAreNotPermittedToReadThisFile, Toast.LENGTH_SHORT).show();
        }

        // ---------------------- write

    }


    // ---------------------------------- Export ---------------------------------------------------

    private void openFilePickerForWrite() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("plain/text");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.putExtra(Intent.EXTRA_TITLE, "report");
        startActivityForResult(intent, WRITE_FILE_PICKER_REQUEST_CODE);
    }


    private void checkWritePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            writeFileProcess();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST_CODE);
        }
    }


    private void writeFileProcess() {
        if (fileUri == null) {
            Toast.makeText(this, R.string.filePathNotSet, Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            // converting arrayList of expense to json string
            String content = gson.toJson(ExpensesActivity.getExpensesList());
            int errorCode = fileAccessHelper.writeToFile(fileUri, content);

            if (errorCode > 0) {
                String errorMessage = fileAccessHelper.getFileErrorMessage(errorCode);
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, R.string.exportingFileDoneSuccessfully, Toast.LENGTH_SHORT).show();

    }
    // ---------------------------------- Import ---------------------------------------------------

    private void openFilePickerForRead() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("plain/text");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent, READ_FILE_PICKER_REQUEST_CODE);
    }


    private void checkReadPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            readFileProcess();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_REQUEST_CODE);
        }
    }


    private void readFileProcess() {
        if (fileUri == null) {
            Toast.makeText(this, R.string.filePathNotSet, Toast.LENGTH_SHORT).show();
            return;
        }
        Pair<String, Integer> result = fileAccessHelper.readFile(fileUri);
        String fileContents = result.first;
        int errorCode = result.second;
        if (fileContents == null) {
            String errorMessage = fileAccessHelper.getFileErrorMessage(errorCode);
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            //converting arrayList of Expense from json in the file to and arrayList of Expense in app
            ArrayList<Expense> res = gson.fromJson(fileContents, new TypeToken<ArrayList<Expense>>() {
            }.getType());
            // for appending using addAll
            ExpensesActivity.getExpensesList().addAll(res);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, R.string.importingFileDoneSuccessfully, Toast.LENGTH_SHORT).show();
    }


    //----------------------------- Implementing View.OnclickListener ------------------------------

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.export_btn:
                openFilePickerForWrite();
                break;

            case R.id.import_btn:
                openFilePickerForRead();
                break;

            case R.id.reports_back_iv:
                finish();
                break;
            default:
                Toast.makeText(ReportsActivity.this,
                        R.string.inappropriateInput,
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }


}

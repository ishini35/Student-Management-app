package com.student.management;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditActivity extends AppCompatActivity {

    private DatabaseHelper  dbHelper;
    private boolean         isEditMode = false;
    private int             studentId  = -1;

    private EditText        etName, etEmail, etPhone;
    private TextInputLayout tilName, tilEmail, tilPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        dbHelper  = new DatabaseHelper(this);

        TextView tvTitle = findViewById(R.id.tv_form_title);
        etName   = findViewById(R.id.et_student_name);
        etEmail  = findViewById(R.id.et_student_email);
        etPhone  = findViewById(R.id.et_student_phone);
        tilName  = findViewById(R.id.til_name);
        tilEmail = findViewById(R.id.til_email);
        tilPhone = findViewById(R.id.til_phone);

        Button btnSave   = findViewById(R.id.btn_save);
        Button btnCancel = findViewById(R.id.btn_cancel);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("student_id")) {
            isEditMode = true;
            studentId  = extras.getInt("student_id");
            tvTitle.setText("Edit Student");
            etName.setText(extras.getString("student_name"));
            etEmail.setText(extras.getString("student_email"));
            etPhone.setText(extras.getString("student_phone"));
        } else {
            tvTitle.setText("Add Student");
        }

        btnSave.setOnClickListener(v -> saveStudent());
        btnCancel.setOnClickListener(v -> finish());
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void saveStudent() {
        tilName.setError(null);
        tilEmail.setError(null);
        tilPhone.setError(null);

        String name  = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        boolean valid = true;

        if (TextUtils.isEmpty(name)) {
            tilName.setError("Name is required"); valid = false;
        }
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email is required"); valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Enter a valid email"); valid = false;
        }
        if (TextUtils.isEmpty(phone)) {
            tilPhone.setError("Phone is required"); valid = false;
        } else if (phone.length() < 7) {
            tilPhone.setError("Enter a valid phone number"); valid = false;
        }

        if (!valid) return;

        Student student = new Student(name, email, phone);

        if (isEditMode) {
            student.setId(studentId);
            if (dbHelper.updateStudent(student) > 0) finish();
            else Snackbar.make(etName, "Update failed.", Snackbar.LENGTH_SHORT).show();
        } else {
            if (dbHelper.insertStudent(student) != -1) finish();
            else Snackbar.make(etName, "Save failed.", Snackbar.LENGTH_SHORT).show();
        }
    }
}

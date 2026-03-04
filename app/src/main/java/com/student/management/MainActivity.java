package com.student.management;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements StudentAdapter.OnStudentAction {

    private DatabaseHelper dbHelper;
    private StudentAdapter adapter;
    private List<Student>  allStudents = new ArrayList<>();

    private RecyclerView   recyclerView;
    private LinearLayout   tvEmptyState;
    private TextView       tvStudentCount;
    private EditText       etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper       = new DatabaseHelper(this);
        recyclerView   = findViewById(R.id.recycler_students);
        tvEmptyState   = findViewById(R.id.tv_empty_state);
        tvStudentCount = findViewById(R.id.tv_student_count);
        etSearch       = findViewById(R.id.et_search);

        ExtendedFloatingActionButton fab = findViewById(R.id.fab_add);

        adapter = new StudentAdapter(this, allStudents, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v ->
                startActivity(new Intent(this, AddEditActivity.class)));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                if (dy > 0) fab.shrink(); else fab.extend();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                filterStudents(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }

    private void loadStudents() {
        allStudents = dbHelper.getAllStudents();
        updateUI(allStudents);
    }

    private void filterStudents(String query) {
        if (query.isEmpty()) { updateUI(allStudents); return; }
        String lower = query.toLowerCase();
        List<Student> filtered = allStudents.stream()
                .filter(s -> s.getName().toLowerCase().contains(lower)
                        || s.getEmail().toLowerCase().contains(lower)
                        || s.getPhone().contains(query))
                .collect(Collectors.toList());
        updateUI(filtered);
    }

    private void updateUI(List<Student> list) {
        adapter.updateList(list);
        int count = list.size();
        tvStudentCount.setText(count + (count == 1 ? " student" : " students"));
        tvEmptyState.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onEdit(Student student) {
        Intent intent = new Intent(this, AddEditActivity.class);
        intent.putExtra("student_id",    student.getId());
        intent.putExtra("student_name",  student.getName());
        intent.putExtra("student_email", student.getEmail());
        intent.putExtra("student_phone", student.getPhone());
        startActivity(intent);
    }

    @Override
    public void onDelete(Student student) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Student")
                .setMessage("Remove " + student.getName() + "?")
                .setPositiveButton("Delete", (d, w) -> {
                    dbHelper.deleteStudent(student.getId());
                    loadStudents();
                    Snackbar.make(recyclerView,
                            student.getName() + " removed.", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
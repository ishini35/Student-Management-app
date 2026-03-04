package com.student.management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private final Context         context;
    private       List<Student>   studentList;
    private final OnStudentAction listener;

    public interface OnStudentAction {
        void onEdit(Student student);
        void onDelete(Student student);
    }

    public StudentAdapter(Context context, List<Student> studentList, OnStudentAction listener) {
        this.context     = context;
        this.studentList = studentList;
        this.listener    = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);

        holder.tvName.setText(student.getName());
        holder.tvEmail.setText(student.getEmail());
        holder.tvPhone.setText(student.getPhone());
        holder.tvAvatar.setText(getInitials(student.getName()));

        int[] avatarColors = context.getResources().getIntArray(R.array.avatar_colors);
        holder.tvAvatar.getBackground().setTint(avatarColors[position % avatarColors.length]);

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(student));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(student));
    }

    @Override
    public int getItemCount() { return studentList.size(); }

    public void updateList(List<Student> newList) {
        this.studentList = newList;
        notifyDataSetChanged();
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "?";
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2)
            return String.valueOf(parts[0].charAt(0)).toUpperCase()
                    + String.valueOf(parts[1].charAt(0)).toUpperCase();
        return String.valueOf(name.charAt(0)).toUpperCase();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView    tvAvatar, tvName, tvEmail, tvPhone;
        ImageButton btnEdit, btnDelete;

        StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAvatar  = itemView.findViewById(R.id.tv_avatar);
            tvName    = itemView.findViewById(R.id.tv_name);
            tvEmail   = itemView.findViewById(R.id.tv_email);
            tvPhone   = itemView.findViewById(R.id.tv_phone);
            btnEdit   = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}

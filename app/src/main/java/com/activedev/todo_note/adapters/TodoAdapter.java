package com.activedev.todo_note.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.activedev.todo_note.R;
import com.activedev.todo_note.model.Todo;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    Context context;
    OnItemClickListener mListener;
    private List<Todo> todoList;

    public TodoAdapter(List<Todo> todoList) {
        this.todoList = todoList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_details, parent, false);
        return new TodoViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.text.setText(todo.getText());
        holder.dueDate.setText(todo.getDueDate());
        if (todo.isFavored().equals("0"))
            holder.btnFavored.setFavorite(false);
        else holder.btnFavored.setFavorite(true);


    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onBtnClick(int position);
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        MaterialFavoriteButton btnFavored;
        TextView text;
        TextView dueDate;

        public TodoViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            text = itemView.findViewById(R.id.textTodo);
            dueDate = itemView.findViewById(R.id.dueData);
            btnFavored = itemView.findViewById((R.id.btnFavored));

        /*    itemView.setOnClickListener((view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                        listener.onItemClick(position);
                }
            }));*/

            btnFavored.setOnFavoriteChangeListener(
                    (buttonView, favorite) -> {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION)
                                listener.onBtnClick(position);
                        }

                    });
        }
    }
}

/*

class TodoAdapter(
    private val commends: List<Todo>,
    mContext: Context?
) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodoViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_details, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TodoViewHolder,
        position: Int
    ) {
        val todo: Todo = commends[position]
        holder.text.text = todo.text
        holder.dueDate.text = todo.dueDate
    }

    override fun getItemCount(): Int {
        return commends.size
    }

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text: TextView = itemView.findViewById(R.id.textTodo)
        var dueDate: TextView = itemView.findViewById(R.id.dueData)
        var favored:MaterialFavoriteButton = itemView.findViewById((R.id.btnFavored))


    }

}
*/
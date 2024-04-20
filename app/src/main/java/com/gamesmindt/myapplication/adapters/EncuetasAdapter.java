package com.gamesmindt.myapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gamesmindt.myapplication.Model.Survey;
import com.gamesmindt.myapplication.Model.Survey;
import com.gamesmindt.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EncuetasAdapter extends RecyclerView.Adapter<EncuetasAdapter.OfertaViewHolder> {

    private Context context;
    private List<Survey> listaEncuetas;

    public EncuetasAdapter(Context context, List<Survey> listaEncuetas) {
        this.context = context;
        this.listaEncuetas = listaEncuetas;
    }

    public interface OnClickListener {
        void onResponderClick(Integer id_encuesta);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener listener) {
        this.onClickListener = listener;
    }

    @NonNull
    @Override
    public OfertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_encuestas, parent, false);
        return new OfertaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfertaViewHolder holder, int position) {
        Survey encuesta = listaEncuetas.get(position);
        holder.bind(encuesta);

        holder.responderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onResponderClick(encuesta.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaEncuetas.size();
    }

    public void filterList(List<Survey> filteredList) {
        listaEncuetas = filteredList;
        notifyDataSetChanged();
    }

    public class OfertaViewHolder extends RecyclerView.ViewHolder {

        private TextView name_txt, desc_Text;
        private Button responderButton;

        public OfertaViewHolder(@NonNull View itemView) {
            super(itemView);
            name_txt = itemView.findViewById(R.id.name_txt);
            desc_Text = itemView.findViewById(R.id.desc_Text);
            responderButton = itemView.findViewById(R.id.responder_button);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Survey encuesta) {
            name_txt.setText(encuesta.getTitle());
            desc_Text.setText(encuesta.getDescription());
        }
    }
}
package com.gamesmindt.myapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gamesmindt.myapplication.Model.Postulacion;
import com.gamesmindt.myapplication.Model.Postulacion;
import com.gamesmindt.myapplication.R;

import java.util.List;

public class PostulacionesAdapter extends RecyclerView.Adapter<PostulacionesAdapter.PostulacionViewHolder> {

    private Context context;
    private List<Postulacion> PostulacionList;

    public PostulacionesAdapter(Context context, List<Postulacion> PostulacionS) {
        this.context = context;
        this.PostulacionList = PostulacionS;
    }

    public interface OnVerMasClickListener {
        void onVerMasClick(Integer id_Postulacion);

        void onDeleteClick(Integer id_Postulacion, String estado);
    }

    private OnVerMasClickListener verMasClickListener;

    public void setOnVerMasClickListener(OnVerMasClickListener listener) {
        this.verMasClickListener = listener;
    }

    @NonNull
    @Override
    public PostulacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_postulaciones, parent, false);
        return new PostulacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostulacionViewHolder holder, int position) {
        Postulacion Postulacion = PostulacionList.get(position);
        holder.bind(Postulacion);

        holder.verMasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verMasClickListener != null) {
                    verMasClickListener.onVerMasClick(Postulacion.getOfertaLaboral().getId());
                }
            }
        });

        holder.eliminarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verMasClickListener != null) {
                    verMasClickListener.onDeleteClick(Postulacion.getId(), Postulacion.getEstado());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return PostulacionList.size();
    }

    public void filterList(List<Postulacion> filteredList) {
        PostulacionList = filteredList;
        notifyDataSetChanged();
    }

    public class PostulacionViewHolder extends RecyclerView.ViewHolder {

        private TextView name_txt, cargoRe_Text, fechView, salario, estado;
        private ImageView empresaImg;
        private Button verMasButton, eliminarButton;

        public PostulacionViewHolder(@NonNull View itemView) {
            super(itemView);
            name_txt = itemView.findViewById(R.id.name_txt);
            cargoRe_Text = itemView.findViewById(R.id.cargoRe_Text);
            fechView = itemView.findViewById(R.id.fechView);
            empresaImg = itemView.findViewById(R.id.empresaImg);
            verMasButton = itemView.findViewById(R.id.ver_info_button);
            eliminarButton = itemView.findViewById(R.id.eliminar_button);
            salario = itemView.findViewById(R.id.salario_text);
            estado = itemView.findViewById(R.id.estado_text);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Postulacion Postulacion) {
            name_txt.setText(Postulacion.getOfertaLaboral().getEmpresa().getNombre());
            estado.setText("Estado: " + Postulacion.getEstado());
            salario.setText("Salario: $ " + Postulacion.getOfertaLaboral().getSalario());
            cargoRe_Text.setText(Postulacion.getOfertaLaboral().getCargo());
            String fechaPublicacion = Postulacion.getOfertaLaboral().getFechaPublicacion();

            if (Postulacion.getEstado().equals("CANCELADA_POR_GRADUADO")) {
                eliminarButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#35bc1e")));
                eliminarButton.setText("POSTULAR");
            } else {
                eliminarButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF2E2E")));
                eliminarButton.setText("CANCELAR");
            }

            if (fechaPublicacion != null) {
                fechView.setText("Fecha de Publicación: " + fechaPublicacion);
            } else {
                fechView.setText("Fecha no disponible");
            }

            String fotoBase64 = Postulacion.getOfertaLaboral().getFotoPortada();

            if (fotoBase64 != null) {
                Glide.with(context)
                        .load(fotoBase64)
                        .into(empresaImg);
            } else {
                System.out.println("No se pudo obtener la imagen");
            }
        }
    }
}
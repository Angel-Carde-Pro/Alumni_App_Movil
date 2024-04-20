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
import com.gamesmindt.myapplication.Model.OfertaLaboralDTO;
import com.gamesmindt.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OfertasAdapter extends RecyclerView.Adapter<OfertasAdapter.OfertaViewHolder> {

    private Context context;
    private List<OfertaLaboralDTO> listaOfertas;

    public OfertasAdapter(Context context, List<OfertaLaboralDTO> listaOfertas) {
        this.context = context;
        this.listaOfertas = listaOfertas;
    }

    public interface OnClickListener {
        void onVerMasClick(Integer id_Oferta);

        void onAplicarClick(Integer id_Oferta);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener listener) {
        this.onClickListener = listener;
    }

    @NonNull
    @Override
    public OfertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ofertas, parent, false);
        return new OfertaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfertaViewHolder holder, int position) {
        OfertaLaboralDTO oferta = listaOfertas.get(position);
        holder.bind(oferta);

        holder.verMasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onVerMasClick(oferta.getId());
                }
            }
        });

        holder.aplicarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onAplicarClick(oferta.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaOfertas.size();
    }

    public void filterList(List<OfertaLaboralDTO> filteredList) {
        listaOfertas = filteredList;
        notifyDataSetChanged();
    }

    public class OfertaViewHolder extends RecyclerView.ViewHolder {

        private TextView name_txt, cargo_Text, experiencia, tiempo, fechaCierre, salario;
        private ImageView empresaImg;
        private Button verMasButton, aplicarButton;

        public OfertaViewHolder(@NonNull View itemView) {
            super(itemView);
            name_txt = itemView.findViewById(R.id.name_txt);
            cargo_Text = itemView.findViewById(R.id.cargo_Text);
            fechaCierre = itemView.findViewById(R.id.fechaCierreView);
            empresaImg = itemView.findViewById(R.id.empresaImg);
            verMasButton = itemView.findViewById(R.id.ver_info_button);
            aplicarButton = itemView.findViewById(R.id.aplicar_button);
            tiempo = itemView.findViewById(R.id.tiempo_Text);
            experiencia = itemView.findViewById(R.id.experiencia_Text);
            salario = itemView.findViewById(R.id.salario_text);
        }

        @SuppressLint("SetTextI18n")
        public void bind(OfertaLaboralDTO oferta) {
            name_txt.setText(oferta.getNombreEmpresa());

            if (oferta.getSalario() != 0) {
                salario.setText("Salario: $ " + oferta.getSalario());
                salario.setVisibility(View.VISIBLE);
            } else {
                salario.setVisibility(View.GONE);
            }

            if (oferta.getTiempo() != null) {
                tiempo.setText("Tiempo: " + oferta.getTiempo());
                tiempo.setVisibility(View.VISIBLE);
            } else {
                tiempo.setVisibility(View.GONE);
            }

            if (oferta.getExperiencia() != null) {
                experiencia.setText("Experiencia: " + oferta.getExperiencia());
                experiencia.setVisibility(View.VISIBLE);
            } else {
                experiencia.setVisibility(View.GONE);
            }

            if (oferta.getCargo() != null) {
                cargo_Text.setText("Cargo: " + oferta.getCargo());
                cargo_Text.setVisibility(View.VISIBLE);
            } else {
                cargo_Text.setVisibility(View.GONE);
            }

            String fCierre = oferta.getFechaCierre();
            if (fCierre != null) {
                SimpleDateFormat formatoFechaEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
                try {
                    Date fechaDate = formatoFechaEntrada.parse(fCierre);
                    SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                    String fechaFormateada = formatoSalida.format(fechaDate);
                    fechaCierre.setText("Fecha de Cierre: " + fechaFormateada);
                    fechaCierre.setVisibility(View.VISIBLE);
                } catch (ParseException e) {
                    e.printStackTrace();
                    fechaCierre.setVisibility(View.GONE);
                }
            } else {
                fechaCierre.setVisibility(View.GONE);
            }

            String fotoBase64 = oferta.getFotoPortada();

            if (fotoBase64 != null) {
                Glide.with(context)
                        .load(fotoBase64)
                        .into(empresaImg);
            } else {
                Glide.with(context)
                        .load(R.drawable.empresas_img)
                        .into(empresaImg);
            }
        }
    }
}
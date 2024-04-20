package com.gamesmindt.myapplication.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gamesmindt.myapplication.Model.Graduado;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.activities.DetallePostuladoActivity;

import java.util.List;

public class PostulantesAdapter extends RecyclerView.Adapter<PostulantesAdapter.PostulantesViewHolder> {
    private Context context;
    private List<Graduado> graduadoList;

    public PostulantesAdapter(Context context, List<Graduado> graduadoList) {
        this.context = context;
        this.graduadoList = graduadoList;
    }

    public interface OnItemClickListener {
        void onItemClick(Graduado graduado);
    }

    @NonNull
    @Override
    public PostulantesAdapter.PostulantesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.postulantes, parent, false);
        return new PostulantesAdapter.PostulantesViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PostulantesAdapter.PostulantesViewHolder holder, int position) {
        Graduado graduado = graduadoList.get(position);
        holder.bind(graduado);
    }

    @Override
    public int getItemCount() {
        return graduadoList.size();
    }

    public void filterList(List<Graduado> filteredList) {
        graduadoList = filteredList;
        notifyDataSetChanged();
    }
    public class PostulantesViewHolder extends RecyclerView.ViewHolder{
        private TextView name_txt, descripcion_txt;
        private ImageView empresaImg;

        private ImageButton imageButton;

        public PostulantesViewHolder(@NonNull View itemView){
            super(itemView);
            name_txt = itemView.findViewById(R.id.name);
            descripcion_txt = itemView.findViewById(R.id.description);
            imageButton = itemView.findViewById(R.id.imageButtonPostulantes);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Graduado graduado) {
            name_txt.setText(graduado.getUsuario().getPersona().getPrimerNombre());
            descripcion_txt.setText(graduado.getEmailPersonal());
            final String idPostulante = graduado.getUsuario().getId().toString(); //Mandar el id

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences preferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("idpostulante", idPostulante);
                    editor.apply();
                    System.out.println(idPostulante + "IDOFERTALABORAL");

                    // Crear un Intent para iniciar la actividad
                    Intent intent = new Intent(view.getContext(), DetallePostuladoActivity.class);

                    // Opcional: agregar datos adicionales al Intent
                    intent.putExtra("nombre_extra", idPostulante);

                    // Iniciar la actividad
                    view.getContext().startActivity(intent);
                }
            });
/*
            String fotoBase64 = empresa.get();

            // Verificar si la cadena Base64 no está vacía y comienza con el prefijo adecuado
            if (fotoBase64 != null && fotoBase64.startsWith("data:image/jpeg;base64,")) {
                // Obtener la parte de la cadena después del prefijo
                String base64Image = fotoBase64.substring("data:image/jpeg;base64,".length());

                // Decodificar la cadena Base64 en un arreglo de bytes
                byte[] fotoBytes = Base64.decode(base64Image, Base64.DEFAULT);

                // Convertir los bytes en un Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
                empresaImg.setImageBitmap(bitmap);
            } else {
                System.out.println("No se pudo obtener la imagen");
            }*/
        }
    }
}

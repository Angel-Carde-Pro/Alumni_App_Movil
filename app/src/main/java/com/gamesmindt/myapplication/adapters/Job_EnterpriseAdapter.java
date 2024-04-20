package com.gamesmindt.myapplication.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.gamesmindt.myapplication.Model.OfertaDetalle;
import com.gamesmindt.myapplication.Model.OfertaLaboralDTO;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.configs.LoadingDialog;
import com.gamesmindt.myapplication.fragments.PostulantesFragment;

import java.util.List;

public class Job_EnterpriseAdapter extends RecyclerView.Adapter<Job_EnterpriseAdapter.Job_EnterpriseViewHolder> {
    private Context context;
    private List<OfertaDetalle> dtoList;
    public Job_EnterpriseAdapter(Context context, List<OfertaDetalle> dtoList) {
        this.context = context;
        this.dtoList = dtoList;
    }
    public interface  OnItemClickListener{
        void onItemClick(OfertaLaboralDTO ofertaLaboralDTO);
    }

    @NonNull
    @Override
    public Job_EnterpriseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.jobs_list, parent, false);
        return new Job_EnterpriseAdapter.Job_EnterpriseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Job_EnterpriseAdapter.Job_EnterpriseViewHolder holder, int position) {
        OfertaDetalle laboralDTO = dtoList.get(position);
        holder.bind(laboralDTO);
    }

    @Override
    public int getItemCount() {
        return dtoList.size();
    }

    public void filterList(List<OfertaDetalle> filteredList) {
        dtoList = filteredList;
        notifyDataSetChanged();
    }
    public class Job_EnterpriseViewHolder extends RecyclerView.ViewHolder{

        private TextView name_txt, descripcion_txt;
        private ImageView trabajoImg;
        private ImageButton imageButton;

        LoadingDialog loadingDialog ;



        public Job_EnterpriseViewHolder (@NonNull View itemView) {
            super(itemView);
            name_txt = itemView.findViewById(R.id.name);
            descripcion_txt = itemView.findViewById(R.id.description);
            imageButton = itemView.findViewById(R.id.imageButton);

        }



        @SuppressLint("SetTextI18n")
        public void bind(OfertaDetalle ofertaLaboralDTO) {
            name_txt.setText(ofertaLaboralDTO.getCargo());
            descripcion_txt.setText(ofertaLaboralDTO.getEmpresa().getNombre());
            final String ofertaLaboralDTOId = ofertaLaboralDTO.getId().toString();

            String fotoBase64 = ofertaLaboralDTO.getFotoPortada();
            try {
                // Verificar si la cadena Base64 no está vacía y comienza con el prefijo adecuado
                if (fotoBase64 != null && fotoBase64.startsWith("data:image/jpeg;base64,")) {
                    // Obtener la parte de la cadena después del prefijo
                    String base64Image = fotoBase64.substring("data:image/jpeg;base64,".length());

                    // Decodificar la cadena Base64 en un arreglo de bytes
                    byte[] fotoBytes = Base64.decode(base64Image, Base64.DEFAULT);

                    // Convertir los bytes en un Bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
                    trabajoImg.setImageBitmap(bitmap);
                } else {
                    System.out.println("No se pudo obtener la imagen");
                }
            }catch (Exception e){
                System.out.println("SSSSSSSS");
            }


            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences preferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("ofertaLaboralid", ofertaLaboralDTOId);
                    editor.apply();
                    System.out.println(ofertaLaboralDTOId + "IDOFERTALABORAL");

                    FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    PostulantesFragment nuevoFragmento = new PostulantesFragment();
                    fragmentTransaction.replace(R.id.container, nuevoFragmento); // Reemplaza R.id.contenedor_fragmento con el ID de tu contenedor de fragmento
                    fragmentTransaction.addToBackStack(null); // Opcional: agrega este fragmento a la pila de retroceso para permitir la navegación hacia atrás
                    fragmentTransaction.commit();
                }
            });
        }
}
}

package com.gamesmindt.myapplication.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.gamesmindt.myapplication.Model.Empresa;
import com.gamesmindt.myapplication.Model.EmpresaDTO;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.fragments.Job_EnterpriseFragment;

import java.util.List;

public class EmpresasAdapter extends RecyclerView.Adapter<EmpresasAdapter.EmpresaViewHolder>{

    private Context context;
    private List<EmpresaDTO> listaEmpresas;



    public EmpresasAdapter(Context context, List<EmpresaDTO> listaEmpresas) {
        this.context = context;
        this.listaEmpresas = listaEmpresas;
    }

    // Interfaz para manejar clics en elementos de la lista
    public interface OnItemClickListener {
        void onItemClick(Empresa empresa);
    }

    @NonNull
    @Override
    public EmpresaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.jobs_list, parent, false);
        return new EmpresasAdapter.EmpresaViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EmpresasAdapter.EmpresaViewHolder holder, int position) {
        EmpresaDTO empresa = listaEmpresas.get(position);
        holder.bind(empresa);
    }

    @Override
    public int getItemCount() {
        return listaEmpresas.size();
    }

    public void filterList(List<EmpresaDTO> filteredList) {
        listaEmpresas = filteredList;
        notifyDataSetChanged();
    }

    public class EmpresaViewHolder extends RecyclerView.ViewHolder {

        private TextView name_txt, descripcion_txt;
        private ImageView empresaImg;

        private ImageButton imageButton;

        public EmpresaViewHolder(@NonNull View itemView) {
            super(itemView);
            name_txt = itemView.findViewById(R.id.name);
            descripcion_txt = itemView.findViewById(R.id.description);
            empresaImg = itemView.findViewById(R.id.imageviewlist);
            imageButton = itemView.findViewById(R.id.imageButton);
        }

        @SuppressLint("SetTextI18n")
        public void bind(EmpresaDTO empresa) {
            name_txt.setText(empresa.getNombre());
            descripcion_txt.setText(empresa.getRuc());
            empresaImg.setImageResource(R.drawable.office_building);
            final String nameenterprise = empresa.getNombre(); // Declarar nameenterprise como final
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences preferences = context.getSharedPreferences("user_data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("nombreempresa", nameenterprise);
                    editor.apply();
                    System.out.println(nameenterprise + "NAMEWWWWEEE");

                    FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Job_EnterpriseFragment nuevoFragmento = new Job_EnterpriseFragment();
                    fragmentTransaction.replace(R.id.container, nuevoFragmento); // Reemplaza R.id.contenedor_fragmento con el ID de tu contenedor de fragmento
                    fragmentTransaction.addToBackStack(null); // Opcional: agrega este fragmento a la pila de retroceso para permitir la navegación hacia atrás
                    fragmentTransaction.commit();

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

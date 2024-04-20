package com.gamesmindt.myapplication.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.activities.DetalleOfertaActivity;
import com.gamesmindt.myapplication.activities.Update_Profile_Activity;

public class ProfileFragment extends Fragment {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_profile, viewGroup, false);
        SharedPreferences preferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String primerNombre = preferences.getString("primerNombre", "");
        String apellidoPaterno = preferences.getString("apellidoPaterno", "");
        String url_imagen = preferences.getString("urlImagen", "");

        TextView welcomeText = inflate.findViewById(R.id.user_txt);
        RelativeLayout logOutBtn = inflate.findViewById(R.id.log_out);
        RelativeLayout editar_Perfil = inflate.findViewById(R.id.editar_Perfil_Btn);
        RelativeLayout alumni_web = inflate.findViewById(R.id.alumni_web);

        ImageView perfilImg = inflate.findViewById(R.id.imagePerfil);

        RelativeLayout facebookBtn = inflate.findViewById(R.id.facebook);
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialLink(getString(R.string.facebook_link));
            }
        });

        RelativeLayout youtubeBtn = inflate.findViewById(R.id.youtube);
        youtubeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialLink(getString(R.string.youtube_link));
            }
        });

        RelativeLayout instagramBtn = inflate.findViewById(R.id.instagram);
        instagramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialLink(getString(R.string.instagram_link));
            }
        });

        RelativeLayout twitterBtn = inflate.findViewById(R.id.twitter);
        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialLink(getString(R.string.x_link));
            }
        });

        editar_Perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Update_Profile_Activity.class);
                startActivity(intent);
            }
        });

        alumni_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Crear un Intent con la acción ACTION_VIEW
                Intent intent = new Intent(Intent.ACTION_VIEW);

                // Establecer la URL en el Intent
                intent.setData(Uri.parse(getString(R.string.alumni_link)));

                // Verificar si hay aplicaciones que puedan manejar el Intent
                if (getContext() != null) {
                    // Abrir la página web
                    getContext().startActivity(intent);
                } else {
                    // Si no hay aplicaciones que puedan manejar el Intent, mostrar un mensaje de error
                    Toast.makeText(getContext(), "No se pudo abrir la página web", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("¿Estás seguro de que quieres salir?").setCancelable(false).setPositiveButton("Salir de la App", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requireActivity().finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
            }
        });

        String mensajeBienvenida = getString(R.string.mensajeBienvenida, primerNombre, apellidoPaterno);
        String mensajeBienvenidaCapitalizado = capitalizeFirstLetterOfEachWord(mensajeBienvenida);
        welcomeText.setText(mensajeBienvenidaCapitalizado);


        Glide.with(getContext())
                .load(url_imagen)
                .into(perfilImg);

        return inflate;
    }

    private String capitalizeFirstLetterOfEachWord(String input) {
        StringBuilder output = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                output.append(c);
            } else if (capitalizeNext) {
                output.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                output.append(Character.toLowerCase(c));
            }
        }

        return output.toString();
    }

    private void openSocialLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setData(Uri.parse(url));

        if (getContext() != null) {
            // Abrir la página web
            getContext().startActivity(intent);
        } else {
            // Si no hay aplicaciones que puedan manejar el Intent, mostrar un mensaje de error
            Toast.makeText(getContext(), "No se pudo abrir la página web", Toast.LENGTH_SHORT).show();
        }
    }


    public void shareApp() {
        // Note: Corregir
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", R.string.app_name);
        intent.putExtra("android.intent.extra.TEXT", R.string.base_link_apk);
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    public void onDestroy() {
        super.onDestroy();
    }
}

package com.gamesmindt.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gamesmindt.myapplication.Model.AnswerSearchDTO;
import com.gamesmindt.myapplication.Model.Carrera;
import com.gamesmindt.myapplication.Model.GraduadoDTO;
import com.gamesmindt.myapplication.Model.PostulacionDTO;
import com.gamesmindt.myapplication.Model.Question;
import com.gamesmindt.myapplication.Model.QuestionType;
import com.gamesmindt.myapplication.Model.Survey;
import com.gamesmindt.myapplication.R;
import com.gamesmindt.myapplication.Services.AnswerServiceApi;
import com.gamesmindt.myapplication.Services.CarreraServiceApi;
import com.gamesmindt.myapplication.Services.EncuestasServiceApi;
import com.gamesmindt.myapplication.Services.GraduadoServiceApi;
import com.gamesmindt.myapplication.configs.LoadingDialog;
import com.gamesmindt.myapplication.configs.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleEncuestaActivity extends AppCompatActivity {

    SharedPreferences preferences;
    LoadingDialog loadingDialog = new LoadingDialog(DetalleEncuestaActivity.this);
    EncuestasServiceApi encuestasServiceApi = RetrofitClient.getRetrofitInstance().create(EncuestasServiceApi.class);
    GraduadoServiceApi graduadoServiceApi = RetrofitClient.getRetrofitInstance().create(GraduadoServiceApi.class);
    AnswerServiceApi answerServiceApi = RetrofitClient.getRetrofitInstance().create(AnswerServiceApi.class);
    CarreraServiceApi carreraServiceApi = RetrofitClient.getRetrofitInstance().create(CarreraServiceApi.class);
    String accessToken = "";
    String usuario = "";
    private Survey selectedSurvey;
    List<Carrera> carreras;
    private RadioGroup radioGroupCarrera;
    private EditText editTextComentario;
    private EditText editTextCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_encuesta);

        preferences = DetalleEncuestaActivity.this.getSharedPreferences("user_data", MODE_PRIVATE);
        accessToken = preferences.getString("accessToken", "");
        usuario = preferences.getString("username", "");

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));

        preferences = DetalleEncuestaActivity.this.getSharedPreferences("user_data", MODE_PRIVATE);
        String token = preferences.getString("accessToken", "");

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button responderbtn = findViewById(R.id.btnResponder);
        responderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardarRespuesta();
            }
        });

        Intent intent = getIntent();
        int surveyId = intent.getIntExtra("id_Encuesta", -1);

        if (surveyId != -1) {
            loadingDialog.startLoagingDialog();
            Call<Survey> call = encuestasServiceApi.getSurveyById(surveyId, "Bearer " + accessToken);
            call.enqueue(new Callback<Survey>() {
                @Override
                public void onResponse(Call<Survey> call, Response<Survey> response) {
                    loadingDialog.cerrarLoadingDialog();
                    if (response.isSuccessful()) {
                        selectedSurvey = response.body();
                        showSurveyQuestionsAndOptions(selectedSurvey);
                    } else {
                        loadingDialog.showFailureDialog("Oops...", "Fallo al intentar obtener los datos.");
                    }
                }

                @Override
                public void onFailure(Call<Survey> call, Throwable t) {
                    System.out.println(R.string.error_conexion + t.getMessage());
                    loadingDialog.showFailureDialog(getString(R.string.error_conexion), "Revise su conexión a internet.");
                    t.printStackTrace();
                }
            });
        }
    }

    private void showSurveyQuestionsAndOptions(Survey survey) {
        if (survey == null || survey.getQuestions() == null || survey.getQuestions().isEmpty()) {
            return;
        }

        LinearLayout containerLayout = findViewById(R.id.containerLayout);
        containerLayout.removeAllViews();

        Call<List<Carrera>> call = carreraServiceApi.getCarreras("Bearer " + accessToken);

        call.enqueue(new Callback<List<Carrera>>() {
            @Override
            public void onResponse(Call<List<Carrera>> call, Response<List<Carrera>> response) {
                if (response.isSuccessful()) {
                    carreras = response.body();

                    // Crear el TextView para el título
                    TextView tituloTextView = new TextView(DetalleEncuestaActivity.this);
                    tituloTextView.setText("Seleccione su carrera:");
                    containerLayout.addView(tituloTextView);

                    // Crear el grupo de radio para las opciones de carrera
                    RadioGroup radioGroup = new RadioGroup(DetalleEncuestaActivity.this);
                    for (Carrera carrera : carreras) {
                        RadioButton radioButton = new RadioButton(DetalleEncuestaActivity.this);
                        radioButton.setText(carrera.getNombre());
                        radioGroup.addView(radioButton);
                    }
                    containerLayout.addView(radioGroup);

                    // Guardar referencia al RadioGroup
                    radioGroupCarrera = radioGroup;

                    // Crear el TextView y EditText para "Comentario:"
                    TextView comentarioTextView = new TextView(DetalleEncuestaActivity.this);
                    comentarioTextView.setText("Comentario:");
                    containerLayout.addView(comentarioTextView);

                    EditText comentarioEditText = new EditText(DetalleEncuestaActivity.this);
                    containerLayout.addView(comentarioEditText);

                    // Guardar referencia al EditText del comentario
                    editTextComentario = comentarioEditText;

                    Call<List<GraduadoDTO>> callgraduado = graduadoServiceApi.searchGraduadosByUsuario(usuario, "Bearer " + accessToken);

                    callgraduado.enqueue(new Callback<List<GraduadoDTO>>() {
                        @Override
                        public void onResponse(Call<List<GraduadoDTO>> call, Response<List<GraduadoDTO>> response) {
                            if (response.isSuccessful()) {
                                System.out.println("graduadoDTO.getEmailPersonal() = " + response.body().get(0).getEmailPersonal());

                                // Crear el TextView y EditText para "Escriba su correo electrónico:"
                                TextView correoTextView = new TextView(DetalleEncuestaActivity.this);
                                correoTextView.setText("Escriba su correo electrónico:");
//                                correoTextView.setText();

                                containerLayout.addView(correoTextView);
                                EditText correoEditText = new EditText(DetalleEncuestaActivity.this);
                                correoEditText.setText(response.body().get(0).getEmailPersonal());
                                containerLayout.addView(correoEditText);

                                // Guardar referencia al EditText del correo electrónico
                                editTextCorreo = correoEditText;
                            } else {
                                System.out.println("Error obtener el correo.");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<GraduadoDTO>> call, Throwable t) {
                            System.out.println("Error de conexión = " + t);
                            loadingDialog.showFailureDialog(getString(R.string.error_conexion), "Revisa tu conexión.");
                        }
                    });
                } else {
                    System.out.println("Error obtener las carreras.");
                }
            }


            @Override
            public void onFailure(Call<List<Carrera>> call, Throwable t) {
                System.out.println("Error de conexión = " + t);
                loadingDialog.showFailureDialog(getString(R.string.error_conexion), "Revisa tu conexión.");
            }
        });

        for (Question question : survey.getQuestions()) {
            TextView questionTextView = new TextView(this);
            questionTextView.setText(question.getText());
            questionTextView.setTextAppearance(this, android.R.style.TextAppearance_Small);
            containerLayout.addView(questionTextView);

            // Crear vistas para las opciones según el tipo de pregunta
            if (question.getType() == QuestionType.OPCION_MULTIPLE) {
                showMultipleChoiceOptions(containerLayout, question);
            } else if (question.getType() == QuestionType.OPCION_MULTIPLEUNICO) {
                showSingleChoiceOptions(containerLayout, question);
            } else if (question.getType() == QuestionType.ABIERTA) {
                showOpenEndedQuestion(containerLayout, question);
            } else if (question.getType() == QuestionType.SI_NO) {
                showYesNoOptions(containerLayout, question);
            } else if (question.getType() == QuestionType.CALIFICACION_1_10) {
                showRatingOptions(containerLayout, question, 1, 10);
            } else if (question.getType() == QuestionType.CALIFICACION_1_5) {
                showRatingOptions(containerLayout, question, 1, 5);
            }
        }
    }

    private void showMultipleChoiceOptions(LinearLayout containerLayout, Question question) {
        for (String option : question.getOptions()) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(option);
            checkBox.setTag(question.getId());
            containerLayout.addView(checkBox);
        }
    }

    private void showSingleChoiceOptions(LinearLayout containerLayout, Question question) {
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setTag(question.getId());

        for (String option : question.getOptions()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioGroup.addView(radioButton);
        }
        containerLayout.addView(radioGroup);
    }

    private void showOpenEndedQuestion(LinearLayout containerLayout, Question question) {
        EditText editText = new EditText(this);
        editText.setHint("Ingrese su respuesta");
        editText.setTag(question.getId());
        containerLayout.addView(editText);
    }

    private void showYesNoOptions(LinearLayout containerLayout, Question question) {
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setTag(question.getId());

        RadioButton yesButton = new RadioButton(this);
        yesButton.setText("Sí");
        radioGroup.addView(yesButton);

        RadioButton noButton = new RadioButton(this);
        noButton.setText("No");
        radioGroup.addView(noButton);

        containerLayout.addView(radioGroup);
    }

    private void showRatingOptions(LinearLayout containerLayout, Question question, int minRating, int maxRating) {
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setTag(question.getId());

        for (int i = minRating; i <= maxRating; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(String.valueOf(i));
            radioGroup.addView(radioButton);
        }
        containerLayout.addView(radioGroup);
    }

    private void GuardarRespuesta() {
        Map<Integer, String> respuestas = obtenerRespuestas();
        System.out.println("respuestas = " + respuestas);
        AnswerSearchDTO answerDTO = new AnswerSearchDTO();
        answerDTO.setGraduadoEmail(editTextCorreo.getText().toString());
        answerDTO.setCarreraNombre(getSelectedCarrera());
        answerDTO.setSurveyTitle(selectedSurvey.getTitle());
        answerDTO.setQuestionResponses(respuestas);
        answerDTO.setOpenAnswer(editTextComentario.getText().toString());

        Call<AnswerSearchDTO> call = answerServiceApi.saveAnswer(answerDTO, "Bearer " + accessToken);

        call.enqueue(new Callback<AnswerSearchDTO>() {
            @Override
            public void onResponse(Call<AnswerSearchDTO> call, Response<AnswerSearchDTO> response) {
                Toast.makeText(DetalleEncuestaActivity.this, "¡Tu respuesta fue guardada correctamente!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<AnswerSearchDTO> call, Throwable t) {
                // Manejar errores de conexión
                Toast.makeText(DetalleEncuestaActivity.this, "Error de conexión. Por favor, revisa tu conexión a Internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getSelectedCarrera() {
        int selectedId = radioGroupCarrera.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        if (selectedRadioButton != null) {
            return selectedRadioButton.getText().toString();
        }
        return "";
    }

    private Map<Integer, String> obtenerRespuestas() {
        Map<Integer, String> respuestas = new HashMap<>();

        LinearLayout containerLayout = findViewById(R.id.containerLayout);
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            View view = containerLayout.getChildAt(i);

            if (view instanceof RadioGroup) {
                RadioGroup radioGroup = (RadioGroup) view;
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // Check if a radio button is selected before accessing it
                if (selectedId != -1) {
                    RadioButton selectedRadioButton = radioGroup.findViewById(selectedId);
                    Integer questionId = (Integer) radioGroup.getTag(); // Obtener el id de la pregunta desde la etiqueta

                    if (questionId != null) {
                        respuestas.put(questionId, selectedRadioButton.getText().toString());
                    }
                }
            } else if (view instanceof EditText) {
                EditText editText = (EditText) view;
                String respuesta = editText.getText().toString();
                Integer questionId = (Integer) editText.getTag(); // Obtener el id de la pregunta desde la etiqueta

                if (questionId != null) {
                    respuestas.put(questionId, respuesta);
                }
            } else if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    Integer questionId = (Integer) checkBox.getTag(); // Obtener el id de la pregunta desde la etiqueta

                    if (questionId != null) {
                        respuestas.put(questionId, checkBox.getText().toString());
                    }
                }
            }
        }

        return respuestas;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
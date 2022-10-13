package br.com.vamosracharapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

import br.com.vamosrachar.R;

public class MainActivity extends AppCompatActivity
        implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

    EditText valorADividirEditText;
    TextView qtdePessoasTextView;
    ImageButton menosPessoasButton;
    ImageButton maisPessoasButton;
    TextView valorParaCadaTextView;
    FloatingActionButton compartilharValorFButton;
    FloatingActionButton lerValorFButton;
    TextToSpeech TTSPlayer;

    int qtdePessoas = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valorADividirEditText = findViewById(R.id.valorADividirEditText);
        qtdePessoasTextView = findViewById(R.id.qtdePessoasTextView);
        valorParaCadaTextView = findViewById(R.id.valorParaCadaTextView);
        maisPessoasButton = findViewById(R.id.maisPessoasButton);
        menosPessoasButton = findViewById(R.id.menosPessoasButton);
        compartilharValorFButton = findViewById(R.id.compartilharValorFButton);
        lerValorFButton = findViewById(R.id.lerValorFButton);

        valorADividirEditText.addTextChangedListener(this);
        qtdePessoasTextView.addTextChangedListener(this);
        maisPessoasButton.setOnClickListener(this);
        menosPessoasButton.setOnClickListener(this);
        compartilharValorFButton.setOnClickListener(this);
        lerValorFButton.setOnClickListener(this);

        Intent myTTSIntent = new Intent();
        myTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(myTTSIntent, 1122);
    } //onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
            TTSPlayer = new TextToSpeech(this, this);
        } else {
            Intent instalTTSIntent = new Intent();
            instalTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(instalTTSIntent);
        }
    } //onActivityResult

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS)
            Toast.makeText(this, "O TTS está habilitado!", Toast.LENGTH_LONG).show();
        else if (initStatus == TextToSpeech.ERROR)
            Toast.makeText(this, "O TTS está desabilidado!", Toast.LENGTH_LONG).show();

    } //onInit

    @Override
    public void beforeTextChanged(CharSequence charSequence, int var1, int var2, int var3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int var1, int var2, int var3) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        double valorParaCada, valorTotal = 0;

        try {
            valorTotal = Double.parseDouble(valorADividirEditText.getText().toString());
        } catch (Exception exception) {
            valorADividirEditText.setText("0.00");
        }

        valorParaCada = valorTotal / qtdePessoas;

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String resultado = decimalFormat.format(valorParaCada);
        valorParaCadaTextView.setText(resultado);
    }

    @Override
    public void onClick(View view) {
        if (view == maisPessoasButton) {
            qtdePessoas += 1;
            qtdePessoasTextView.setText(String.valueOf(qtdePessoas));
        }
        if (view == menosPessoasButton) {
            qtdePessoas -= 1;
            if (qtdePessoas <= 2) {
                qtdePessoas = 2;
            }
            qtdePessoasTextView.setText(String.valueOf(qtdePessoas));
        }
        if (view == compartilharValorFButton) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Ficou R$ "
                    + valorParaCadaTextView.getText().toString() + " para cada");
            startActivity(intent);
        }
        if (view == lerValorFButton) {
            if (TTSPlayer != null) {
                TTSPlayer.speak("Ficou R$ " + valorParaCadaTextView.getText().toString()
                        + " para cada", TextToSpeech.QUEUE_FLUSH,
                        null, "ID1");
            }
        }

    }
}
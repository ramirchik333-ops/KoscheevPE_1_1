package com.example.unitconverter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Второй экран: ввод числа, выбор единиц «Из» и «В», вывод результата.
 */
public class ConverterActivity extends Activity {

    private static final String KEY_INPUT = "input";
    private static final String KEY_FROM = "from";
    private static final String KEY_TO = "to";
    private static final String KEY_RESULT = "result";

    private EditText inputField;
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private TextView resultField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        MainActivity.applyEdgeToEdge(this, findViewById(R.id.root));

        inputField = findViewById(R.id.inputValue);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        resultField = findViewById(R.id.resultText);

        // Разрешаем и точку, и запятую: раскладки клавиатур различаются.
        inputField.setKeyListener(DigitsKeyListener.getInstance("0123456789,.-"));
        inputField.setRawInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL
                | InputType.TYPE_NUMBER_FLAG_SIGNED);

        setupUnitSpinners();

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else {
            // По умолчанию переводим байты в килобайты.
            toSpinner.setSelection(1);
        }

        findViewById(R.id.calculateButton).setOnClickListener(v -> calculate());
    }

    private void setupUnitSpinners() {
        List<String> units = Arrays.asList(getResources().getStringArray(R.array.units));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);
    }

    private void calculate() {
        hideKeyboard();

        String raw = inputField.getText().toString().trim();
        if (raw.isEmpty()) {
            resultField.setText("");
            Toast.makeText(this, R.string.error_empty_input, Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal value;
        try {
            value = new BigDecimal(raw.replace(',', '.'));
        } catch (NumberFormatException e) {
            resultField.setText("");
            Toast.makeText(this, R.string.error_invalid_number, Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal converted = UnitConverter.convert(value,
                fromSpinner.getSelectedItemPosition(),
                toSpinner.getSelectedItemPosition());

        resultField.setText(UnitConverter.format(converted));
    }

    private void hideKeyboard() {
        View focus = getCurrentFocus();
        if (focus == null) {
            return;
        }
        InputMethodManager manager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
        focus.clearFocus();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_INPUT, inputField.getText().toString());
        outState.putInt(KEY_FROM, fromSpinner.getSelectedItemPosition());
        outState.putInt(KEY_TO, toSpinner.getSelectedItemPosition());
        outState.putString(KEY_RESULT, resultField.getText().toString());
    }

    private void restoreState(Bundle state) {
        inputField.setText(state.getString(KEY_INPUT, ""));
        fromSpinner.setSelection(state.getInt(KEY_FROM, 0));
        toSpinner.setSelection(state.getInt(KEY_TO, 1));
        resultField.setText(state.getString(KEY_RESULT, ""));
    }
}

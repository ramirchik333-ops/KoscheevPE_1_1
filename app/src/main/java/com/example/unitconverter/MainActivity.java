package com.example.unitconverter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

/**
 * Первый экран: заголовок, иллюстрация-заглушка и кнопка «Начать».
 * Кнопка открывает экран конвертера.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applyEdgeToEdge(this, findViewById(R.id.root));

        findViewById(R.id.startButton).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ConverterActivity.class)));
    }

    /**
     * Приложение занимает всю площадь экрана (edge-to-edge, обязателен для targetSdk 35+),
     * поэтому отступы системных панелей добавляем вручную.
     */
    static void applyEdgeToEdge(Activity activity, View root) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return;
        }
        Window window = activity.getWindow();
        window.setDecorFitsSystemWindows(false);
        window.setStatusBarColor(android.graphics.Color.TRANSPARENT);
        window.setNavigationBarColor(android.graphics.Color.TRANSPARENT);

        WindowInsetsController controller = window.getInsetsController();
        if (controller != null) {
            controller.setSystemBarsAppearance(0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                            | WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
        }

        root.setOnApplyWindowInsetsListener((view, insets) -> {
            android.graphics.Insets bars = insets.getInsets(WindowInsets.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }
}

package com.example.unitconverter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Чистая логика перевода единиц информации.
 *
 * <p>Все единицы кратны 1024 (двоичные приставки: КБ = 1024 Б, МБ = 1024 КБ, ГБ = 1024 МБ).
 * Расчёты выполняются в {@link BigDecimal}, поэтому результат не зависит от двоичной
 * плавающей точки и не теряет точность на больших значениях.</p>
 */
public final class UnitConverter {

    /** Множитель между соседними единицами. */
    static final int BASE = 1024;

    /** Показатели степени для порядка единиц из {@code R.array.units}. */
    private static final int[] EXPONENT = {0, 1, 2, 3};

    /** Сколько знаков после запятой показываем пользователю. */
    private static final int MAX_FRACTION_DIGITS = 6;

    private UnitConverter() {
    }

    /** Количество поддерживаемых единиц. */
    public static int unitsCount() {
        return EXPONENT.length;
    }

    /**
     * Переводит значение из одной единицы в другую.
     *
     * @param value     исходное значение
     * @param fromIndex индекс исходной единицы (0 — байт ... 3 — гигабайт)
     * @param toIndex   индекс целевой единицы
     * @return переведённое значение с точностью до 34 значащих цифр
     */
    public static BigDecimal convert(BigDecimal value, int fromIndex, int toIndex) {
        checkIndex(fromIndex);
        checkIndex(toIndex);

        int shift = EXPONENT[fromIndex] - EXPONENT[toIndex];
        BigDecimal factor = BigDecimal.valueOf(BASE).pow(Math.abs(shift));

        if (shift == 0) {
            return value;
        }
        return shift > 0
                ? value.multiply(factor)
                : value.divide(factor, MathContext.DECIMAL128);
    }

    /** Форматирует число для вывода: до 6 знаков, без хвостовых нулей, с запятой. */
    public static String format(BigDecimal value) {
        BigDecimal rounded = value
                .setScale(MAX_FRACTION_DIGITS, RoundingMode.HALF_UP)
                .stripTrailingZeros();
        String plain = rounded.toPlainString();
        return plain.replace('.', ',');
    }

    private static void checkIndex(int index) {
        if (index < 0 || index >= EXPONENT.length) {
            throw new IllegalArgumentException("Неизвестная единица измерения: " + index);
        }
    }
}

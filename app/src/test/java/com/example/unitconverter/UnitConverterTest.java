package com.example.unitconverter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.math.BigDecimal;

/** Юнит-тесты чистой логики перевода единиц. */
public class UnitConverterTest {

    @Test
    public void kilobyte_to_bytes() {
        assertEquals("1024", UnitConverter.format(UnitConverter.convert(BigDecimal.ONE, 1, 0)));
    }

    @Test
    public void bytes_to_kilobytes() {
        assertEquals("0,000977", UnitConverter.format(UnitConverter.convert(BigDecimal.ONE, 0, 1)));
    }

    @Test
    public void gigabyte_to_megabytes() {
        assertEquals("1024", UnitConverter.format(UnitConverter.convert(BigDecimal.ONE, 3, 2)));
    }

    @Test
    public void half_megabyte_to_kilobytes() {
        assertEquals("512", UnitConverter.format(
                UnitConverter.convert(new BigDecimal("0.5"), 2, 1)));
    }

    @Test
    public void same_unit_keeps_value() {
        assertEquals("42", UnitConverter.format(UnitConverter.convert(new BigDecimal("42"), 2, 2)));
    }

    @Test
    public void format_trims_trailing_zeros() {
        assertEquals("2,5", UnitConverter.format(new BigDecimal("2.500000")));
        assertEquals("0", UnitConverter.format(BigDecimal.ZERO));
        assertEquals("1048576", UnitConverter.format(new BigDecimal("1048576.000")));
    }
}

package com.exasol.javatutorial.tls.test;

import com.exasol.ExaDataTypeException;
import com.exasol.ExaIterationException;
import com.exasol.ExaIterator;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a stub for the {@link ExaIterator} a class that represents the context a Java UDF runs in.
 * <p>
 * The main use case is verifying the rows emitted by a Java UDF.
 * </p>
 */
public class ExaIteratorStub implements ExaIterator {
    List<List<Object>> emittedRows = new ArrayList<>();

    @Override
    public long size() throws ExaIterationException {
        return 0;
    }

    @Override
    public boolean next() throws ExaIterationException {
        return false;
    }

    @Override
    public void reset() throws ExaIterationException {

    }

    @Override
    public void emit(final Object... objects) throws ExaIterationException, ExaDataTypeException {
        emittedRows.add(List.of(objects));
    }

    @Override
    public Integer getInteger(final int i) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public Integer getInteger(final String s) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public Long getLong(final int i) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public Long getLong(final String s) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(final int i) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(final String s) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public Double getDouble(final int i) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public Double getDouble(final String s) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public String getString(final int i) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public String getString(final String s) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public Boolean getBoolean(final int i) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public Boolean getBoolean(final String s) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public Date getDate(final int i) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public Date getDate(final String s) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(final int i) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(final String s) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public Object getObject(final int i) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    @Override
    public Object getObject(final String s) throws ExaIterationException, ExaDataTypeException {
        return null;
    }

    /**
     * Get the rows that were emitted in the UDF.
     * @return List (rows) of list (columns) of emitted objects
     */
    public List<List<Object>> getEmittedRows() {
        return emittedRows;
    }
}

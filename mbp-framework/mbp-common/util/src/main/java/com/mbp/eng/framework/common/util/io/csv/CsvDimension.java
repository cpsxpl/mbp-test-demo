package com.mbp.eng.framework.common.util.io.csv;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.AbstractRowProcessor;

public class CsvDimension extends AbstractRowProcessor {
    long lastColumn = -1;
    long rowCount = 0;

    @Override
    public void rowProcessed(String[] row, ParsingContext context) {
        rowCount++;
        if (lastColumn < row.length) {
            lastColumn = row.length;
        }
    }
}

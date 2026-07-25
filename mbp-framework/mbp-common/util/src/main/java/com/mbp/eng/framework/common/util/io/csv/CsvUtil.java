package com.mbp.eng.framework.common.util.io.csv;

import com.univocity.parsers.common.processor.ColumnProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CsvUtil {
    /**
     * 获取流
     *
     * @param file 文件对象
     * @return 文件流
     */
    public static InputStream getInputStreamFromFile(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("RuntimeException occur when try get InputStream from File");
        }
    }
    /**
     * 获取一个csv文件行数
     *
     * @param inputStream csv文件流
     * @return csv文件行数
     */
    public static long getCsvRowSize(InputStream inputStream) {
        //Creates an instance of our own custom RowProcessor, defined above.
        CsvDimension csvDimension = new CsvDimension();

        CsvParserSettings settings = new CsvParserSettings();

        //This tells the parser that no row should have more than 2,000,000 columns
        settings.setMaxColumns(2000000);

        //Here you can select the column indexes you are interested in reading.
        //The parser will return values for the columns you selected, in the order you defined
        //By selecting no indexes here, no String objects will be created
        settings.selectIndexes(/*nothing here*/);

        //When you select indexes, the columns are reordered so they come in the order you defined.
        //By disabling column reordering, you will get the original row, with nulls in the columns you didn't select
        settings.setColumnReorderingEnabled(false);

        //We instruct the parser to send all rows parsed to your custom Processor.
        settings.setProcessor(csvDimension);

        //Finally, we create a parser
        CsvParser parser = new CsvParser(settings);

        //And parse! All rows are sent to your custom RowProcessor (CsvDimension)
        parser.parse(inputStream);
        return csvDimension.rowCount;
    }
    /**
     * get csv column data
     *
     * @param inputStream file input stream
     * @return Map<String, List < String>>
     **/
    public static Map<String, List<String>> getCsvColumnData(InputStream inputStream) {
        // To get the values of all columns, use a column processor
        ColumnProcessor colProcessor = getColumnProcessor(inputStream);
        //Finally, we can get the column values:
        Map<String, List<String>> columnValues = new TreeMap<String, List<String>>(colProcessor.getColumnValuesAsMapOfNames());
        return columnValues;
    }
    /**
     * get one column data by index
     *
     * @param inputStream file stream
     * @param index       column index, start with 0
     * @return the column data
     */
    public static List<String> getCsvColumnDataByIndex(InputStream inputStream, int index) {
        // To get the values of all columns, use a column processor
        ColumnProcessor colProcessor = getColumnProcessor(inputStream);
        //catch exception,contain null
        try {
            //Finally, we can get the column values:
            List<String> stringList = colProcessor.getColumn(index);
            return stringList;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    /**
     * get ByteArrayOutputStream file stream by row pojo such as List<Object[]>
     *
     * @param rows the data need write to file
     * @return file ByteArrayOutputStream
     */
    public static ByteArrayOutputStream writeCsvFileStream(List<Object[]> rows) {
        // Writing to an in-memory byte array. This will be printed out to the standard output so you can easily see the result.
        ByteArrayOutputStream csvResult = new ByteArrayOutputStream();

        // CsvWriter (and all other file writers) work with an instance of java.io.Writer
        Writer outputWriter = new OutputStreamWriter(csvResult);

        // All you need is to create an instance of CsvWriter with the default CsvWriterSettings.
        // By default, only values that contain a field separator are enclosed within quotes.
        // If quotes are part of the value, they are escaped automatically as well.
        // Empty rows are discarded automatically.
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());

        // Write the record headers of this file
        // Here we just tell the writer to write everything and close the given output Writer instance.
        writer.writeRowsAndClose(rows);
        return csvResult;
    }
    /**
     * write only one column csv file
     *
     * @param objectList the data of column want to write
     * @return file ByteArrayOutputStream
     */
    public static <T> ByteArrayOutputStream writeCsvFileOneColumn(List<T> objectList) {
        List<Object[]> rows = new ArrayList<>();
        for (Object object : objectList) {
            rows.add(new Object[]{object});
        }
        return writeCsvFileStream(rows);
    }
    private static ColumnProcessor getColumnProcessor(InputStream inputStream) {
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.getFormat().setLineSeparator("\n");
        parserSettings.setHeaderExtractionEnabled(false);

        // To get the values of all columns, use a column processor
        ColumnProcessor colProcessor = new ColumnProcessor();
        parserSettings.setProcessor(colProcessor);

        CsvParser parser = new CsvParser(parserSettings);

        //This will kick in our column processor
        parser.parse(inputStream);
        return colProcessor;
    }
}

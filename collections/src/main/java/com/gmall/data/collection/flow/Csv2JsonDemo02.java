package com.gmall.data.collection.flow;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
public class Csv2JsonDemo02 {

    public static void main(String[] args) throws Exception{

        File input = new File("/Users/wufengchi/Downloads/shence_log0509.csv");
        File output = new File("collections/src/main/resources/data/shence_log0509.json");

        CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
        CsvMapper csvMapper = new CsvMapper();

        // Read data from CSV file
        List<Object> readAll = csvMapper.readerFor(Map.class).with(csvSchema).readValues(input).readAll();

        ObjectMapper mapper = new ObjectMapper();

        // Write JSON formated data to output.json file
        mapper.writerWithDefaultPrettyPrinter().writeValue(output, readAll);

        // Write JSON formated data to stdout
//        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(readAll));


    }


}

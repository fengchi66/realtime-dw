package com.gmall.data.collection.flow;

import com.google.common.base.Strings;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class CsvToJsonDemo {
    private List stringToList(String s, String sep) {
        if (s == null) {
            return null;

        }

        String[] parts = s.split(sep);

        return Arrays.asList(parts);

    }

    private String stringToJson(List header, List lineData) throws Exception {
//        if (header == null || lineData == null) {
//            throw new Exception("输入不能为null。");
//
//        } else if (header.size() != lineData.size()) {
//            throw new Exception("表头个数和数据列个数不等。");
//        }

        StringBuilder sBuilder = new StringBuilder();

        sBuilder.append("{");

        for (int i = 0; i < header.size(); i++) {
            String mString = String.format("\"%s\":\"%s\"", header.get(i), lineData.get(i));

            if (!Strings.isNullOrEmpty(lineData.get(i).toString()) && i != header.size() - 1) {
                sBuilder.append(mString);
            }

            if (i != header.size() - 1 && !Strings.isNullOrEmpty(lineData.get(i).toString())) {
                sBuilder.append(",");

            }

        }

        sBuilder.append("}");

        return sBuilder.toString();

    }

    public void ConvertToJson(InputStream filePath, OutputStream outPutPath) throws Exception {
        InputStreamReader isr = new InputStreamReader(filePath, "utf-8");

        BufferedReader reader = new BufferedReader(isr);

        OutputStreamWriter osw = new OutputStreamWriter(outPutPath, "utf-8");

        BufferedWriter writer = new BufferedWriter(osw);

        try {
            String sep = ",";

            String headerStr = reader.readLine();

            if (headerStr.trim().isEmpty()) {
                System.out.println("表格头不能为空");

                return;

            }

            List header = stringToList(headerStr, sep);

            String line;

            int lineCnt = 1;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    System.out.println("第" + lineCnt + "行为空，已跳过");

                    continue;

                }

                List lineData = stringToList(line, sep);

//                if (lineData.size() != header.size()) {
//                    String mString = String.format("第%d行数据列和表头列个数不一致\r\n%s", lineCnt, line);
//
//                    System.err.println(mString);
//
//                    continue;
//
//                }

                String jsonStr = stringToJson(header, lineData);

                writer.write(jsonStr);

                writer.write("\r\n");

                lineCnt++;

            }

        } finally {
            if (reader != null) {
                reader.close();

            }

            if (writer != null) {
                writer.close();

            }

        }

    }

    public static void main(String[] args) throws Exception {
// TODO Auto-generated method stub

        InputStream filePath = new FileInputStream("/Users/wufengchi/Downloads/shence_log0509.csv");

        OutputStream outPutPath = new FileOutputStream("collections/src/main/resources/data/shence_log0509.json");

        CsvToJsonDemo csvToJSon = new CsvToJsonDemo();

        csvToJSon.ConvertToJson(filePath, outPutPath);

        System.out.println("处理完成。");

    }

}

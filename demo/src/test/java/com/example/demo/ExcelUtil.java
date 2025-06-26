package com.example.demo;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelUtil {

    public static List<String[]> readLoginData(String filePath) {
        List<String[]> loginData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header

                String username = row.getCell(0).getStringCellValue();
                String password = row.getCell(1).getStringCellValue();
                loginData.add(new String[]{username, password});
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return loginData;
    }
}

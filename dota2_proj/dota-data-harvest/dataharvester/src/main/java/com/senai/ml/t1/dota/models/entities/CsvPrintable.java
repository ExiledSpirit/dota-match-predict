package com.senai.ml.t1.dota.models.entities;

import java.lang.reflect.Field;

public abstract class CsvPrintable {
  private static String CSV_SEPARATOR = ",";
  private static String CSV_NEWLINE = "\n";

  public String printCsvHeader() {
    Field[] fields = this.getClass().getDeclaredFields();
    StringBuilder csvHeader = new StringBuilder();
    for (Field field : fields) {
      csvHeader.append(field.getName()).append(CSV_SEPARATOR);
    }
    csvHeader.deleteCharAt(csvHeader.length() - 1).append(CSV_NEWLINE);
    return csvHeader.toString();
  }

  public String printCsv() {
    StringBuilder csv = new StringBuilder();

    Field[] fields = this.getClass().getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        csv.append(field.get(this)).append(CSV_SEPARATOR);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        e.printStackTrace();
      }
      field.setAccessible(false);
    }

    csv.deleteCharAt(csv.length() - 1).append(CSV_NEWLINE);
    return csv.toString();
  }
}

package com.senai.ml.t1.dota.helper;

import java.util.List;

public class MathHelper {
  private MathHelper() {
  }

  public static double calculateStandardDeviation(List<Integer> array) {
    // get the sum of array
    double sum = 0.0;
    for (double i : array) {
      sum += i;
    }

    // get the mean of array
    int length = array.size();
    double mean = sum / length;

    // calculate the standard deviation
    double standardDeviation = 0.0;
    for (double num : array) {
      standardDeviation += Math.pow(num - mean, 2);
    }

    return Math.sqrt(standardDeviation / length);
  }
}

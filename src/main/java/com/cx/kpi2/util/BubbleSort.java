package com.cx.kpi2.util;

import java.math.BigDecimal;

public class BubbleSort {
    public void bubbleSort(BigDecimal[] arr, int n) {
        if (n <= 1) return;

        for (int i = 0; i < n; ++i) {

            boolean flag = false;
            for (int j = 0; j < n - i - 1; ++j) {

                if (arr[j].compareTo(arr[j + 1]) < 0) {
                    BigDecimal temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    flag = true;
                }
            }
            if (!flag) break;
        }
    }

}

package com.sdc2ch.service.test;

import java.util.Arrays;

public class TestSum {

	
	public static void main(String[] args) {
        int givenArray[] = {1, 2, 1, -3, -1};
        int firstElement;
        int startIndex;
        int endIndex;
        int currentSum;
        Arrays.sort(givenArray);
        for (int i = 0; i < givenArray.length; i++) {
            firstElement = givenArray[i];
            startIndex = i + 1;
            endIndex = givenArray.length - 1;
            while (startIndex < endIndex) {
                currentSum = givenArray[startIndex] + givenArray[endIndex];
                if (currentSum + firstElement == 0) {
                    System.out.println("Found three elements " + firstElement + "," + givenArray[startIndex] + " and " + givenArray[endIndex]);
                    startIndex++;
                    endIndex--;
                } else if (currentSum + firstElement < 0) {
                    startIndex++;
                } else {
                    endIndex--;
                }
            }
        }
    }
}

package com.azx.mybuildassistant;

public class SortTest {

    public static void main(String[] args) {
        int[] mumbers = new int[]{1, 2, 3, 4, 5, 6};
//        int result = addArray2(mumbers);
//        System.out.println("result : " + result);

        int[] arr = new int[]{1, 3, 5, 7, 8, 9, 10};
        int result = binarySearch(arr, 5);
        System.out.println("binarySearch : " + result);
    }

    // 二分查找
    private static int binarySearch(int[] arr, int target) {
        if (arr == null) {
            return -1;
        }

        int low = 0;
        int high = arr.length - 1;
        while (low <= high) {
            System.out.println("binarySearch low : " + low + " | high : " + high);
            int mid = (low + high) / 2;
            int guess = arr[mid];
            if (guess == target) {
                return mid;
            }

            if (guess > target) {
                high = mid;
            } else {
                low = mid;
            }
        }
        return -1;

    }
//
//
//    // 递归数组求和
//    // 0 1 2 3 4 5
//    // 1 2 3 4 5 6
//    private static int addArray(int[] numbers) {
//        int startIndex = 0;
//        return addNumber(startIndex, numbers);
//    }
//
//    private static int addNumber(int startIndex, int[] numbers) {
//        System.out.println("addNumber : " + startIndex);
//        if (startIndex <= numbers.length - 3) {
//            int tempResult = addNumber(startIndex + 2, numbers);
//            return tempResult + numbers[startIndex] + numbers[startIndex + 1];
//        } else {
//            return numbers[startIndex] + numbers[startIndex + 1];
//        }
//    }
//
//    private static int startIndex = 0;
//    private static int numSum = 0;
//    private static void addArray2(int[] numbers) {
////        System.out.println("addNumber : " + startIndex);
////        numSum = numbers[startIndex] +
////
////        if (startIndex + 1 > numbers.length - 1) {
////            return numSum + numbers[startIndex];
////        } else {
////            numSum = numbers[startIndex] + numbers[startIndex + 1];
////            startIndex += 2;
////            if (startIndex > numbers.length - 1) {
////
////            }
////            return addArray2(numbers);
////        }
//    }
}

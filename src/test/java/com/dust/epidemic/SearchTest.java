package com.dust.epidemic;

import java.util.Arrays;
import java.util.Random;

public class SearchTest {

    private static int find(int[] nums, int k, int len) {
        int l = -1, r = len;
        int mid;
        while (l + 1 < r) {
            mid = (l + r) / 2;
            if (nums[mid] == k) {
                return mid;
            } else if (nums[mid] < k) {
                l = mid;
            } else {
                r = mid;
            }
        }
        return r;
    }

    public static void main(String[] args) {
        int[] nums = new int[10];
        int len = 0;
        Random random = new Random();
        for (int i = 0; i < 11; i++) {
            int n = random.nextInt(22);
            int m = find(nums, n, len);
            if (nums[m] != n) {
                System.arraycopy(nums, m, nums, m + 1, len - m);
                nums[m] = n;
                len++;
            }
        }

        System.out.println(Arrays.toString(nums));
    }

}

package com.github.gacourse.util;

import com.sun.deploy.util.StringUtils;

import java.util.Arrays;

/**
 * @功能描述:
 * @author: 邪桑子
 * @date: 2019/10/15 17:16
 */
public class GAUtil {
    /**
     * 功能描述：早上，下午，晚上连上编码
     *
     * @author 邪桑子
     * @date 2019/10/15 17:21
     * @param morning 早上节次
     * @param afternoon  下午节次
     * @param night 晚上节次
     * @return void
     */
    public static void initSectionCode(int morning, int afternoon, int night) {
        if (morning >= afternoon && afternoon >= night) {
            int total = morning + afternoon + night;
            //最大为max节连上
            int max = morning;
            //早上
            gen(max, 0, morning, total, 1);
            //下午
            gen(max, morning, morning + afternoon, total, 2);
            //晚上
            gen(max, morning + afternoon, total, total, 3);
        }

    }

    /**
     * 功能描述：
     *
     * @author 邪桑子
     * @date 2019/10/16 9:39
     * @param max 最大连上节次
     * @param start 连上范围开始位置
     * @param end 连上范围结束位置
     * @param total 每天课程总节次
     * @param type 1-早上，2-下午，3-晚上
     * @return void
     */
    private static void gen(int max, int start, int end, int total, int type) {
        String[] sections = new String[end - start];
        for (int j = 0; j < end - start; j++) {
            sections[j] = "0";
        }

        for (int i = 1; i <= max; i++) {
            //i表示连上节次
            if (end - start >= i) {
                String pre = "";
                for (int j = 0; j < start; j++) {
                    pre += "0";
                }

                String next = "";
                for (int j = end; j < total; j++) {
                    next += "0";
                }

                for (int j = 0; j < sections.length; j = j + i) {
                    String[] mid = sections.clone();
                    int count = 0;
                    for (int k = j; k < j + i; k++) {
                        if (k < sections.length) {
                            mid[k] = "1";
                            count = count + 1;
                        }
                    }
                    if (count != i) {
                        mid = sections.clone();
                        for (int k = 0; k < i; k++) {
                            mid[sections.length - k - 1] = "1";
                        }
                    }
                    String result = pre + StringUtils.join(Arrays.asList(mid), "") + next;
                    String[] arr = result.split("");
                    for (int k = 0; k < arr.length; k++) {
                        if (arr[k].equals("1")) {
                            arr[k] = "" + (k + 1) + ",";
                        } else {
                            arr[k] = "";
                        }
                    }
                    String scts = StringUtils.join(Arrays.asList(arr), "");
                    scts = scts.substring(0, scts.length() - 1);
                    String tp = "";
                    switch (type) {
                        case 1:
                            tp = "早上";
                            break;
                        case 2:
                            tp = "下午";
                            break;
                        case 3:
                            tp = "晚上";
                            break;
                        default:
                            break;
                    }
                    System.out.println(result + "  " + i + "连上，" + tp + "，第" + scts + "节");
                }

            }
        }
    }

}

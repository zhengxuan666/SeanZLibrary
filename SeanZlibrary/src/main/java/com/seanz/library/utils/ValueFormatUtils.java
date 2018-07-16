package com.seanz.library.utils;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Catch on 2018/3/13.
 */

public class ValueFormatUtils {
    /**
     * 格式化数据
     *
     * @param value
     * @param separator 是否加分隔符(,)
     * @param convert   是否进行万 的转换
     * @return
     */

    public static String formatValue(Double value, boolean separator, boolean convert) {
        if (value == null) {
            return "——";
        }
        String flag = "";
        if (value < 0) {
            flag = "-";
        }
        String str = "";
        DecimalFormat df = new DecimalFormat(separator ? "#,##0.00" : "######0.00");
        value = Math.abs(value);
        try {
            if (value < 100000 || !convert) {
                str = df.format(value);
            } else {//if (value < 100000000)
                double v = 0;
                v = value / 10000;
                str = df.format(v) + "万";
            }
//            else {
//                double v = 0;
//                v = value / 100000000;
//                str = df.format(v) + "亿";
//            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return flag + str;
    }


    public static String formatValue(Object value) {
        if (value != null && !"——".equals(value)) {
            return formatValue(value, false);
        } else {
            return "——";
        }
    }

    public static String formatValue(Object value, boolean separator) {
        if (value != null) {
            if (value instanceof String) {
                return formatValue((String) value, separator, true);
            } else if (value instanceof Double) {
                return formatValue((Double) value, separator, true);
            } else if (value instanceof Float) {
                return formatValue((Float) value, separator, true);
            } else if (value instanceof Integer) {
                return formatValue((Integer) value, separator, true);
            } else {
                return value.toString();
            }
        } else {
            return "——";
        }
    }

    public static String formatValue(String string, boolean separator, boolean convert) {
        if (TextUtils.isEmpty(string)) {
            return "——";
        }
        if (string.contains(",")) return string;
        Double df = null;
        try {
            df = Double.parseDouble(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return formatValue(df, separator, convert);
    }


    public static String countFormatValue(String value) {
        if (TextUtils.isEmpty(value)) {
            return "——";
        }
        long v = Long.parseLong(value);
        return countFormatValue(v, false);
    }

    public static String countFormatValue(String value, boolean convert) {
        if (TextUtils.isEmpty(value)) {
            return "——";
        }
        long v = Long.parseLong(value);
        return countFormatValue(v, convert);
    }


    public static String countFormatValue(Long value) {
        if (value == null) {
            return "——";
        }
        return countFormatValue(value, false);
    }

    public static String countFormatValue(Long value, boolean convert) {
        if (value == null) {
            return "——";
        }
        String str = value + "";
        if (convert) {
            DecimalFormat df = new DecimalFormat("######0.00");
            //if (value < 100000000)
            if (value > 100000) {
                double v = 0;
                v = value / 10000d;
                str = df.format(v) + "万";
            }
        }
//        String str = "";
//        DecimalFormat df = new DecimalFormat("######0.00");
//        if (value < 100000000) {
//            str = value + "";
//        } else {
//            double v = 0;
//            v = value / 100000000;
//            str = df.format(v) + "亿";
//        }
//
//        return str;
        return str;
    }

    public static String macdKdjValueFormat(Float value) {
        if (value == null) {
            return "——";
        }
        String str = "";
        DecimalFormat df = new DecimalFormat("######0.0000");
/*        if (value < 100000000) {
            str = value + "";
        } else {
            double v = 0;
            v = value / 100000000;
            str = df.format(v) + "亿";
        }*/

        str = df.format(value);
        return str;
    }

    public static String nullformatValue(String value) {
        if (TextUtils.isEmpty(value)) {
            return "——";
        }
        if ("0".equals(value)) {
            return "——";
        } else {
            return formatValue(value);
        }
    }

    public static String formatValue(Integer value, boolean separator, boolean convert) {
        if (value == null) {
            value = 0;
        }
        return formatValue(Double.parseDouble(value + ""), separator, convert);
    }

    public static String formatValue(Float value, boolean separator, boolean convert) {
        if (value == null) {
            value = 0f;
        }
        return formatValue(Double.parseDouble(value + ""), separator, convert);
    }

    public static double numberROUND_CEILING(double v) {
        BigDecimal bigDecimal = new BigDecimal(v);
        return bigDecimal.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();

    }
}

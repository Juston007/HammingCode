public class HammingCode {

    /**
     * @author Juston
     * @date 2021/04/06 21:05
     */

    public static void main(String[] args) {
        System.out.printf("%s check hammingCode %s\n", "0100011", HammingCode.check("0100111"));

        int data = 5, dataBitNumber = 4;
        String resStr = HammingCode.enCode(data, dataBitNumber);
        System.out.printf("\n\n");
        System.out.printf("raw data %s\n", fillZeroInLeft(data, dataBitNumber));
        System.out.printf("hammingCode %s\n", resStr);
        System.out.printf("decode data %s\n", deCode(resStr));
    }

    //获取校验位个数
    public static int getDetectionBitNumber(int dataBitNumber) {
        int k = 0;
        while (!((int) Math.pow(2, k) >= dataBitNumber + k + 1)) {
            k++;
        }
        return k;
    }

    //编码
    public static String enCode(int data, int dataBitNumber) {
        char[] dataArray = fillZeroInLeft(data, dataBitNumber).toCharArray();

        //计算所需要的校验位的个数
        int detectionBitNumber = getDetectionBitNumber(dataBitNumber);
        //总共需要的数据位
        int totalBitNumber = detectionBitNumber + dataBitNumber;

        //System.out.printf("total Number %d     dataArray %s\n", totalBitNumber, dataStr);

        char[] resultArray = new char[totalBitNumber];

        int C = 0;

        //先把所有数据位放到对应的位置
        for (int i = 1, j = 0, k = 0; i <= totalBitNumber; i++) {
            //System.out.printf("i%d j%d 2^(i-1)%d\n", i, j, (int) Math.pow(2, k));
            //2的k次方不放数据位
            if (i == (int) Math.pow(2, k)) {
                k++;
                continue;
            }

            //把值为1数据位的编号彼此异或就是校验结果
            if (dataArray[j] == '1') {
                if (i == 0) {
                    C = i;
                } else {
                    C ^= i;
                }
            }

            resultArray[i - 1] = dataArray[j++];
        }


        //System.out.printf("\n\n");

        //把计算好的校验位放到对应位置上去
        char[] cArray = fillZeroInLeft(C, detectionBitNumber).toCharArray();
        for (int i = 1, j = 0; i <= resultArray.length; i++) {
            if (i == (int) Math.pow(2, j)) {
                //System.out.printf("resultArray[%d] = %c\n", i - 1, cArray[j]);
                resultArray[i - 1] = cArray[j++];
                continue;
            }
        }

        return String.valueOf(resultArray);
    }

    //解码
    public static String deCode(String hammingCode) {

        //检验汉明码是否正确


        char[] codeArray = hammingCode.toCharArray();

        String data = "";
        for (int i = 1, k = 0; i <= codeArray.length; i++) {
            if (i == (int) Math.pow(2, k)) {
                k++;
                continue;
            }

            data = data + codeArray[i - 1];
        }

        return data;
    }

    //计算检验位
    public static String check(String hammingCode) {

        char[] codeArray = hammingCode.toCharArray();

        int detectionBitNumber = 0;

        for (int i = 1; i <= codeArray.length; i++) {
            if (i == (int) Math.pow(2, detectionBitNumber)) {
                detectionBitNumber++;
            }
        }

        //System.out.printf("detectionBitNumber %d\n", detectionBitNumber);

        String checkResultStr = "";

        //几个校验码就有几组
        for (int i = 0; i < detectionBitNumber; i++) {
            //System.out.printf("i%d\n", i);
            boolean isFirst = true;
            //校验码
            int C = 0;

            //System.out.printf("\n\n");

            //遍历汉明码的所有位
            for (int j = 1; j <= codeArray.length; j++) {
                //把j转换为字符串并检测从右往左第i位是否为1
                char[] jArray = fillZeroInLeft(j, detectionBitNumber).toCharArray();
                //System.out.printf("jArray %s\n", fillZeroInLeft(j, detectionBitNumber));
                //对编号从右往左第i位为1的数据位进行异或运算 算出来的就是该组的检验结果
                if (jArray[i] == '1') {
                    if (isFirst) {
                        isFirst = false;
                        C = (codeArray[j - 1] == '1') ? 1 : 0;
                    } else {
                        C ^= (codeArray[j - 1] == '1') ? 1 : 0;
                    }
                }
            }

            //System.out.printf("P%d = %d\n", (int) Math.pow(2, i), C);

            //每个组的检验结果拼接起来就可以知道哪一位在传送中发生了错误
            checkResultStr += C;
        }

        return checkResultStr;
    }


    //从左边填充0
    public static String fillZeroInLeft(int value, int count) {
        String valueStr = Integer.toBinaryString(value);
        while (valueStr.length() < count)
            valueStr = "0" + valueStr;
        return valueStr;
    }
}

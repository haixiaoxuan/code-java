import java.util.HashMap;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/7/25.
 */
public class JavaBaseTest3 {

    public static HashMap<String, Long> map = new HashMap<String, Long>();

    public static void main(String[] args){

        long start = System.currentTimeMillis();
        String str = "11111111100011000110011111100";
        System.out.println(process(str));
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + " ms");
    }


    public static long process(String str){
        int size = str.length();
        long value = Long.parseLong(str, 2);
        // 取走之后对应的bit变为0
        long flag = (long) Math.pow(2, size) - 1;

        return start(value, flag, size);
    }


    public static long start(long value, long flag, int size){
        if(value == 0){
            if(flag == 0){
                return 1;
            }else{
                return 0;
            }
        }

        long res = 0;

        for(int i = 0; i < size; i++){
            if(getValueByIndex(value, i) == 0 || getValueByIndex(flag, i) == 0){
                continue;
            }else{

                long flagTmp = setZero(flag, i);
                long valueTmp = setZero(value, i);
                long[] valueAndLeftAndRight = reverseLeftAndRight(valueTmp, flagTmp, i, size);
                String[] valueStr = transformLongToString(valueAndLeftAndRight[0], flagTmp, size);
                if(map.containsKey(valueStr[0])){
                    res += map.get(valueStr[0]);
                }else if(map.containsKey(valueStr[1])){
                    res += map.get(valueStr[1]);
                }else{
                    long tmp = start(valueAndLeftAndRight[0], flagTmp, size);
                    res += tmp;
                    map.put(valueStr[0], tmp);
                    map.put(valueStr[1], tmp);
                }
            }

        }

        return res;
    }


    public static String[] transformLongToString(long value, long flag, int size){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i++){
            if(getValueByIndex(flag, i) == 1){
                sb.append(getValueByIndex(value, i));
            }
        }

        return new String[]{sb.toString(), sb.reverse().toString()};
    }




    // 将对应 bit 设置为 1
    public static long setOne(long value, int index){
        return value | (1 << index);
    }

    public static long setZero(long value, int index){
        if(getValueByIndex(value, index) == 0){
            return value;
        }
        return value - (1 << index);
    }


    // 得到对应bit上的值
    public static int getValueByIndex(long value, int index){
        return (value & (1 << index)) == 0 ? 0 : 1;
    }



    // 将index处值进行反转(0变为1，1变为0)
    public static long reverse(long value, int index){
        if(getValueByIndex(value, index) == 0){
            return setOne(value, index);
        }else{
            return value - (1 << index);
        }
    }


    /**
     * 将index两边的硬币翻转
     * 返回左右两侧硬币的角标
     */
    private static long[] reverseLeftAndRight(long value, long flag, int index, int size){

        int left = index - 1;
        int right = index + 1;

        while(left >= 0){
            if(getValueByIndex(flag, left) == 1){
                value = reverse(value, left);
                break;
            }
            left--;
        }

        while(right < size){
            if(getValueByIndex(flag, right) == 1){
                value = reverse(value, right);
                break;
            }
            right++;
        }

        return new long[]{value, left, right};
    }
}

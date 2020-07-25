import java.util.HashMap;

/**
 * description
 *
 *
 *
 *
 * <p>
 * Created by xiexiaoxuan on 2020/7/3.
 */
public class JavaBaseTest2 {

    public static HashMap<String, Long> map = new HashMap<String, Long>();


    public static void main(String[] args){

        long start = System.currentTimeMillis();
        String s = "11111111100011000110011111100";
        long res = process(s);
        System.out.println(res);
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + " ms");
    }


    public static long process(String s){
        if(s == null || s.length() == 0){
            return 0;
        }

        // 0 用来标记反面，1用来标记正面
        byte[] arr = new byte[s.length()];
        // false 表示存在，true表示取出
        boolean[] flag = new boolean[s.length()];
        // 朝上的硬币数量
        int count = 0;


        for(int i = 0; i < s.length(); i++){

            if(s.charAt(i) != '1' && s.charAt(i) != '0'){
                System.out.println("数据格式错误！");
                return 0;
            }

            if('1' == (s.charAt(i))){
                arr[i] = 1;
                count++;
            }
        }

        return start(arr, flag, arr.length, count);
    }


    /**
     *
     * @param num 剩余的没有拿走的硬币个数
     * @param count 剩余正面朝上个数
     * @return
     */
    private static long start(byte[] arr, boolean[] flag, int num, int count){

        if(count <= 0){
            if(num == 0){
                return 1;
            }else{
                return 0;
            }
        }


        // 最终所有可能的情况
        long res = 0;

        for(int i = 0; i < arr.length; i++){
            if(0 == arr[i] || flag[i]){
                continue;
            }else{

                /**
                 * 如果正面朝上
                 *      step1, flag index 处变为true
                 *      step2, arr index 左右两边取反（需要考虑左右是否越界，以及是否为false）
                 *      step3, 递归计算
                 */
                flag[i] = true;
                int[] leftAndRightIndex = reverseLeftAndRight(arr, flag, i);


                String[] mapKeys = transformArrToString(arr, flag);
                if(map.containsKey(mapKeys[0])){
                    res += map.get(mapKeys[0]);
                }else if(map.containsKey(mapKeys[1])){
                    res += map.get(mapKeys[1]);
                }else{
                    long currentRes = start(arr, flag, num-1, count + countUpperNum(arr, leftAndRightIndex) - 1);
                    map.put(mapKeys[0], currentRes);
                    map.put(mapKeys[1], currentRes);
                    res += currentRes;
                }

                flag[i] = false;
                if(leftAndRightIndex[0] >= 0){
                    reverse(arr, leftAndRightIndex[0]);
                }
                if(leftAndRightIndex[1] < arr.length){
                    reverse(arr, leftAndRightIndex[1]);
                }
            }
        }

        return res;
    }


    private static String[] transformArrToString(byte[] arr, boolean[] flag){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < arr.length; i++){
            if(!flag[i]){
                sb.append(arr[i]);
            }
        }
        return new String[]{sb.toString(), sb.reverse().toString()};
    }



    /**
     * 统计经过翻转之后还有多少头朝上的硬币
     */
    private static int countUpperNum(byte[] arr, int[] leftAndRightIndex){
        int res = 0;
        for(int index: leftAndRightIndex){
            if(index >= 0 && index < arr.length) {
                if(1 == arr[index]){
                    res++;
                }else{
                    res--;
                }
            }
        }
        return res;
    }



    /**
     * 将index两边的硬币翻转
     * 返回左右两侧硬币的角标
     */
    private static int[] reverseLeftAndRight(byte[] arr, boolean[] flag, int index){

        int left = index - 1;
        int right = index + 1;

        while(left >= 0){
            if(!flag[left]){
                reverse(arr, left);
                break;
            }
            left--;
        }

        while(right < arr.length){
            if(!flag[right]){
                reverse(arr, right);
                break;
            }
            right++;
        }

        return new int[]{left, right};
    }


    /**
     * 将index处的硬币翻转
     */
    private static void reverse(byte[] arr, int index){
        if(0 == arr[index]){
            arr[index] = 1;
        }else{
            arr[index] = 0;
        }
    }


}

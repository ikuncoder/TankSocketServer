package randamMap;

import com.kesar.a.KeSarStart;
import tank.TankLevel;
import tank.TankWorld;

import java.io.*;
import java.sql.SQLOutput;
import java.util.Random;

/*开发思路：
*1）先将level.txt的文件读取出来，然后将空格置换为0，其他数字不变
* 2）然后将0随机变为2，其他不变
* 3）将生成的地图保存为Secondlevel.txt,同时将地图传输给各个客户端，服务端的地图就以这个为基础
* 4）客户端将地图保存为Secondlevel.txt，然后客户端的地图就以这个为基础进行
* */
public class RandomMap {
    static int w,h;
    static  KeSarStart keSarStart=new KeSarStart();
    public static void main(String[] args){
       /* int[][]map=getChangeMap();
        KeSarStart.printMap(map);*/
        //this.getClass().getResource();
        //System.out.println(Class.class.getClass().getResource("Resources/level.txt"));
        //System.out.println(TankWorld.class.getResource("Resources/level.txt"));
        //TankWorld.class.getResource("Resources/level.txt");
        //C:\Users\LSK2CGH\Desktop\lsk\TankServer8-01\src\main\resources\tank\Resources
        RandomMap randomMap=new RandomMap();
        randomMap.outPutMap();
    }

    /*生成随机地图*/
    public  int[][] getChangeMap(){
        GenerateMap generateMap=new GenerateMap();
        TankLevel level = new TankLevel("Resources/level.txt");
        int mapLength = level.w;
        int mapHight = level.h;
        //生成随机地图
        //int[][] randomMap=generateMap.GenerateMapFunction(mapHight/2,mapLength/2);
        BufferedReader bufferedReader;
       int[][] map = new int[mapHight][mapLength];
        String filename="Resources/level.txt";
        String line;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(TankWorld.class.getResource(filename).openStream()));
            line = bufferedReader.readLine();
            w = line.length();
            h = 0;
            Random random=new Random();
            int j=random.nextInt(6);
            while (line != null) {
                for (int i = 0, n = line.length(); i < n; i++) {
                    char c = line.charAt(i);
                    if(c==32){//空格
                        //随机生成2
                        if((h+i+j)%3==1){
                            map[h][i]=2;
                        }else{
                            map[h][i]=0;
                        }
                    }else{
                        map[h][i]=Character.getNumericValue(c);
                    }
                }
                h++;
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return map;
    }

   /* 将随机地图输出到newlevel.txt*/
    public  void outPutMap(){
        int[][]map=getChangeMap();
        //String  filename="C:/Users/LSK2CGH/Desktop/lsk/TankServer8-01/src/main/resources/tank/Resources/newlevel.txt";
        String parent="C:/Users/LSK2CGH/Desktop/lsk/TankServer8-01/src/main/resources/tank/Resources/";
        String childName="newlevel.txt";
        try {
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(parent,childName))));
            for(int i=0;i<map.length;i++){
                for(int j=0;j<map[i].length;j++){
                    bufferedWriter.write(map[i][j]+"");
                }
                //换行
                bufferedWriter.write("\n");
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}

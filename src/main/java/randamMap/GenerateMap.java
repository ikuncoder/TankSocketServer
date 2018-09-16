package randamMap;

import java.util.ArrayList;
import java.util.Random;

public class GenerateMap {

    public int[][] GenerateMapFunction(int r,int c)
    {
        int[][] a = init(r,c);
        process(a);
        return a;
    }
    //初始化数组
    public int[][] init(int r,int c)
    {
        int[][] a=new int[2*r+1][2*c+1];
        //全部置1
        for(int i=0,len=a.length;i<len;i++)
        {
            for(int j=0;j<a[i].length;j++){
                a[i][j]=1;
            }
        }
        //中间格子为0
        for(int i=0;i<r;i++)
            for(int j=0;j<c;j++)
            {
                a[2*i+1][2*j+1] = 0;
            }
        return a;
    }

    //处理数组，产生最终的数组
    public void process(int[][] arr) {
        //acc存放已访问队列，noacc存放没有访问队列
        //int[] acc = new int[4];
        //int[] noacc = new int[4];
        //ArrayDeque<Integer> acc=new ArrayDeque<>();
        ArrayList<Integer> acc=new ArrayList<>();
        ArrayList<Integer> noacc=new ArrayList<>();
        //ArrayDeque<Integer> noacc=new ArrayDeque<>();
        int r = arr.length>>1,c=arr[0].length>>1;
        int count = r*c;
        for(int i=0;i<count;i++){
            //noacc[i]=0;
            //noacc.set(i,0);
            noacc.add(0);
        }
        //定义空单元上下左右偏移
        //String[] offs={"-c","c","-1","1"};
        int[] offs={-c,c,-1,1};
        int[] offR={-1,1,0,0};
        int[] offC={0,0,-1,1};
        //随机从noacc取出一个位置
        Random random=new Random();
        int pos = random.nextInt(count)+1;
        //noacc[pos]=1;
        noacc.set(pos,1);
        acc.add(pos);
        //acc.remove(pos);
        //acc.push(pos);
        while(acc.size()<count)
        {
            int ls = -1;
            int offPos = -1;
            //找出pos位置在二维数组中的坐标
            int pr = pos/c|0,pc=pos%c,co=0,o=0;
            //随机取上下左右四个单元
            while(++co<5)
            {
                Random random1=new Random();
                //o = MathUtil.randInt(0,5);
                o=random1.nextInt(4);
                ls =offs[o]+pos;
                int tpr = pr+offR[o];
                int tpc = pc+offC[o];
                if(tpr>=0&&tpc>=0&&tpr<=r-1&&tpc<=c-1&&noacc.get(ls)==0){ offPos = o;break;}
            }
            if(offPos<0)
            {
                Random random1=new Random();

                pos=acc.get(random1.nextInt(acc.size()));
                //pos = acc[MathUtil.randInt(acc.size())];
                // pos = acc[MathUtil.randInt(acc.length)];
            }
            else
            {
                pr = 2*pr+1;
                pc = 2*pc+1;
                //相邻空单元中间的位置置0
                arr[pr+offR[offPos]][pc+offC[offPos]]=0;
                pos = ls;
                //noacc[pos] = 1;
                noacc.set(pos,1);
                acc.add(pos);
                //acc.remove(pos);
                //acc.push(pos);
            }
        }
    }
}

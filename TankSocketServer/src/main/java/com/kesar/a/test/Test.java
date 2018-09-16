package com.kesar.a.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.kesar.a.*;

import tank.TankLevel;
import tank.TankWorld;

public class Test
{
	static BufferedReader bufferedReader;
	static String line;
	static int w;
	static int h;
	
	public static void main(String[] args) throws IOException
	{
		//拿到地图的长和宽
		TankLevel level=new TankLevel("Resources/level.txt");
		int mapLength=level.w;
		int mapHight=level.h;
		int Aiplayer3X = 0;
		int Aiplayer3Y=0;
		int player1X=0;
		int player1Y=0;
		int[][]map=new int[mapHight][mapLength];
		//读取地图
		bufferedReader = new BufferedReader(new InputStreamReader(TankWorld.class.getResource("Resources/level.txt").openStream()));
        line = bufferedReader.readLine();
        w = line.length();
        h = 0;
        while (line != null) {
        	 for (int i = 0, n = line.length(); i < n; i++) {
                 char c = line.charAt(i);
                 if(c=='1'||c=='3'||c=='4'||c=='5'||c=='6'||c=='7') {
                	 map[h][i]=Character.getNumericValue(c);
                	 if(c=='3') {
                		 player1X=i;
                		 player1Y=h;
                	 }else if(c=='6') {
                		 Aiplayer3X=i;
                		 Aiplayer3Y=h;
                	 }
                 }else if (c=='2') {//将2变为1
					map[h][i]=1;
				} else {//空设为0
					map[h][i]=0;
				}
        	 }
            h++;
            line = bufferedReader.readLine();
        }
        
        printMap(map);
        MapInfo info=new MapInfo(map,map[0].length, map.length,new Node(Aiplayer3X,Aiplayer3Y), new Node(player1X,player1Y));
		new AStar().start(info,8);
		printMap(map);
        
		/*int[][] maps = { 
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0 },
				{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
				{ 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 } 
				};
		
		MapInfo info=new MapInfo(maps,maps[0].length, maps.length,new Node(1, 5), new Node(10, 5));
		new AStar().start(info);
		printMap(maps);*/
	}
	
	/**
	 * 鎵撳嵃鍦板浘
	 */
	public static void printMap(int[][] maps)
	{
		for (int i = 0; i < maps.length; i++)
		{
			for (int j = 0; j < maps[i].length; j++)
			{
				System.out.print(maps[i][j] + " ");
			}
			System.out.println();
		}
	}

}

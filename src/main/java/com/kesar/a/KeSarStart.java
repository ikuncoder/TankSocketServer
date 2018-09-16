package com.kesar.a;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import tank.TankLevel;
import tank.TankWorld;

public class KeSarStart{
	public BufferedReader bufferedReader;
	public String line;
	public int w;
	public int h;
	TankLevel level = new TankLevel("Resources/level"+TankWorld.getInstance().mapNum+".txt");
	int mapLength = level.w;
	int mapHight = level.h;
	public int[][] map = new int[mapHight][mapLength];
	
	
	public int[][] GetMap(int player1X, int player1Y,int Aiplayer3X,int Aiplayer3Y,int path) {
		// 拿到地图的长和宽
		// 读取地图
		try {
			bufferedReader = new BufferedReader(
					new InputStreamReader(TankWorld.class.getResource("Resources/level"+TankWorld.getInstance().mapNum+".txt").openStream()));
			line = bufferedReader.readLine();
			w = line.length();
			h = 0;
			while (line != null) {
				for (int i = 0, n = line.length(); i < n; i++) {
					char c = line.charAt(i);
					if (c == '1' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7') {
						map[h][i] = Character.getNumericValue(c);
					} else if (c == '2') {// 将2变为1
						map[h][i] = 1;
					} else {// space设为0
						map[h][i] = 0;
					}
				}
				h++;
				line = bufferedReader.readLine();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MapInfo info = new MapInfo(map, map[0].length, map.length, new Node(Aiplayer3X, Aiplayer3Y),
				new Node(player1X, player1Y));
		new AStar().start(info,path);
		return map;
	}

	/**
	 * 鎵撳嵃鍦板浘
	 */
	public static void printMap(int[][] maps) {
		for (int i = 0; i < maps.length; i++) {
			for (int j = 0; j < maps[i].length; j++) {
				System.out.print(maps[i][j] + " ");
			}
			System.out.println();
		}
	}

}

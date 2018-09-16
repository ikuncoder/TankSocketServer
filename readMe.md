<<<<<<< HEAD
# 坦克大战网络版服务端

## 功能

1)接收客户端传来的动作指令，进行逻辑处理<br>
2)处理完接收到在指令后，将坦克的状态广播给两个客户端，包括位置，方向，血量，分数值等<br>



# 坦克大战

## 网络版

本目录包含三个工程:<br>
1)TankSocketClient是网络版的坦克大战的客户端，主要是界面的显示<br>
2)TankSocketServer是网络版的坦克大战的服务端，主要是做逻辑的判断<br>
3)tank-master是单机版坦克大战，网络版的坦克大战就是基于这个单机版进行二次开发的<br>

主类：tank.TankWorld

## 主要实现的功能

+ 支持1v1网络对战
+ AI坦克
+ 随机地图生成

## tips<br>
1)TankSocketServer是网络版坦克大战的服务端,需要先启动，启动类是lskServer.SocketServer<br>
2)TankSocketClient是网络版坦克大战的客户端,需要启动两次，启动类是lskClient.SocketClient<br>
3)Tank-master是单机版坦克大战<br>

## 协议
### 客户端和服务端之间用的是原生的socket进行通讯
### 协议格式为：
1)服务端——>客户端：sign+playerId+player.x+player.y+direction+live+health+score+strength+respawnCounter<br>
2)客户端——>服务端：sign+playerId+order<br>

## 其他
1)AI寻路用的是A*算法<br>
2)随机地图是根据服务端传给客户端的地图数字选择地图(客户端本来就存在地图)<br>







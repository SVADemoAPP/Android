package com.hck.imagemap.utils;
import java.util.ArrayList;

public class Dijkstra
{

    public static ArrayList<String> dijkstra(int[][] W1, int start, int end)
    {

       // System.out.println("锟斤拷锟�:" + start + "锟秸碉拷:" + end);
        boolean[] isLabel = new boolean[W1[0].length];// 锟角凤拷锟斤拷
        int[] indexs = new int[W1[0].length];// 锟斤拷锟叫憋拷诺牡锟斤拷锟铰标集锟较ｏ拷锟皆憋拷诺锟斤拷群锟剿筹拷锟斤拷锟叫存储锟斤拷实锟斤拷锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷锟斤拷锟绞撅拷锟秸�
        int i_count = -1;// 栈锟侥讹拷锟斤拷
        int[] distance = W1[start].clone();// v0锟斤拷锟斤拷锟斤拷锟斤拷锟教撅拷锟斤拷某锟绞贾�
        int index = start;// 锟接筹拷始锟姐开始
        int presentShortest = 0;// 锟斤拷前锟斤拷时锟斤拷叹锟斤拷锟�

        indexs[++i_count] = index;// 锟斤拷锟窖撅拷锟斤拷诺锟斤拷卤锟斤拷锟斤拷锟铰标集锟斤拷
        isLabel[index] = true;

        while (i_count < W1[0].length)
        {
            // 锟斤拷一锟斤拷锟斤拷锟矫碉拷锟斤拷原锟斤拷锟斤拷锟斤拷锟侥筹拷锟斤拷锟�
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < distance.length; i++)
            {
                if (!isLabel[i] && distance[i] != -1 && i != index)
                {
                    // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟叫憋拷,锟斤拷锟斤拷没锟叫憋拷锟斤拷锟�
                    if (distance[i] < min)
                    {
                        min = distance[i];
                        index = i;// 锟斤拷锟铰憋拷锟轿拷锟角帮拷卤锟�
                    }
                }
            }
            i_count = i_count + 1;
            if (i_count == W1[0].length)
            {
                break;
            }
            isLabel[index] = true;// 锟皆碉拷锟斤拷斜锟斤拷
            indexs[i_count] = index;// 锟斤拷锟窖撅拷锟斤拷诺锟斤拷卤锟斤拷锟斤拷锟铰标集锟斤拷

            if (W1[indexs[i_count - 1]][index] == -1
                    || presentShortest + W1[indexs[i_count - 1]][index] > distance[index])
            {
                // 锟斤拷锟斤拷锟斤拷锟斤拷锟矫伙拷锟街憋拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷路锟斤拷锟斤拷锟斤拷锟斤拷锟铰凤拷锟�
                presentShortest = distance[index];
            } else
            {
                presentShortest += W1[indexs[i_count - 1]][index];
            }

            // 锟节讹拷锟斤拷锟斤拷锟斤拷锟斤拷vi锟斤拷锟斤拷锟铰硷拷锟斤拷distance锟叫的撅拷锟斤拷
            for (int i = 0; i < distance.length; i++)
            {

                // 锟斤拷锟絭i锟斤拷锟角革拷锟斤拷锟叫边ｏ拷锟斤拷v0锟斤拷锟斤拷锟斤拷锟侥撅拷锟斤拷锟�
                if (distance[i] == -1 && W1[index][i] != -1)
                {// 锟斤拷锟斤拷锟角帮拷锟斤拷纱铮拷锟斤拷锟斤拷诳纱锟斤拷锟�
                    distance[i] = presentShortest + W1[index][i];
                } else if (W1[index][i] != -1
                        && presentShortest + W1[index][i] < distance[i])
                {
                    // 锟斤拷锟斤拷锟角帮拷纱铮拷锟斤拷锟斤拷诘锟铰凤拷锟斤拷锟斤拷锟角帮拷锟斤拷蹋锟斤拷锟斤拷锟斤拷锟缴革拷锟教碉拷路锟斤拷
                    distance[i] = presentShortest + W1[index][i];
                }

            }

        }
        ArrayList<String> route = getRoute(W1, indexs, end);

        return route;
    }

 /*   public static void main(String[] args)
    {
        // 锟斤拷锟斤拷一锟斤拷权值锟斤拷锟斤拷
        int[][] W1 =  { //锟斤拷锟斤拷锟斤拷锟斤拷1  
                { 0, 1, -1, -1, -1, -1 , -1, -1, -1, -1, -1, -1},  
                { 1, 0,  1, -1,  2, -1,  -1,  6 , -1, -1, 11, -1},  
                { -1, 1, 0,  3, -1,  2,  -1, -1,   6 , -1, -1, -1 },   
                { -1, -1, 3, 0, -1, -1,   2, -1,   -1,   6, -1, -1},  
                { -1, 2, -1, -1, 0, -1, -1, 4 ,    -1,  -1,  9, -1 },   
                { -1, -1, 2, -1, 1, 0 , -1, -1 ,    4, -1, -1, -1} ,
                { -1, -1, -1, 2, -1, -1 , 0, -1 ,   -1, 4, -1, -1} ,
                { -1, -1, -1, -1, 4, -1 , -1, 0 ,   1, -1, 5, -1} ,
                { -1, -1, -1, -1, -1, 4 , -1, 1 ,   0,  3, -1, -1} ,
                { -1, -1, -1, -1, -1, -1 , 4, -1 ,   3,  0, -1, -1} ,
                { -1, -1, -1, -1, -1, -1 , -1, 5 ,  -1, -1, 0, 2} ,
                { -1, -1, -1, -1, -1, -1 , -1, -1 ,  -1, -1, 2, 0} ,
                };  
        // System.out.println("f" + W1[0][4]);

        int[][] W = { // 锟斤拷锟斤拷锟斤拷锟斤拷2
        { 0, 1, 3, 4 }, { 1, 0, 2, -1 }, { 3, 2, 0, 5 }, { 4, -1, 5, 0 } };

        System.out.println(dijkstra(W1, 10, 5)); // (int[][] W1, int start, int
                                                // end)

    }*/

    // indexs:1,0,2,4,3,5 锟脚讹拷锟斤拷锟剿筹拷锟�
    // end:锟斤拷锟揭拷亩锟斤拷锟斤拷锟斤拷锟�:5
    // routeLength:锟斤拷锟斤拷:8
    /**
     * seven 锟斤拷锟铰凤拷锟�(锟斤拷愕斤拷锟斤拷械锟斤拷)
     */
    static ArrayList<String> ap=new ArrayList<String>();
    public static ArrayList<String> getRoute(int[][] WW, int[] indexs, int end)
    {
        ap.clear();
        String[] routeArray = new String[indexs.length];
        for (int i = 0; i < routeArray.length; i++)
        {
            routeArray[i] = "";
        }

        // 锟皆硷拷锟斤拷路锟斤拷
        routeArray[indexs[0]] = indexs[0] + "";
        for (int i = 1; i < indexs.length; i++)
        {
            // 锟斤拷锟矫碉拷锟斤拷前锟斤拷锟斤拷锟叫碉拷锟斤拷锟斤拷锟斤拷锟斤拷械锟斤拷锟斤拷路锟斤拷锟斤拷然锟斤拷玫锟斤拷锟斤拷锟斤拷路锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟侥革拷锟姐，锟斤拷锟斤拷锟剿碉拷锟絩oute锟斤拷锟斤拷锟揭筹拷锟角碉拷锟絩oute+锟剿碉拷
            int[] thePointDis = WW[indexs[i]];
            int prePoint = 0;

            int tmp = 9999;
            for (int j = 0; j < thePointDis.length; j++)
            {

                boolean chooseFlag = false;
                // 锟竭的撅拷锟斤拷锟斤拷蹋锟斤拷锟斤拷遥锟斤拷锟斤拷锟斤拷牡锟斤拷锟角帮拷锟侥点当锟斤拷
                for (int m = 0; m < i; m++)
                {
                    if (j == indexs[m])
                    {
                        chooseFlag = true;
                    }
                }
                if (chooseFlag == false)
                {
                    continue;
                }
                if (thePointDis[j] < tmp && thePointDis[j] > 0)
                {
                    prePoint = j;
                    tmp = thePointDis[j];
                }
            }
            routeArray[indexs[i]] = routeArray[prePoint] +">"+ indexs[i];
        }
        ap.add(routeArray[end]);
       /* for (int i = 0; i < routeArray.length; i++)
        {
            ap.add(routeArray[i]);
           // System.out.println(routeArray[i]+">");
        }*/
        return ap;
    }
}
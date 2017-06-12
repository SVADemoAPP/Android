package com.hck.imagemap.config;

public class GlobalConfig
{

    public static String server_ip = "192.168.1.100";
    public static boolean autoPush = true;
    public static boolean bAnimation = true;
    public static String port = "8082";
    public static String identification = "IP";
    public static String insapi = "API";
    public static int send_time;
    public static int getSend_time() {
		return send_time;
	}

	public static void setSend_time(int send_time) {
		GlobalConfig.send_time = send_time;
	}

	public static String getContent() {
		return content;
	}

	public static void setContent(String content) {
		GlobalConfig.content = content;
	}

	public static String content;
    
    public static double LOCATED = 8; // 5
    
    public static String getInsapi() {
        return insapi;
    }

    public static void setInsapi(String insapi) {
        GlobalConfig.insapi = insapi;
    }

    public static String threshold = "0.2";//静止门限
    public static String weights = "0.04";//权重
    public static String crossWeights = "0.02";//权重
    public static String getCrossWeights() {
		return crossWeights;
	}

	public static void setCrossWeights(String crossWeights) {
		GlobalConfig.crossWeights = crossWeights;
	}

	public static String maxDeviate = "8";//最大偏离
    public static String moreMaxDeviate = "5";//超过最大偏离
    public static String direction = "0";//矫正地图方向
    public static String spr = "10";//滤波窗长
    public static String addShold = "0";//偏执速度
    public static double errorAngle=1;// 静止次数
    public static double getErrorAngle() {
		return errorAngle;
	}

	public static void setErrorAngle(double errorAngle) {
		GlobalConfig.errorAngle = errorAngle;
	}

	public static double getPositioningError() {
		return positioningError;
	}

	public static void setPositioningError(double positioningError) {
		GlobalConfig.positioningError = positioningError;
	}

	public static double getFilterNumber() {
		return filterNumber;
	}

	public static void setFilterNumber(double filterNumber) {
		GlobalConfig.filterNumber = filterNumber;
	}

	public static double positioningError=0.6;//过滤波峰误差
    public static double filterNumber=15;//波峰阈值
    public static double gaussVariance=0.6;//步长
    public static String getAddShold()
    {
        return addShold;
    }

    public static void setAddShold(String addShold)
    {
        GlobalConfig.addShold = addShold;
    }

    public static boolean isEnable=true;
    public static boolean isEnable()
    {
        return isEnable;
    }

    public static void setEnable(boolean isEnable)
    {
        GlobalConfig.isEnable = isEnable;
    }

    public static double getGaussVariance()
    {
        return gaussVariance;
    }

    public static void setGaussVariance(double gaussVariance)
    {
        GlobalConfig.gaussVariance = gaussVariance;
    }

    public static String getSpr()
    {
        return spr;
    }

    public static void setSpr(String spr)
    {
        GlobalConfig.spr = spr;
    }

    public static double proportion;//地图比例
    public static double getProportion()
    {
        return proportion;
    }

    public static void setProportion(double proportion)
    {
        GlobalConfig.proportion = proportion;
    }

    public static double xSport = 0.0d;//x边界
    public static double getxSport()
    {
        return xSport;
    }

    public static void setxSport(double d)
    {
        GlobalConfig.xSport = d;
    }

    public static double getySport()
    {
        return ySport;
    }

    public static void setySport(double ySport)
    {
        GlobalConfig.ySport = ySport;
    }

    public static double ySport = 0.0d;//矫正地图方向

    public static String getThreshold()
    {
        return threshold;
    }

    public static void setThreshold(String threshold)
    {
        GlobalConfig.threshold = threshold;
    }

    public static String getWeights()
    {
        return weights;
    }

    public static void setWeights(String weights)
    {
        GlobalConfig.weights = weights;
    }

    public static String getMaxDeviate()
    {
        return maxDeviate;
    }

    public static void setMaxDeviate(String maxDeviate)
    {
        GlobalConfig.maxDeviate = maxDeviate;
    }

    public static String getMoreMaxDeviate()
    {
        return moreMaxDeviate;
    }

    public static void setMoreMaxDeviate(String moreMaxDeviate)
    {
        GlobalConfig.moreMaxDeviate = moreMaxDeviate;
    }

    public static String getDirection()
    {
        return direction;
    }

    public static void setDirection(String direction)
    {
        GlobalConfig.direction = direction;
    }

    public static void setIdentification(String identification)
    {
        GlobalConfig.identification = identification;
    }

    public static void setPort(String p)
    {
        port = p;
    }

    public static void setServerIp(String ip)
    {
        server_ip = ip;
    }

    public static void setAutoPush(boolean auto)
    {
        autoPush = auto;
    }

    public static void setAnimation(boolean animation)
    {
        bAnimation = animation;
    }
}

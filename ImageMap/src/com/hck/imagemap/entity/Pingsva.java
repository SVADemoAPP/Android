package com.hck.imagemap.entity;

public class Pingsva
{
    /*
     * pingsvalist:{"data":[{"position":["U6食堂"],"id":"3","brokerProt":"4703",
     * "username":"app3",
     * "status":0,"tokenProt":"9001","name":"成都研究所","type":2,"storeId":null,"" +
     * "password":"User@123456","ip":"182.138.104.35"},
     * {"position":["福州奥体体育馆"],"id":"4","brokerProt":"4703","username":"attyg0",
     * "status":0,"tokenProt":"9001","name":"福州奥体","type":1,"storeId":null,
     * "password":"User@123456","ip":"58.22.127.178"},
     * {"position":["2015MBBF会场"]
     * ,"id":"5","brokerProt":"4703","username":"app2",
     * "status":0,"tokenProt":"9001","name":"MBBF","type":1,"storeId":null,
     * "password":"User@123456","ip":"113.28.126.201"},
     * {"position":["王府井"],"id":"6","brokerProt":"4703","username":"app3",
     * "status":1,"tokenProt":"9001","name":"王府井","type":1,"storeId":null,
     * "password":"User@123456","ip":"111.9.25.61"},
     * {"position":["U6食堂"],"id":"7","brokerProt":"4703","username":"app8",
     * "status":1,"tokenProt":"9001","name":"U6","type":0,"storeId":null,
     * "password":"User@123456","ip":"182.138.104.35"}]}
     */

    private String name;
    private String ip;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

}

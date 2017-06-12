package com.hck.imagemap.utils;

import java.io.InputStream;
import java.util.List;

import com.hck.imagemap.entity.Book;

public interface BookParser {  
    /** 
     * 解析输入流 得到Book对象集合 
     * @param is 
     * @return 
     * @throws Exception 
     */  
    public List<Book> parse(InputStream is) throws Exception;  
      
    /** 
     * 序列化Book对象集合 得到XML形式的字符串 
     * @param books 
     * @return 
     * @throws Exception 
     */  
    public String serialize(List<Book> books) throws Exception;
}  
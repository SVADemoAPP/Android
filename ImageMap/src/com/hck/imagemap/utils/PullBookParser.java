package com.hck.imagemap.utils;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.hck.imagemap.entity.Book;

public class PullBookParser implements BookParser
{

    private int linesSize = 0;
    @Override
    public List<Book> parse(InputStream is) throws Exception
    {
        List<Book> books = null;
        Book book = null;

        // XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // XmlPullParser parser = factory.newPullParser();

        XmlPullParser parser = Xml.newPullParser(); // 由android.util.Xml创建一个XmlPullParser实例
        parser.setInput(is, "UTF-8"); // 设置输入流 并指明编码方式
        linesSize = 0;
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            switch (eventType)
            {
            case XmlPullParser.START_DOCUMENT:
                books = new ArrayList<Book>();
                break;
            case XmlPullParser.START_TAG:
                if ("line".equals(parser.getName()))
                {
                    book = new Book();
                    book.setY(parser.getAttributeValue(0));
                    book.setX(parser.nextText());
                    books.add(book);
                    book = null;
                    linesSize++;
                }else if("points".equals(parser.getName()))
                {
                    book = new Book();
                    book.setPointX(parser.getAttributeValue(0));
                    book.setPointY(parser.nextText());
                    books.add(book);
                    book = null;
                }
                break;
            case XmlPullParser.END_TAG:
                /*if (parser.getName().equals("line"))
                {
                    books.add(book);
                    book = null;
                }
                if (parser.getName().equals("points"))
                {
                    books.add(book);
                    book = null;
                }*/
                break;
            }
            eventType = parser.next();
        }
        return books;
    }
    
    public int getLineSize()
    {
        return linesSize;
    }

    @Override
    public String serialize(List<Book> books) throws Exception
    {
        // XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // XmlSerializer serializer = factory.newSerializer();

        XmlSerializer serializer = Xml.newSerializer(); // 由android.util.Xml创建一个XmlSerializer实例
        StringWriter writer = new StringWriter();
        serializer.setOutput(writer); // 设置输出方向为writer
        serializer.startDocument("UTF-8", true);
        serializer.startTag("", "books");
        for (Book book : books)
        {
            serializer.startTag("", "book");

            serializer.startTag("", "x");
            serializer.text(book.getX() + "");
            serializer.endTag("", "name");

            serializer.startTag("", "y");
            serializer.text(book.getY() + "");
            serializer.endTag("", "price");

            serializer.endTag("", "book");
        }
        serializer.endTag("", "books");
        serializer.endDocument();

        return writer.toString();
    }
}
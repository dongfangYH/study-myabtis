package com.study.mybatis.framework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {

    public static void main(String[] args) throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/mybatis?useUnicode=true&characterEncoding=utf8";
        String username = "root";
        String password = "Aa123456";
        Connection connection = DriverManager.getConnection(url, username, password);

        PreparedStatement pst = connection.prepareStatement("select * from user ");
        pst.execute();

        ResultSet rs = pst.getResultSet();

        while (rs.next()){
            Integer id = rs.getInt("id");
            String userName = rs.getString("userName");
            String userAge = rs.getString("userAge");
            String userAddress = rs.getString("userAddress");
            System.out.println(id + " : " + userName + " : " + userAge + " : " + userAddress);
        }

        rs.close();
        pst.close();
        connection.close();
    }

}

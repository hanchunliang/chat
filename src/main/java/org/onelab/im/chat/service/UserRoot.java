package org.onelab.im.chat.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunliangh on 14-10-29.
 */
public class UserRoot {
    private static List<String> userList = new ArrayList<String>();
    public static void add(String userName){
        userList.add(userName);
    }
    public static void remove(String userName){
        userList.remove(userName);
    }
    public static List<String> getUserList(){
        return new ArrayList<String>(userList);
    }

    public static void main(String[] args){
        System.out.println("1".compareTo("2"));
    }
}

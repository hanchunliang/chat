package org.onelab.im.chat.controller;

import com.alibaba.fastjson.JSON;
import org.onelab.im.chat.service.UserRoot;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by chunliangh on 14-10-29.
 */
public class UserController extends HttpServlet {
    //获取所有用户（不包含自己）
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> users = UserRoot.getUserList();
        resp.getWriter().write(JSON.toJSONString(users));
    }
}

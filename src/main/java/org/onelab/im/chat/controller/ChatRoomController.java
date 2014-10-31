package org.onelab.im.chat.controller;

import com.alibaba.fastjson.JSON;
import org.onelab.im.chat.service.Common;
import org.onelab.im.core.DialogPanel;
import org.onelab.im.core.ImEngine;
import org.onelab.im.core.domain.Message;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 公用聊天板
 * Created by chunliangh on 14-10-27.
 */
public class ChatRoomController extends HttpServlet {
    private final String dialogId = "1";
    static {
        ImEngine.createDialog(Common.groupId,"1",null);
    }

    //读取聊天信息
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String index = req.getParameter("index");
        DialogPanel dialogPanel = ImEngine.getDialogPanel(Common.groupId, dialogId);
        List<Message> messageList = dialogPanel.read(Integer.parseInt(index));
        if (messageList==null){
            messageList = Collections.EMPTY_LIST;
        }
        resp.getWriter().write(JSON.toJSONString(messageList));
    }

    //写入聊天信息
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String msg = req.getParameter("text");
        String name = (String) req.getSession().getAttribute("name");
        if (name == null){
            name = "游客";
        }
        Message message = new Message();
        message.setAuthor(name);
        message.setContent(msg);
        DialogPanel dialogPanel1 = ImEngine.getDialogPanel(Common.groupId, dialogId);
        dialogPanel1.write(message);
    }
}

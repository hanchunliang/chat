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
import java.util.*;

/**
 * Created by chunliangh on 14-10-29.
 */
public class ChatController extends HttpServlet {
    //读取聊天信息
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String index = req.getParameter("index");
        String to = req.getParameter("name");
        String from = (String) req.getSession().getAttribute("name");
        if (from==null || to==null){
            resp.getWriter().write("[]");
            return;
        }
        String dialogId = getDialogId(from,to);
        DialogPanel dialogPanel = ImEngine.getDialogPanel(Common.groupId, dialogId);
        List<Message> messageList = dialogPanel.read(Integer.parseInt(index));
        if (messageList==null){
            messageList = Collections.EMPTY_LIST;
        }
        resp.getWriter().write(JSON.toJSONString(messageList));
    }

    private String getDialogId(String from, String to) {
        int a = from.compareTo(to);
        if (a>0){
            return from+":"+to;
        } else {
            return to+":"+from;
        }
    }

    //写入聊天信息
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String msg = req.getParameter("text");
        String to = req.getParameter("name");
        String from = (String) req.getSession().getAttribute("name");
        if (from==null || from.equals("") || to==null || to.equals("")){
            return;
        }
        String dialogId = getDialogId(from,to);
        Message message = new Message();
        message.setType(102);
        message.setAuthor(from);
        message.setContent(msg);
        if (!ImEngine.hasDialog(Common.groupId, dialogId)){
            Map<String,String> info = new HashMap<String, String>();
            info.put("from",from);
            info.put("to",to);
            info.put("time",System.currentTimeMillis()+"");
            ImEngine.createDialog(Common.groupId, dialogId, info);
        }
        DialogPanel dialogPanel = ImEngine.getDialogPanel(Common.groupId, dialogId);
        dialogPanel.write(message);
    }
}

package org.onelab.im.chat.controller;

import com.alibaba.fastjson.JSON;
import org.onelab.im.chat.model.Dialog;
import org.onelab.im.chat.service.Common;
import org.onelab.im.core.Condition;
import org.onelab.im.core.DialogPanel;
import org.onelab.im.core.ImEngine;
import org.onelab.im.core.domain.Message;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 监控
 * Created by chunliangh on 14-10-29.
 */
public class MonitorController extends HttpServlet{
    //查询对话
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        if ("1".equals(type)){
            dialogDetail(req,resp);
        }else{
            dialogList(req,resp);
        }
    }
    //关闭对话
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        String dialogId = req.getParameter("did");
        if ("1".equals(type)){
            if (dialogId!=null&&!dialogId.equals("")){
                String msg = req.getParameter("text");
                DialogPanel dialogPanel = ImEngine.getDialogPanel(Common.groupId,dialogId);
                Message message = new Message();
                message.setType(100);
                message.setAuthor("管理员");
                message.setContent(msg);
                dialogPanel.write(message);
            }
        }else{
            if (dialogId!=null&&!dialogId.equals("")){
                ImEngine.destroyDialog(Common.groupId,dialogId);
            }
        }


    }
    private void dialogDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dialogId = req.getParameter("did");
        String index = req.getParameter("index");
        if (dialogId!=null&&!dialogId.equals("")){
            DialogPanel dialogPanel = ImEngine.getDialogPanel(Common.groupId,dialogId);
            resp.getWriter().write(JSON.toJSONString(dialogPanel.read(Integer.parseInt(index))));
        }
    }
    private void dialogList(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String start = req.getParameter("start");
        String end = req.getParameter("end");
        Condition condition = new Condition() ;
        if (from!=null&&!from.trim().equals("")){
            condition.and(new Condition(Condition.Operator.EQ, "from", from.trim()));
        }
        if (to!=null&&!to.trim().equals("")){
            condition.and(new Condition(Condition.Operator.EQ, "to", to.trim()));
        }
        if (start!=null&&!start.trim().equals("")){
            try {
                start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start).getTime()+"";
                condition.and(new Condition(Condition.Operator.GE,"time",start));
            } catch (ParseException e) {}
        }
        if (end!=null&&!end.trim().equals("")){
            try {
                end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end).getTime()+"";
                condition.and(new Condition(Condition.Operator.LE,"time",end));
            } catch (ParseException e) {}
        }
        List<DialogPanel> dialogPanels = ImEngine.getDialogPanels(Common.groupId, condition);
        List<Dialog> dialogs = new ArrayList<Dialog>();
        for (DialogPanel dialogPanel:dialogPanels){
            Dialog dialog = new Dialog();
            dialog.setFrom(dialogPanel.getDialogInfo().get("from"));
            dialog.setTo(dialogPanel.getDialogInfo().get("to"));
            String time = dialogPanel.getDialogInfo().get("time");
            if (time!=null){
                dialog.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(time))));
            }
            dialog.setId(dialogPanel.getDialogId());
            dialog.setGroup(dialogPanel.getGroup());
            dialogs.add(dialog);
        }
        resp.getWriter().write(JSON.toJSONString(dialogs));
    }
}

package org.onelab.im.chat.controller;

import org.onelab.im.chat.service.UserRoot;
import org.onelab.im.core.Condition;
import org.onelab.im.core.DialogPanel;
import org.onelab.im.core.ImEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by chunliangh on 14-10-29.
 */
public class LoginController extends HttpServlet {
    private final String loginpath = "login.jsp";
    private final String chatgroup = "index.jsp";

    //用户登录
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        try{
            name = name.trim();
            String oldName = (String) req.getSession().getAttribute("name");
            if (!name.equals("")){
                if (oldName!=null){
                    UserRoot.remove(oldName);
                }
                UserRoot.add(name);
                req.getSession().setAttribute("name",name);
                req.getRequestDispatcher(chatgroup).forward(req,resp);
            } else {
                if (oldName!=null){
                    req.getRequestDispatcher(chatgroup).forward(req, resp);
                }else{
                    throw new RuntimeException();
                }
            }
        } catch (Exception e){
            req.getRequestDispatcher(loginpath).forward(req,resp);
        }
    }

    //用户退出
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = (String)req.getSession().getAttribute("name");
        if (name!=null){
            UserRoot.remove(name);
            req.getSession().removeAttribute("name");
            List<DialogPanel> dialogPanelList = ImEngine.getDialogPanels("test_chat",
                    new Condition(Condition.Operator.EQ,"from",name).or(new Condition(Condition.Operator.EQ,"to", name)));
            for (DialogPanel dialogPanel:dialogPanelList){
                ImEngine.destroyDialog(dialogPanel.getGroup(),dialogPanel.getDialogId());
            }
        }
        req.getRequestDispatcher(loginpath).forward(req,resp);
    }
}

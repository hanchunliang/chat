package org.onelab.im.chat.listener;

import com.alibaba.fastjson.JSON;
import org.onelab.im.core.ImConfig;
import org.onelab.im.core.ImEngine;
import org.onelab.im.core.domain.Dialog;
import org.onelab.im.dependence.DialogPersistenceInterface;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

/**
 * Created by chunliangh on 14-10-27.
 */
public class ImListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ImConfig.init(null, new DialogPersistenceInterface() {
            @Override
            public void persist(Dialog dialog) {
                System.out.println(JSON.toJSONString(dialog));
            }

            @Override
            public void persist(List<Dialog> dialogs) {
                System.out.println(JSON.toJSONString(dialogs));
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ImConfig.destroy();
    }
}

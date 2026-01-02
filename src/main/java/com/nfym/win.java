package com.nfym;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.nfym.setting.ShowWindow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class win extends AnAction {

    // 用于跟踪JavaFX应用是否已经启动
    public static final AtomicBoolean JAVAFX_STARTED = new AtomicBoolean(false);

    @Override
    public void actionPerformed(AnActionEvent e) {

        if (JAVAFX_STARTED.compareAndSet(false, true)) {
            // 使用Platform.startup()初始化JavaFX平台
            Platform.startup(() -> {
                Platform.setImplicitExit(false);
            });
        }

        // 在JavaFX应用线程中显示弹窗
        Platform.runLater(() -> {
            // 创建一个简单的JavaFX窗口
            Stage stage = new Stage();
            ShowWindow showWindow = ShowWindow.getInstance();
            try {
                showWindow.show(stage,"枫叶");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

}

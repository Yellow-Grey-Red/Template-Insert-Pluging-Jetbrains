package com.nfym;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.nfym.insert.InsertX;
import com.nfym.setting.ShowWindow;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

public class InsertItem extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        if (win.JAVAFX_STARTED.compareAndSet(false, true)) {
            Platform.startup(() -> {
                Platform.setImplicitExit(false);
            });
        }
        
        Platform.runLater(() -> {
            Stage stage = new Stage();
            InsertX insertX = InsertX.getInstance();
            try {
                insertX.show(stage,"枫叶");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}

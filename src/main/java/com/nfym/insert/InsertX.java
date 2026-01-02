package com.nfym.insert;



import com.nfym.setting.Item;
import com.nfym.setting.Mapper;
import com.nfym.setting.ShowWindow;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

/**
 * abc包应用程序的主控制器类
 * 实现单例模式以方便全局访问
 * 处理UI初始化、事件处理和数据管理
 */
public class InsertX {

    // 单例模式实现，确保全局只有一个实例
    private static InsertX instance = new InsertX();

    // 重命名静态字段以避免与VBox类名冲突
    public static VBox contentVBox = null; // VBox项目的主容器
    public static CheckBox sortCheckBox = null; // 用于排序功能的复选框

    public static boolean hasEmpty = false; // 指示是否有空项目的标志

    // 跟踪当前和上一个选中项的变量
    public static int current = -1; // 当前选中项的索引
    public static int last = -1; // 上一个选中项的索引
    private static String color = "#A0A0A0"; // 选中项的背景颜色

    /**
     * 私有构造函数，防止外部实例化
     */
    private InsertX() {
    }

    /**
     * 获取asds类的单例实例
     * @return 单例实例
     */
    public static InsertX getInstance() {
        return instance;
    }

    /**
     * 显示应用程序窗口
     * @param stage 应用程序的主舞台
     * @param str 舞台的标题
     * @throws IOException 如果加载FXML文件时出错
     */
    public void show(Stage stage, String str) throws IOException {
        // 加载主FXML文件
        Thread.currentThread().setContextClassLoader(InsertX.class.getClassLoader());
        FXMLLoader fxmlLoader = new FXMLLoader(InsertX.class.getResource("/META-INF/insert.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image(String.valueOf(ShowWindow.class.getResource("/META-INF/a.png"))));
        // 设置舞台属性并显示
        stage.setTitle(str);
        stage.setScene(scene);
        stage.show();
        
        // 通过查找ID初始化UI组件
        contentVBox = (VBox) scene.lookup("#content");
        sortCheckBox = (CheckBox) scene.lookup("#sort");
        
        // 将排序事件绑定到handleSort方法
        sortCheckBox.setOnAction(e -> handleSort());
        
        // 通过加载数据和创建VBox项来初始化界面
        init();
    }

    /**
     * 初始化界面，加载数据并创建VBox项
     */
    public void init() {
        try {
            // 调用Mapper.load()加载数据
            if(Mapper.arraylist==null){
                Mapper.load();
            }
            
            // 如果Mapper.arraylist为空，直接返回
            if (Mapper.arraylist == null || Mapper.arraylist.isEmpty()) {
                return;
            }
            
            // 使用加载的数据重建UI
            rebuildUI(Mapper.arraylist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新按钮可见性
     */
    public void update() {
        // 获取contentVBox中元素的数量
        int size = contentVBox.getChildren().size();
        
        // 如果没有元素，直接返回
        if (size == 0) {
            return;
        }
        
        // 遍历所有元素
        for (int i = 0; i < size; i++) {
            // 获取当前VBox
            VBox currentVBox = (VBox) contentVBox.getChildren().get(i);
            // 获取VBox中的HBox
            HBox hBox = (HBox) currentVBox.getChildren().get(0);
            // 获取上下按钮
            ImageView up = (ImageView) hBox.getChildren().get(0);
            ImageView down = (ImageView) hBox.getChildren().get(1);
            
            // 根据位置设置按钮可见性
            if (size == 1) {
                // 如果只有一个元素，上下按钮都不可见
                up.setVisible(false);
                down.setVisible(false);
            } else {
                // 如果是第一个元素，上按钮不可见
                up.setVisible(i != 0);
                // 如果是最后一个元素，下按钮不可见
                down.setVisible(i != size - 1);
            }
        }
    }

    /**
     * 使用给定的项目列表重建UI
     * @param items 要显示的项目列表
     */
    private void rebuildUI(java.util.List<Item> items) {
        // 清空当前的contentVBox
        contentVBox.getChildren().clear();
        
        try {
            // 使用给定的项目重建UI
            for (Item item : items) {
                // 使用BAA工厂创建Box元素
                VBox vBox = Assist.create();
                
                // 获取VBox中的HBox
                HBox hBox = (HBox) vBox.getChildren().get(0);
                
                // 设置项目的HBox
                item.hbox = hBox;
                
                // 获取HBox中的控件
                Label titleLabel = (Label) hBox.getChildren().get(2);
                
                // 将标题填充到标签中
                titleLabel.setText(item.title);
                
                // 为HBox设置UUID
                UUID uuid = UUID.randomUUID();
                String standardUuid = uuid.toString();
                String noHyphenUuid = standardUuid.replace("-", "");
                hBox.getStyleClass().add(noHyphenUuid);
                
                // 设置isEdit为false
                item.isEdit = false;
                
                item.in = true;
                
                // 将VBox添加到contentVBox
                contentVBox.getChildren().add(vBox);
            }
            
            // 重置current和last
            current = 0;
            last = -1;
            
            // 更新背景颜色
            updateBackground();
            
            // 更新按钮可见性
            update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 处理排序复选框事件
     */
    public void handleSort() {
        
        // 如果勾选排序，按count字段排序
        if (sortCheckBox.isSelected()) {
            // 创建原数组的副本以避免修改原数组
            java.util.List<Item> sortedList = new java.util.ArrayList<>(Mapper.arraylist);
            
            // 按count字段升序排序
            sortedList.sort((item1, item2) -> Integer.compare(item2.count, item1.count));
            
            // 使用排序后的项目重建UI
            rebuildUI(sortedList);
        } else {
            // 如果取消勾选，恢复原顺序
            // 使用原项目重建UI
            rebuildUI(Mapper.arraylist);
        }
    }

    
    /**
     * 处理标签点击事件
     * @param label 被点击的标签
     */
    public void handleLabelClick(Label label) {
        // 获取标签所在的HBox
        HBox hBox = (HBox) label.getParent();
        // 获取HBox所在的VBox
        VBox vBox = (VBox) hBox.getParent();
        // 获取VBox在contentVBox中的索引
        int index = contentVBox.getChildren().indexOf(vBox);
        
        // 更新last和current
        last = current;
        current = index;
        
        // 更新背景颜色
        updateBackground();
    }
    
    /**
     * 处理标签双击事件
     * @param label 被双击的标签
     */
    public void handleLabelDoubleClick(Label label) {
        // 获取标签所在的HBox
        HBox hBox = (HBox) label.getParent();
        // 获取HBox所在的VBox
        VBox vBox = (VBox) hBox.getParent();
        // 获取VBox在contentVBox中的索引
        int index = contentVBox.getChildren().indexOf(vBox);

        Item item = Mapper.arraylist.get(index);

        item.count++;
        Platform.runLater(Mapper::save);
        // 打印双击事件信息

        try {
            // 1. 关闭当前窗口
            Stage stage = (Stage) vBox.getScene().getWindow();
            stage.close();
            
            // 2. 在IDEA编辑器中插入内容
            // 使用IDE线程执行IDEA API调用
            new Thread(() -> {
                com.intellij.openapi.application.Application application = com.intellij.openapi.application.ApplicationManager.getApplication();
                application.invokeLater(() -> {
                    // 获取当前打开的项目
                    com.intellij.openapi.project.Project[] projects = com.intellij.openapi.project.ProjectManager.getInstance().getOpenProjects();
                    if (projects.length > 0) {
                        com.intellij.openapi.project.Project project = projects[0];
                        
                        // 获取当前活动编辑器
                        com.intellij.openapi.editor.Editor editor = null;
                        
                        try {
                            // 使用FileEditorManager获取当前活动编辑器（最可靠的方式）
                            com.intellij.openapi.fileEditor.FileEditorManager fileEditorManager = com.intellij.openapi.fileEditor.FileEditorManager.getInstance(project);
                            
                            // 获取当前选中的编辑器
                            com.intellij.openapi.fileEditor.FileEditor[] selectedEditors = fileEditorManager.getSelectedEditors();
                            if (selectedEditors.length > 0) {
                                // 从FileEditor中获取Editor实例
                                for (com.intellij.openapi.fileEditor.FileEditor fileEditor : selectedEditors) {
                                    if (fileEditor instanceof com.intellij.openapi.fileEditor.TextEditor) {
                                        editor = ((com.intellij.openapi.fileEditor.TextEditor) fileEditor).getEditor();
                                        break;
                                    }
                                }
                            }
                            
                            // 如果无法获取选中编辑器，尝试获取所有打开的文本编辑器
                            if (editor == null) {
                                com.intellij.openapi.fileEditor.FileEditor[] allEditors = fileEditorManager.getAllEditors();
                                for (com.intellij.openapi.fileEditor.FileEditor fileEditor : allEditors) {
                                    if (fileEditor instanceof com.intellij.openapi.fileEditor.TextEditor) {
                                        editor = ((com.intellij.openapi.fileEditor.TextEditor) fileEditor).getEditor();
                                        break;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            // 如果获取编辑器失败，记录异常
                            e.printStackTrace();
                        }
                        
                        if (editor != null) {
                            // 获取文档和光标位置
                            com.intellij.openapi.editor.Document document = editor.getDocument();
                            com.intellij.openapi.editor.CaretModel caretModel = editor.getCaretModel();
                            int offset = caretModel.getOffset();
                            
                            // 使用WriteCommandAction确保文档修改操作被正确包装在命令中
                            com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction(project, () -> {
                                // 在命令中执行文档写入操作
                                document.insertString(offset, item.content);
                            });
                        } else {
                            // 当前没有打开的文档，显示弹窗提示
                            Platform.runLater(() -> {
                                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
                                alert.setTitle("警告");
                                alert.setHeaderText(null);
                                alert.setContentText("当前没有打开的文档，无法插入内容！");
                                alert.showAndWait();
                            });
                        }
                    }
                });
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新背景颜色
     */
    public void updateBackground() {
        // 为上一个项目设置背景颜色
        if (last >= 0 && last < contentVBox.getChildren().size()) {
            VBox lastVBox = (VBox) contentVBox.getChildren().get(last);
            HBox lastHBox = (HBox) lastVBox.getChildren().get(0);
            Label lastLabel = (Label) lastHBox.getChildren().get(2);
            lastLabel.setStyle("-fx-background-color: #f4f4f4;");
            lastHBox.setStyle("-fx-background-color: #f4f4f4;");
        }
        
        // 为当前项目设置背景颜色
        if (current >= 0 && current < contentVBox.getChildren().size()) {
            VBox currentVBox = (VBox) contentVBox.getChildren().get(current);
            HBox currentHBox = (HBox) currentVBox.getChildren().get(0);
            Label currentLabel = (Label) currentHBox.getChildren().get(2);
            currentLabel.setStyle("-fx-background-color: " + color + ";");
            currentHBox.setStyle("-fx-background-color: " + color + ";");
        }
    }

    /**
     * 根据索引获取HBox的UUID
     * @param index 索引值
     * @return UUID字符串，无连字符
     */
    private String getUuidByIndex(int index) {
        if (index < 0 || index >= contentVBox.getChildren().size()) {
            return null;
        }
        
        // 根据索引获取VBox
        VBox vBox = (VBox) contentVBox.getChildren().get(index);
        // 获取VBox中的HBox
        HBox hBox = (HBox) vBox.getChildren().get(0);
        
        // 遍历HBox的样式类以查找UUID
        for (String styleClass : hBox.getStyleClass()) {
            // UUID无连字符，长度32位
            if (styleClass.length() == 32 && styleClass.matches("[0-9a-fA-F]+") ) {
                return styleClass;
            }
        }
        
        return null;
    }

    /**
     * 根据UUID获取HBox在contentVBox中的索引
     * @param uuid UUID字符串，无连字符
     * @return 索引值，未找到返回-1
     */
    private int getIndexByUuid(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            return -1;
        }
        
        // 遍历contentVBox中的所有VBox
        for (int i = 0; i < contentVBox.getChildren().size(); i++) {
            VBox vBox = (VBox) contentVBox.getChildren().get(i);
            HBox hBox = (HBox) vBox.getChildren().get(0);
            
            // 检查HBox的样式类是否包含指定的UUID
            if (hBox.getStyleClass().contains(uuid)) {
                return i;
            }
        }
        
        return -1;
    }

    /**
     * 上移功能
     * @param img 上移按钮
     */
    public void up(ImageView img) {
        // 获取当前ImageView所在的HBox
        HBox hBox = (HBox) img.getParent();
        // 获取HBox所在的VBox
        VBox currentVBox = (VBox) hBox.getParent();
        // 获取VBox在contentVBox中的索引
        int index = contentVBox.getChildren().indexOf(currentVBox);
        
        // 只有不是第一个项目时才能上移
        if (index > 0) {
            // 保存last和current项目的UUID
            String lastUuid = getUuidByIndex(last);
            String currentUuid = getUuidByIndex(current);
            
            // 从contentVBox中移除当前项目，然后添加到前一个位置
            contentVBox.getChildren().remove(index);
            contentVBox.getChildren().add(index - 1, currentVBox);
            
            // 更新Mapper.arraylist中的顺序
            Item temp = Mapper.arraylist.get(index);
            Mapper.arraylist.remove(index);
            Mapper.arraylist.add(index - 1, temp);
            
            // 根据UUID重新计算last和current的新索引
            if (lastUuid != null) {
                last = getIndexByUuid(lastUuid);
            }
            if (currentUuid != null) {
                current = getIndexByUuid(currentUuid);
            }
            
            // 更新背景颜色
            updateBackground();
            
            // 调用update方法更新按钮可见性
            update();
        }
    }

    /**
     * 下移功能
     * @param img 下移按钮
     */
    public void down(ImageView img) {
        // 获取当前ImageView所在的HBox
        HBox hBox = (HBox) img.getParent();
        // 获取HBox所在的VBox
        VBox currentVBox = (VBox) hBox.getParent();
        // 获取VBox在contentVBox中的索引
        int index = contentVBox.getChildren().indexOf(currentVBox);
        
        // 只有不是最后一个项目时才能下移
        if (index < contentVBox.getChildren().size() - 1) {
            // 保存last和current项目的UUID
            String lastUuid = getUuidByIndex(last);
            String currentUuid = getUuidByIndex(current);
            
            // 从contentVBox中移除当前项目，然后添加到下一个位置
            contentVBox.getChildren().remove(index);
            contentVBox.getChildren().add(index + 1, currentVBox);
            
            // 更新Mapper.arraylist中的顺序
            Item temp = Mapper.arraylist.get(index);
            Mapper.arraylist.remove(index);
            Mapper.arraylist.add(index + 1, temp);
            
            // 根据UUID重新计算last和current的新索引
            if (lastUuid != null) {
                last = getIndexByUuid(lastUuid);
            }
            if (currentUuid != null) {
                current = getIndexByUuid(currentUuid);
            }
            
            // 更新背景颜色
            updateBackground();
            
            // 调用update方法更新按钮可见性
            update();
        }
    }

    /**
     * 删除功能
     * @param img 删除按钮
     */
    public void delete(ImageView img) {
        // 获取当前ImageView所在的HBox
        HBox hBox = (HBox) img.getParent();
        // 获取HBox所在的VBox
        VBox currentVBox = (VBox) hBox.getParent();
        // 获取VBox在contentVBox中的索引
        int index = contentVBox.getChildren().indexOf(currentVBox);
        
        // 从contentVBox中移除当前项目
        contentVBox.getChildren().remove(index);
        
        // 从Mapper.arraylist中移除当前项目
        Mapper.arraylist.remove(index);
        
        // 更新current和last索引
        if (current == index) {
            // 如果删除的是当前选中项，重新选择下一项或上一项
            if (index < contentVBox.getChildren().size()) {
                // 如果不是最后一项，选择下一项
                current = index;
            } else if (index > 0) {
                // 如果是最后一项，选择上一项
                current = index - 1;
            } else {
                // 如果没有项目了，重置current
                current = -1;
            }
        } else if (current > index) {
            // 如果当前选中项在删除项之后，索引减1
            current--;
        }
        
        // 重置last
        last = -1;
        
        // 更新背景颜色
        updateBackground();
        
        // 调用update方法更新按钮可见性
        update();
    }
}

package com.nfym.insert;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * abc包的VBox工厂类，使用insertVBox.fxml
 * 实现单例模式以方便全局访问
 */
public class Assist {
    
    // 单例模式实现，确保全局只有一个实例
    private static Assist instance = new Assist();

    /**
     * 私有构造函数，防止外部实例化
     */
    private Assist() {
    }
    
    /**
     * VBox项目上移功能
     * 调用asds单例实例的up方法
     * @param img 上移按钮的ImageView
     */
    public void up(ImageView img) {
        InsertX.getInstance().up(img);
    }
    
    /**
     * VBox项目下移功能
     * 调用asds单例实例的down方法
     * @param img 下移按钮的ImageView
     */
    public void down(ImageView img) {
        InsertX.getInstance().down(img);
    }
    
    /**
     * VBox项目删除功能
     * 调用asds单例实例的delete方法
     * @param img 删除按钮的ImageView
     */
    public void delete(ImageView img) {
        InsertX.getInstance().delete(img);
    }

    /**
     * 处理标题标签点击事件
     * 为标签设置单击和双击监听器
     * @param titleLabel 要处理事件的标题Label
     */
    public static void labelHandler(Label titleLabel) {
        titleLabel.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                // 处理标签单击以选择项目
                InsertX.getInstance().handleLabelClick(titleLabel);
            } else if (event.getClickCount() == 2) {
                // 处理标签双击事件
                InsertX.getInstance().handleLabelDoubleClick(titleLabel);
            }
        });
    }

    /**
     * 初始化VBox，获取相应元素并绑定事件
     * @param vBox 要初始化的VBox
     * @return 初始化后的VBox
     */
    private static VBox init(VBox vBox) {
        // 获取VBox中的HBox容器
        HBox hBox = (HBox) vBox.getChildren().get(0);
        
        // 获取HBox中的控件
        ImageView up = (ImageView) hBox.getChildren().get(0);
        ImageView down = (ImageView) hBox.getChildren().get(1);
        Label title = (Label) hBox.getChildren().get(2);
        ImageView delete = (ImageView) hBox.getChildren().get(3);
        
        // 为上移按钮绑定鼠标点击事件
        up.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                instance.up(up);
            }
        });
        
        // 为下移按钮绑定鼠标点击事件
        down.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                instance.down(down);
            }
        });
        
        // 为删除按钮绑定鼠标点击事件
        delete.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                instance.delete(delete);
            }
        });

        // 设置标签点击处理
        labelHandler(title);
        
        return vBox;
    }
    
    /**
     * 使用insertVBox.fxml创建新的VBox实例
     * 加载FXML文件，初始化它，并返回创建的VBox
     * @return 创建并初始化后的VBox
     * @throws IOException 如果加载FXML文件时出错
     */
    public static VBox create() throws IOException {
        // 从demo包资源加载insertVBox.fxml
        FXMLLoader vboxLoader = new FXMLLoader(Assist.class.getResource("/META-INF/insertVBox.fxml"));
        VBox vBox = vboxLoader.load();
        vBox = init(vBox);
        return vBox;
    }
}

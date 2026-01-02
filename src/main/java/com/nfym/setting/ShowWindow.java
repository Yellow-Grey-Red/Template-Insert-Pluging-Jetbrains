package com.nfym.setting;

import java.util.HashMap;
import java.util.Map;

import com.nfym.setting.Item;
import com.nfym.setting.Mapper;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.UUID;

import org.json.JSONObject;
import org.json.JSONException;

public class ShowWindow {
    
    // 单例模式实现
    private static ShowWindow instance = new ShowWindow();

    // 重命名静态字段，避免与VBox类名冲突
    public static VBox contentVBox=null;

    public static TextArea codeTextArea;

    public static boolean hasEmpty=false;

    private ShowWindow() {
    }
    
    // 获取单例实例
    public static ShowWindow getInstance() {
        return instance;
    }
    
    public void show(Stage stage,String str) throws IOException {
        Thread.currentThread().setContextClassLoader(ShowWindow.class.getClassLoader());
        FXMLLoader fxmlLoader = new FXMLLoader(ShowWindow.class.getResource("/META-INF/scrollPane.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle(str);
        stage.getIcons().add(new Image(String.valueOf(ShowWindow.class.getResource("/META-INF/a.png"))));
        stage.setScene(scene);
        stage.show();
        contentVBox=(VBox) scene.lookup("#content");


        codeTextArea = (TextArea) scene.lookup("#code");
        // 绑定按钮事件
        Button addButton = (Button) scene.lookup("#add");
        Button modifyButton = (Button) scene.lookup("#modify");
        Button saveButton = (Button) scene.lookup("#save");
        
        addButton.setOnAction(e -> button_add(addButton));
        modifyButton.setOnAction(e -> button_mod(modifyButton));
        saveButton.setOnAction(e -> button_save(saveButton));


        init();
    }


    public void button_add(Button button){
        if(hasEmpty){
            alert("有空项未处理");
            return;
        }
        try {
            // 创建新的Item对象
            Item newItem = new Item();
            newItem.content = "";
            newItem.count = 0;
            newItem.isEdit = true;
            newItem.in = false;
            newItem.title = "";
            
            // 创建新的VBox
            VBox newVBox = VBoxFactory.create();
            
            // 获取新VBox中的控件
            HBox hBox = (HBox) newVBox.getChildren().get(0);

            String uuid = UUID.randomUUID().toString().replace("-", "");
            hBox.getStyleClass().add(uuid);


            TextField titleTextField = (TextField) hBox.getChildren().get(2);
            
            // 设置新项的title
            titleTextField.setText(newItem.title);
            setTextFieldEditable(titleTextField,true);
            
            // 将新VBox添加到contentVBox
            contentVBox.getChildren().add(newVBox);
            
            // 将新Item添加到Mapper.arraylist
            Mapper.arraylist.add(newItem);
            
            // 设置新Item的hbox
            newItem.hbox = hBox;
            
            // 设置codeTextArea
            codeTextArea.setText(newItem.content);
            setTextAreaEditable(true);
            hasEmpty=true;
            VBoxFactory.updateReference(Mapper.arraylist.size()-1);
            VBoxFactory.setBackground();
            update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public void button_mod(Button button){
        // 检查是否有选中项
        if (VBoxFactory.current >= 0 && VBoxFactory.current < Mapper.arraylist.size()) {
            if(codeTextArea.isEditable()){
                alert("已允许编辑");
                return;
            }
            // 获取当前选中的项
            Item currentItem = Mapper.arraylist.get(VBoxFactory.current);
            
            // 设置isEdit为true
            currentItem.isEdit = true;
            
            // 允许编辑titleTextField
            HBox hBox = currentItem.hbox;
            if (hBox != null) {
                TextField titleTextField = (TextField) hBox.getChildren().get(2);
                setTextFieldEditable(titleTextField,true);
            }
            
            setTextAreaEditable(true);
        }
    }


    public void button_save(Button button){
        // 检查是否有选中项
        if (VBoxFactory.current >= 0 && VBoxFactory.current < Mapper.arraylist.size()) {
            // 获取当前选中的项
            Item currentItem = Mapper.arraylist.get(VBoxFactory.current);

            if(!currentItem.isEdit){
                alert("未被更改");
                return;
            }
            // 保存titleTextField的内容
            HBox hBox = currentItem.hbox;

            TextField titleTextField = (TextField) hBox.getChildren().get(2);
            currentItem.title = titleTextField.getText().trim();
            if(currentItem.title.isEmpty()){
                alert("不允许title为空");
                return;
            }

            currentItem.content = codeTextArea.getText();

            if(currentItem.content.isEmpty()){
                alert("不允许内容为空");
                return;
            }

            setTextFieldEditable(titleTextField,false);
            
            // 保存codeTextArea的内容

            setTextAreaEditable(false);
            currentItem.in=true;
            currentItem.isEdit = false;
            hasEmpty=false;
            // 调用Mapper.save()保存数据
            Mapper.save();
        }
    }


    public void update(){
        // 获取contentVBox中的元素数量
        int size = contentVBox.getChildren().size();
        
        // 如果没有元素，直接返回
        if (size == 0) {
            return;
        }

        if(size==1){
            VBox currentVBox = (VBox) contentVBox.getChildren().get(0);
            // 获取当前VBox中的HBox
            HBox hBox = (HBox) currentVBox.getChildren().get(0);
            // 获取up和down按钮
            ImageView up = (ImageView) hBox.getChildren().get(0);

            ImageView down = (ImageView) hBox.getChildren().get(1);

            up.setVisible(false);
            down.setVisible(false);
            return;
        }

        VBox currentVBox = (VBox) contentVBox.getChildren().get(0);
        // 获取当前VBox中的HBox
        HBox hBox = (HBox) currentVBox.getChildren().get(0);
        // 获取up和down按钮
        ImageView up = (ImageView) hBox.getChildren().get(0);

        ImageView down = (ImageView) hBox.getChildren().get(1);

        up.setVisible(false);
        down.setVisible(true);

        for(int i=1;i<size-1;i++){
            currentVBox = (VBox) contentVBox.getChildren().get(i);
            // 获取当前VBox中的HBox
            hBox = (HBox) currentVBox.getChildren().get(0);
            // 获取up和down按钮
            up = (ImageView) hBox.getChildren().get(0);

            down = (ImageView) hBox.getChildren().get(1);

            up.setVisible(true);
            down.setVisible(true);
        }

        currentVBox = (VBox) contentVBox.getChildren().get(size-1);
        // 获取当前VBox中的HBox
        hBox = (HBox) currentVBox.getChildren().get(0);
        // 获取up和down按钮
        up = (ImageView) hBox.getChildren().get(0);

        down = (ImageView) hBox.getChildren().get(1);

        up.setVisible(true);
        down.setVisible(false);

    }

    public void init(){
        try {
            if(Mapper.arraylist==null){
                Mapper.load();
            }

            // 如果Mapper.arraylist为空，直接返回
            if (Mapper.arraylist == null || Mapper.arraylist.isEmpty()) {
                return;
            }
            
            // 遍历arraylist中的每一项
            for (int i = 0; i < Mapper.arraylist.size(); i++) {
                Item item = Mapper.arraylist.get(i);
                
                // 构造Box元素
                VBox vBox = VBoxFactory.create();
                
                // 获取VBox中的HBox
                HBox hBox = (HBox) vBox.getChildren().get(0);

                // 设置item中的HBox
                item.hbox = hBox;

                // 获取HBox中的TextField
                TextField titleTextField = (TextField) hBox.getChildren().get(2);
                
                // 将array的title填入文本框
                titleTextField.setText(item.title);
                
                // 设置item的isEdit为false
                item.isEdit = false;

                item.in=true;
                
                // 设置TextField不可编辑
                setTextFieldEditable(titleTextField,false);
                
                // 设置VBox的class
                UUID uuid = UUID.randomUUID();
                String standardUuid = uuid.toString();

                String noHyphenUuid = standardUuid.replace("-", "");
                hBox.getStyleClass().add(noHyphenUuid);
                
                // 将VBox添加到contentVBox中
                contentVBox.getChildren().add(vBox);
            }
            
            // 设置codeTextArea内容为第一条item中的content，并设置为不可编辑
            if (!Mapper.arraylist.isEmpty()) {
                Item firstItem = Mapper.arraylist.get(0);
                codeTextArea.setText(firstItem.content);
                setTextAreaEditable(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        update();
    }

    public void setTextFieldEditable(TextField textField,boolean bool){
        textField.setEditable(bool);
        // 设置文本颜色
        if(bool) {
            // 启用时设置为黑色
            textField.setStyle("-fx-text-fill: blue;-fx-background-color:"+parse(textField.getStyle()).get("-fx-background-color"));
        } else {
            // 禁用时设置为灰色
            textField.setStyle("-fx-text-fill: black;-fx-background-color:"+parse(textField.getStyle()).get("-fx-background-color"));
        }
//        System.out.println(textField.getStyle()+"   "+textField.getText()+"   "+bool);
    }

    public static Map<String, String> parse(String str) {
        Map<String, String> map = new HashMap<>();

        // 1. 判空，避免空指针异常
        if (str == null || str.trim().isEmpty()) {
            return map;
        }

        // 2. 按分号分割字符串
        String[] styleItems = str.split(";");

        // 3. 遍历每个样式项
        for (String item : styleItems) {
            // 去除当前项两端空白，过滤空项
            String trimmedItem = item.trim();
            if (trimmedItem.isEmpty()) {
                continue;
            }

            // 4. 按冒号分割属性名和值（只分割第一次出现的冒号，避免值中包含冒号的情况）
            String[] keyValue = trimmedItem.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                // 5. 存入map（重复key会被覆盖）
                map.put(key, value);
            }
        }

        return map;
    }
    public void setTextAreaEditable(boolean bool){
        codeTextArea.setEditable(bool);
        // 设置文本颜色
        if(bool) {
            // 启用时设置为黑色
            codeTextArea.setStyle("-fx-text-fill: black;");
        } else {
            // 禁用时设置为灰色
            codeTextArea.setStyle("-fx-text-fill: #999999;");
        }
    }

    public static void alert(String str){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(str);
        alert.show();
    }
}

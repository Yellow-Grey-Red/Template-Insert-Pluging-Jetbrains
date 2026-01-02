package com.nfym.setting;

import java.util.ArrayList;
import java.util.Map;

import com.nfym.setting.Item;
import com.nfym.setting.Mapper;
import com.nfym.setting.ShowWindow;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONException;

public class VBoxFactory {
    
    // 单例模式，便于全局访问
    private static VBoxFactory instance = new VBoxFactory();

    public static int current=-1;

    public static boolean is=true;
    public static int last=-1;

    private static String color="#A0A0A0";
    
    /**
     * 根据索引获取对应HBox的UUID
     * @param index 索引值
     * @return UUID字符串，无连字符
     */
    private String getUuidByIndex(int index) {
        if (index < 0 || index >= ShowWindow.contentVBox.getChildren().size()) {
            return null;
        }
        
        // 获取对应索引的VBox
        VBox vBox = (VBox) ShowWindow.contentVBox.getChildren().get(index);
        // 获取VBox中的HBox
        HBox hBox = (HBox) vBox.getChildren().get(0);
        
        // 遍历HBox的样式类，找到UUID
        for (String styleClass : hBox.getStyleClass()) {
            // UUID无连字符，长度32位
            if (styleClass.length() == 32 && styleClass.matches("[0-9a-fA-F]+") ) {
                return styleClass;
            }
        }
        
        return null;
    }
    
    /**
     * 根据UUID获取对应HBox在contentVBox中的索引
     * @param uuid UUID字符串，无连字符
     * @return 索引值，未找到返回-1
     */
    private int getIndexByUuid(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            return -1;
        }
        
        // 遍历contentVBox中的所有VBox
        for (int i = 0; i < ShowWindow.contentVBox.getChildren().size(); i++) {
            VBox vBox = (VBox) ShowWindow.contentVBox.getChildren().get(i);
            HBox hBox = (HBox) vBox.getChildren().get(0);
            
            // 检查HBox的样式类中是否包含指定UUID
            if (hBox.getStyleClass().contains(uuid)) {
                return i;
            }
        }
        
        return -1;
    }


    public void up(ImageView img){
        // 获取当前ImageView所在的HBox
        HBox hBox = (HBox) img.getParent();
        // 获取HBox所在的VBox
        VBox currentVBox = (VBox) hBox.getParent();
        // 获取当前VBox在contentVBox中的索引
        int index = ShowWindow.contentVBox.getChildren().indexOf(currentVBox);
        
        // 如果不是第一项，才可以上移
        if (index > 0) {
            // 保存last和current项的UUID
            String lastUuid = getUuidByIndex(last);
            String currentUuid = getUuidByIndex(current);
            
            // 从contentVBox中移除当前项，然后添加到前一个位置
            ShowWindow.contentVBox.getChildren().remove(index);
            ShowWindow.contentVBox.getChildren().add(index - 1, currentVBox);
            
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
            

            setBackground();
            
            // 调用ShowWindow的update方法
            ShowWindow.getInstance().update();
            Mapper.save();
        }
    }

    public void down(ImageView img){
        // 获取当前ImageView所在的HBox
        HBox hBox = (HBox) img.getParent();
        // 获取HBox所在的VBox
        VBox currentVBox = (VBox) hBox.getParent();
        // 获取当前VBox在contentVBox中的索引
        int index = ShowWindow.contentVBox.getChildren().indexOf(currentVBox);
        
        // 如果不是最后一项，才可以下移
        if (index < ShowWindow.contentVBox.getChildren().size() - 1) {
            // 保存last和current项的UUID
            String lastUuid = getUuidByIndex(last);
            String currentUuid = getUuidByIndex(current);
            
            // 从contentVBox中移除当前项，然后添加到后一个位置
            ShowWindow.contentVBox.getChildren().remove(index);
            ShowWindow.contentVBox.getChildren().add(index + 1, currentVBox);
            
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

            setBackground();
            
            // 调用ShowWindow的update方法
            ShowWindow.getInstance().update();
            Mapper.save();
        }
    }

    public void delete(ImageView img){
        // 获取当前ImageView所在的HBox
        HBox hBox = (HBox) img.getParent();
        // 获取HBox所在的VBox
        VBox currentVBox = (VBox) hBox.getParent();
        // 获取当前VBox在contentVBox中的索引
        int index = ShowWindow.contentVBox.getChildren().indexOf(currentVBox);

        // 从contentVBox中移除当前项
        ShowWindow.contentVBox.getChildren().remove(index);

        // 从Mapper.arraylist中移除当前项
        Mapper.arraylist.remove(index);

        // 更新current和last索引
        if (current == index) {
            // 如果删除的是当前选中项，重新选择下一项或上一项
            if (index < ShowWindow.contentVBox.getChildren().size()) {
                // 如果不是最后一项，选择下一项
                current = index;
            } else if (index > 0) {
                // 如果是最后一项，选择上一项
                current = index - 1;
            } else {
                // 如果没有项了，重置current
                current = -1;
            }
        } else if (current > index) {
            // 如果当前选中项在删除项之后，索引减1
            current--;
        }

        setBackground();

        if(last==index){
            last=-1;
        }else if(last>index){
            last--;
        }


        if(current!=-1){
            Item item1 = Mapper.arraylist.get(current);
            if(item1.isEdit){
                TextField textField = (TextField) item1.hbox.getChildren().get(2);
                ShowWindow.getInstance().setTextFieldEditable(textField,true);
            }
            ShowWindow.codeTextArea.setText(item1.content);
        }else {
            ShowWindow.codeTextArea.setText("");
        }
        // 调用ShowWindow的update方法
        ShowWindow.getInstance().update();
        Mapper.save();
    }



    public static void textFile(TextField titleTextField){
        titleTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                VBox vbox = (VBox)titleTextField.getParent().getParent();

                VBox vBox = (VBox) vbox.getParent();
                int i = vBox.getChildren().indexOf(vbox);

                updateReference(i);
                setBackground();

                ArrayList<Item> arraylist = Mapper.arraylist;


                if(current!=-1){
                    Item item1 = arraylist.get(current);
                    TextField textField = (TextField) item1.hbox.getChildren().get(2);
                    if(item1.isEdit){
                        ShowWindow.getInstance().setTextFieldEditable(textField,true);
                        ShowWindow.getInstance().setTextAreaEditable(true);
                    }else {
                        ShowWindow.getInstance().setTextFieldEditable(textField,false);
                        ShowWindow.getInstance().setTextAreaEditable(false);
                    }
                    ShowWindow.codeTextArea.setText(item1.content);
                }else {
                    ShowWindow.codeTextArea.setText("");
                }

            }
        });
    }

    //获取对应元素，绑定事件
    private static VBox init(VBox vBox){
        // 获取VBox中的HBox
        HBox hBox = (HBox) vBox.getChildren().get(0);
        
        // 获取HBox中的控件
        ImageView up = (ImageView) hBox.getChildren().get(0);
        ImageView down = (ImageView) hBox.getChildren().get(1);
        TextField title = (TextField) hBox.getChildren().get(2);
        ImageView delete = (ImageView) hBox.getChildren().get(3);
        
        // 绑定事件处理程序
        up.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                instance.up(up);
            }
        });
        
        down.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                instance.down(down);
            }
        });
        
        delete.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                instance.delete(delete);
            }
        });

        textFile(title);
        
        return vBox;
    }

    public static void setBackground(){
        ArrayList<Item> arraylist = Mapper.arraylist;
        if(last>-1 && last<arraylist.size()){
            HBox hbox = arraylist.get(last).hbox;
            Node node = hbox.getChildren().get(2);
            String s = ShowWindow.parse(node.getStyle()).get("-fx-text-fill");
            String d="-fx-background-color: #f4f4f4;";
            if(s!=null){
                d=d+"-fx-text-fill:"+s;
            }
            node.setStyle(d);
            hbox.getParent().setStyle("-fx-background-color: #f4f4f4;");
        }
        if(current>-1 && current<arraylist.size())
        {
            HBox hbox = arraylist.get(current).hbox;
            Node node = hbox.getChildren().get(2);
            String s = ShowWindow.parse(node.getStyle()).get("-fx-text-fill");
            String d="-fx-background-color: "+color+";";
            if(s!=null){
                d=d+"-fx-text-fill:"+s;
            }
            node.setStyle(d);
            hbox.getParent().setStyle("-fx-background-color: "+color+";");
        }
    }


    public static void updateReference(int i){
        last=current;
        current=i;
    }
    public static VBox create() throws IOException {
        FXMLLoader vboxLoader = new FXMLLoader(VBoxFactory.class.getResource("/META-INF/VBox.fxml"));
        VBox vBox = vboxLoader.load();
        vBox=init(vBox);
        return vBox;
    }
}

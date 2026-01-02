package com.nfym.setting;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.layout.HBox;

public class Item {
    public String content;
    public int count;

    public boolean isEdit;

    public boolean in ;

    public String title;

    @JsonIgnore
    public HBox hbox;


}

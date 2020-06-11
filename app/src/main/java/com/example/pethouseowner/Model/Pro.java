package com.example.pethouseowner.Model;

import java.io.Serializable;

public class Pro implements Serializable {
    private String Name,Image,Description,Price,MenuId;

    public Pro()
    {
    }

    public Pro(String name, String image, String description, String price, String menuId) {
        Name = name;
        Image= image;
        Description = description;
        Price = price;
        MenuId = menuId;
    }
    //  @PropertyName("Name")
    public String getName() {
        return Name;
    }
    // @PropertyName("Name")
    public void setName(String name) {
        Name = name;
    }


    // @PropertyName("Image")
    public String getImage() {
        return Image;
    }
    //   @PropertyName("Image")
    public void setImage(String image) {
        Image = image;
    }

    //  @PropertyName("Description")
    public String getDescription() {
        return Description;
    }
    //  @PropertyName("Description")
    public void setDescription(String description) {
        Description = description;
    }

    //@PropertyName("Price")
    public String getPrice() {
        return Price;
    }
    //  @PropertyName("Price")
    public void setPrice(String price) {
        Price = price;
    }

    // @PropertyName("MenuId")
    public String getMenuId() {
        return MenuId;
    }
    //  @PropertyName("MenuId")
    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}
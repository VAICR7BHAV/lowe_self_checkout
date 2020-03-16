package com.example.lowe_self_checkout;


public class MyListData
{
    private String barcode;
    private String name;
    private String price;
    private String weight;
    public MyListData(String barcode,String name,String price,String weight)
    {
        this.barcode = barcode;
        this.name = name;
        this.price=price;
        this.weight=weight;
    }

    public String getBarcode()
    {
        return barcode;
    }
    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name =name;
    }

    public String getPrice() {
        return price;
    }

    public String getWeight() {
        return weight;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
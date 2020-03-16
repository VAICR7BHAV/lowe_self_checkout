package com.example.lowe_self_checkout;

public class DataObject {
    private String text1;
    private String text2;
    DataObject (String text1, String text2)
    {
        this.text1 = text1;
        this.text2 = text2;
    }

    public String getmText1()
    {
        return text1;
    }

    public void setmText1(String mText1)
    {
        this.text1 = mText1;
    }

    public String getmText2() {
        return text2;
    }

    public void setmText2(String mText2) {
        this.text2 = mText2;
    }


}
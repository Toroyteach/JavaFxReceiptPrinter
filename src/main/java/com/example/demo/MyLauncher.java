package com.example.demo;

import javafx.application.Application;

public class MyLauncher {
    public static void main(String[] args){
        new Thread(() -> Application.launch(HelloApplication.class)).start();
    }
}

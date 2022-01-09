package com.example.demo.data;

public class BarChartData {

    private String name;
    private Double amount;

    public BarChartData() {
    }

    public BarChartData(String name, Double amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

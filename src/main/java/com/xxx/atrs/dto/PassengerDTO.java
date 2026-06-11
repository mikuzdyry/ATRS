package com.xxx.atrs.dto;

import jakarta.validation.constraints.NotBlank;

public class PassengerDTO {

    @NotBlank(message = "乘客姓名不能为空")
    private String name;

    @NotBlank(message = "证件号不能为空")
    private String idCard;

    private String phone;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}

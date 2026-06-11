package com.xxx.atrs.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class BookingRequestDTO {

    @NotNull(message = "请选择航班")
    private Long flightId;

    @NotBlank(message = "请选择舱位")
    private String seatClass;

    @NotEmpty(message = "请填写乘客信息")
    @Valid
    private List<PassengerDTO> passengers;

    @NotBlank(message = "联系人不能为空")
    private String contactName;

    @NotBlank(message = "联系人电话不能为空")
    private String contactPhone;

    private String contactEmail;
    private String remark;

    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }

    public String getSeatClass() { return seatClass; }
    public void setSeatClass(String seatClass) { this.seatClass = seatClass; }

    public List<PassengerDTO> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerDTO> passengers) { this.passengers = passengers; }

    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

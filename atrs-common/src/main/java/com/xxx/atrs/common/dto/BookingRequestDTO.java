package com.xxx.atrs.common.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
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
}

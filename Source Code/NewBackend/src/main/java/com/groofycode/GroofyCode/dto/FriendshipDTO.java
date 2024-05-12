package com.groofycode.GroofyCode.dto;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class FriendshipDTO {
    @NotBlank(message = "Sender ID is required")
    private Long senderId;
    @NotBlank(message = "Receiver ID is required")
    private Long receiverId;
    @NotBlank(message = "Status is required")
    private String status;

}

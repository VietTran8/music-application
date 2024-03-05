package vn.edu.tdtu.musicapplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseResponse<D> {
    private Integer code;
    private boolean status;
    private String message;
    private D data;
}
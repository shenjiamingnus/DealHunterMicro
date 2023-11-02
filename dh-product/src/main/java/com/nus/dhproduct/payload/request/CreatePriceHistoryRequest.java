package com.nus.dhproduct.payload.request;

import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePriceHistoryRequest {

    @NotNull
    private Double price;

    @NotNull
    private Instant createDate;

    @NotNull
    private Long product_id;

}

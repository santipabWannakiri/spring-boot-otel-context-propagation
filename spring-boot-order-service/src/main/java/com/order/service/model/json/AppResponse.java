package com.order.service.model.json;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Response")
public class AppResponse {

    private String appResponseCode;
    private String appMessageCode;
    private String description;

    public AppResponse() {
    }

    public AppResponse(String appResponseCode, String appMessageCode, String description) {
        this.appResponseCode = appResponseCode;
        this.appMessageCode = appMessageCode;
        this.description = description;
    }


}

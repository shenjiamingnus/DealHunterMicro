package com.nus.dhproduct.controller;

import com.nus.dhproduct.payload.response.GeneralApiResponse;
import com.nus.dhproduct.service.ImgService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
public class ImgController {

  @Autowired
  public ImgService imgService;

  @PostMapping("/upload")
  public ResponseEntity<?> uploadImg(@RequestParam MultipartFile file) throws IOException {
    String address = imgService.uploadImage(file);
    if (address != null) {
      return ResponseEntity.ok(new GeneralApiResponse(true, "Upload Successfully!", address));
    }
    return ResponseEntity.ok(new GeneralApiResponse(false, "Upload Failed."));
  }

}

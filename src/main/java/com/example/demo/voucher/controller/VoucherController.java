package com.example.demo.voucher.controller;

import com.example.demo.common.Response;
import com.example.demo.voucher.dto.AddResponse;
import com.example.demo.voucher.dto.VoucherDto;
import com.example.demo.voucher.dto.VoucherReponse;
import com.example.demo.voucher.repository.VoucherRepository;
import com.example.demo.voucher.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.demo.voucher.model.Voucher;

@RestController
@RequestMapping("vouchers")
public class VoucherController {

    @Autowired
    VoucherService voucherService;

    @Autowired
    VoucherRepository voucherRepository;
    @GetMapping("")
    public ResponseEntity<?> getUsersVouchers(){
        return Response.response(voucherService.getUsersVouchers(), 200, "Success");
    }
    @PostMapping(value = "add", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addVoucher(@ModelAttribute VoucherDto voucherDto){
        if(voucherRepository.findByName(voucherDto.getName()) !=null){
            return Response.response(null, 400, "Existed voucher");
        }
        return Response.response(voucherService.addVoucher(voucherDto), 200, "Success");
    }

    @PutMapping(value = "update/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateVoucher(@ModelAttribute VoucherDto voucherDto,  @PathVariable("id") int id){
        if(voucherRepository.findById(id) == null){
            return Response.response(null, 400, "Not found voucher");
        }
        return Response.response(voucherService.updateVoucher(voucherDto, id), 200, "Success");
    }

}

package com.example.demo.voucher.service;

import com.example.demo.product.cloudinary.CloudinaryService;
import com.example.demo.user.model.SaveAccount;
import com.example.demo.user.model.Users;
import com.example.demo.utils.DateUtils;
import com.example.demo.voucher.dto.*;
import com.example.demo.voucher.model.UserVoucher;
import com.example.demo.voucher.model.Voucher;
import com.example.demo.voucher.repository.UserVoucherRepository;
import com.example.demo.voucher.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoucherService {
    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    UserVoucherRepository userVoucherRepository;

    @Autowired
    CloudinaryService cloudinaryService;

    public List<VoucherReponse> getUsersVouchers(){
        Users user = SaveAccount.users;
        return userVoucherRepository.getUsersVouchers(user.getId()).stream().map(vch -> copyVoucher(vch))
                .collect(Collectors.toList())
                ;
    }
    public VoucherReponse copyVoucher(UserVoucher vch){
        VoucherReponse response = new VoucherReponse(
                vch.getId(), vch.getVch().getImage(), vch.getVch().getName(),
                DateUtils.dateFormat(vch.getVch().getDate()),vch.getVch().getExpiredDaysLeft(),vch.isUsed(),vch.getVch().getDescription(),
                vch.getVch().getVoucherType().name(), vch.getVch().getVoucherDiscountType().name(), vch.getVch().getDiscount()
        );
        return response;
    }
    public Voucher addVoucher(VoucherDto voucherDto){
        Voucher voucher = new Voucher();
        voucher.setName(voucherDto.getName());
        voucher.setVoucherType(voucherDto.getVoucherType());
        voucher.setDate(voucherDto.getDate());
        voucher.setDescription(voucherDto.getDescription());
        voucher.setImage(cloudinaryService.uploadURL(voucherDto.getFile()));
        voucher.setDiscount(voucherDto.getDiscount());
        voucher.setQuantity(voucherDto.getQuantity());
        voucher.setVoucherDiscountType(voucherDto.getVoucherDiscountType());
        voucherRepository.save(voucher);
        return voucher;
    }


    public Voucher updateVoucher(VoucherDto voucherDto, int id){
        Voucher voucher = voucherRepository.findById(id);
        voucher.setName(voucherDto.getName());
        voucher.setVoucherType(voucherDto.getVoucherType());
        voucher.setDate(voucherDto.getDate());
        voucher.setDescription(voucherDto.getDescription());
        voucher.setImage(cloudinaryService.uploadURL(voucherDto.getFile()));
        voucher.setDiscount(voucherDto.getDiscount());
        voucher.setQuantity(voucherDto.getQuantity());
        voucher.setVoucherDiscountType(voucherDto.getVoucherDiscountType());
        voucherRepository.save(voucher);
        return voucher;
    }
    public List<Voucher> getVoucher(Voucher vchs){
        return voucherRepository.findAll();
    }

    public void changeStatusVoucher(int id, boolean status){
        UserVoucher userVoucher = userVoucherRepository.findUserVoucherById(id);
        userVoucher.setUsed(status);
        userVoucherRepository.save(userVoucher);
    }

}

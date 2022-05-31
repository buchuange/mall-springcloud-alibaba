package com.dawn.mall.dto;

import com.dawn.mall.domain.Shipping;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;

/**
 * @Author: Dawn
 * @Date: 2022/5/16 02:39
 */
@Data
public class ShippingDTO {

    @NotBlank
    private String receiverName;

    private String receiverPhone;

    @NotBlank
    private String receiverMobile;

    @NotBlank
    private String receiverProvince;

    @NotBlank
    private String receiverCity;

    @NotBlank
    private String receiverDistrict;

    @NotBlank
    private String receiverAddress;

    private String receiverZip;

    public Shipping convertToShipping() {

        ShippingDTOConvert shippingDTOConvert = new ShippingDTOConvert();

        return shippingDTOConvert.convert(this);
    }

    public static class ShippingDTOConvert implements Converter<ShippingDTO, Shipping> {

        @Override
        public Shipping convert(ShippingDTO shippingDTO) {

            Shipping shipping = new Shipping();
            BeanUtils.copyProperties(shippingDTO, shipping);

            return shipping;
        }
    }
}

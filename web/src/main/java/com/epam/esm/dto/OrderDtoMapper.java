package com.epam.esm.dto;


import com.epam.esm.model.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDtoMapper extends DateMapper {

    private final CertificateDtoMapper certificateDtoMapper;

    public OrderDtoMapper(CertificateDtoMapper certificateDtoMapper) {
        this.certificateDtoMapper = certificateDtoMapper;
    }

    public OrderDto toDto(Order order) {
        String updateDate = toISOFormatDate(order.getUpdateDate());
        List<CertificateDto> certificateDtoList = order.getCertificates()
                .stream()
                .map(certificateDtoMapper::toDto)
                .collect(Collectors.toList());
        return new OrderDto(order.getId(), order.getUser(), updateDate, order.getCost(),
                certificateDtoList);
    }






}

package com.epam.esm.dto;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoMapper extends DateMapper {

    public OrderDto toDto(Order order) {
        String updateDate = toISOFormatDate(order.getUpdateDate());
        Double cost = extractOrderCost(order);
        return new OrderDto(order.getId(), order.getUser(), updateDate, cost,
                order.getCertificates());
    }


    private Double extractOrderCost(Order order) {
        Double cost = 0d;
        for (Certificate certificate : order.getCertificates()) {
            if (certificate.getPrice() != null) {
                cost += certificate.getPrice();
            }
        }
       return cost;

    }


}

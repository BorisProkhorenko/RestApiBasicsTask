package com.epam.esm.dto;

import com.epam.esm.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoMapper extends DateMapper{

        public OrderDto toDto(Order order) {
            String updateDate = toISOFormatDate(order.getUpdateDate());

            return new OrderDto(order.getId(), order.getUser(), updateDate, order.getCost(),
                    order.getCertificates());
        }


    }

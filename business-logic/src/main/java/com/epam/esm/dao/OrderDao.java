package com.epam.esm.dao;

import com.epam.esm.model.Order;

public interface OrderDao extends Dao<Order>{
   void mapSnapshots(Order order);
}

package com.example.bookstore.repository;

import com.example.bookstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserIdOrderById(Long userId);
    @Query("""
      SELECT o 
      FROM Order o
      ORDER BY 
        CASE WHEN o.status = com.example.bookstore.Enum.OrderStatus.DELIVERED THEN 1 ELSE 0 END,
        o.id
      """)
    List<Order> findAllWithDeliveredLast();
}

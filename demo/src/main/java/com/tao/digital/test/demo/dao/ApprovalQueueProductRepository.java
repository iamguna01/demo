package com.tao.digital.test.demo.dao;

import com.tao.digital.test.demo.entity.ApprovalQueue;
import com.tao.digital.test.demo.entity.ApprovalQueueProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalQueueProductRepository extends JpaRepository<ApprovalQueueProduct, Long> {

}

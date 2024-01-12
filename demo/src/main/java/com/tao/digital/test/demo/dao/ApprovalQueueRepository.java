package com.tao.digital.test.demo.dao;

import com.tao.digital.test.demo.entity.ApprovalQueue;
import com.tao.digital.test.demo.entity.ApprovalQueueProduct;
import com.tao.digital.test.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApprovalQueueRepository extends JpaRepository<ApprovalQueue, Long> {
    List<ApprovalQueue> findAllByOrderByRequestDateAsc();

    Optional<ApprovalQueue> findByApprovalQueueProduct(ApprovalQueueProduct product);

}